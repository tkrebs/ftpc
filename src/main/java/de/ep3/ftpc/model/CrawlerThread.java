/*
 * FTP Crawler - A simple file crawler for UNIX based FTP servers
 * Copyright (C) 2014 Tobias Krebs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.ep3.ftpc.model;

import de.ep3.ftpc.model.i18n.I18nManager;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This is the always running (but most of its time sleeping) daemon crawler thread.
 *
 * It manages a list of crawler results and messages that can be asynchronously obtained
 * by other classes (usually by the crawler results controller, who will then be
 * responsible to render them).
 */
public class CrawlerThread extends Thread
{

    private Crawler crawler;
    private I18nManager i18n;

    private FTPClient ftpClient;
    private boolean loggedIn = false;
    private Server currentServer;

    private CrawlerDirectories crawlerDirectories;
    private Vector<CrawlerResult> crawlerResults;
    private ConcurrentLinkedQueue<String> messageQueue;
    private String statusMessage;

    private int defaultSleep = 1000;
    private int runningSleep = 10;
    private int pausedSleep = 2000;
    private int currentSleep = defaultSleep;

    public CrawlerThread(Crawler crawler, I18nManager i18n)
    {
        this.crawler = crawler;
        this.i18n = i18n;

        crawlerResults = new Vector<>();
        messageQueue = new ConcurrentLinkedQueue<>();

        setDaemon(true);
        start();
    }

    public Vector<CrawlerResult> getCrawlerResults()
    {
        return crawlerResults;
    }

    public ConcurrentLinkedQueue<String> getMessageQueue()
    {
        return messageQueue;
    }

    public synchronized String getStatusMessage()
    {
        return statusMessage;
    }

    public synchronized void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }

    private synchronized void setTranslatedStatusMessage(String token)
    {
        this.statusMessage = i18n.translate(token);
    }

    private synchronized void setTranslatedStatusMessage(String token, Object... args)
    {
        this.statusMessage = String.format(i18n.translate(token), args);
    }

    @Override
    public void run()
    {
        while (! isInterrupted()) {
            try {
                runStatusCheck();
                Thread.sleep(currentSleep);
            } catch (InterruptedException e) {
                interrupt();
            }
        }

        tryDisconnect();
    }

    private void runStatusCheck()
    {
        switch (crawler.getStatus()) {
            case IDLE:
                tryDisconnect();
                currentSleep = defaultSleep;
                break;
            case RUNNING:
                if (ftpClient == null) {
                    ftpClient = new FTPClient();
                }

                if (! ftpClient.isConnected()) {
                    currentServer = crawler.getServer();

                    try {
                        setTranslatedStatusMessage("crawlerTryConnect");

                        ftpClient.connect(currentServer.need("server.ip"), Integer.parseInt(currentServer.need("server.port")));

                        int replyCode = ftpClient.getReplyCode();

                        if (! FTPReply.isPositiveCompletion(replyCode)) {
                            throw new IOException(i18n.translate("crawlerServerRefused"));
                        }

                        if (currentServer.has("user.name")) {
                            String userName = currentServer.get("user.name");
                            String userPassword = "";

                            if (currentServer.hasTemporary("user.password")) {
                                userPassword = currentServer.getTemporary("user.password");
                            }

                            loggedIn = ftpClient.login(userName, userPassword);

                            if (! loggedIn) {
                                throw new IOException(i18n.translate("crawlerServerAuthFail"));
                            }
                        }

                        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                        setTranslatedStatusMessage("crawlerConnected");

                        /* Prepare directories */

                        crawlerDirectories = new CrawlerDirectories(ftpClient, currentServer.get("include-paths"), currentServer.get("exclude-paths"));
                        crawlerResults.clear();

                    } catch (IOException e) {
                        messageQueue.add(i18n.translate("crawlerConnectionFailed", currentServer.need("server.name"), e.getMessage()));
                        tryStop();

                        return;
                    }
                }

                try {
                    runSearch();
                } catch (IOException e) {
                    messageQueue.add(i18n.translate("crawlerProblem", e.getMessage()));
                    tryStop();
                }

                currentSleep = runningSleep;
                break;
            case PAUSED:
                if (ftpClient != null && ftpClient.isConnected()) {
                    try {
                        ftpClient.noop();

                        setTranslatedStatusMessage("crawlerPaused");
                    } catch (IOException e) {
                        messageQueue.add(e.getMessage());
                        tryStop();
                    }
                }

                currentSleep = pausedSleep;
                break;
        }
    }

    private void runSearch() throws IOException
    {
        CrawlerFile file = crawlerDirectories.getNextFile();

        if (file == null) {
            setTranslatedStatusMessage("crawlerDone");
            tryStop();
        } else {
            setTranslatedStatusMessage("crawlDir", file.getFullName());

            if (file.isRelevant(currentServer, i18n)) {
                String fileType = currentServer.get("crawler.file-type");

                CrawlerResult crawlerResult = new CrawlerResult(file, fileType, currentServer, ftpClient);

                // Add crawler results to the list, so that the panel can display the download progress already
                crawlerResults.add(crawlerResult);

                switch (fileType) {
                    case "image-all":
                    case "image-custom":
                        try {
                            crawlerResult.generatePreview();
                        } catch (IOException e) {
                            crawlerResult.setPreviewPercentage(100); // But no preview image :'(
                        }
                        break;
                    default:
                        crawlerResult.setPreviewPercentage(100);
                        break;
                }
            }
        }
    }

    private void tryStop()
    {
        tryDisconnect();

        switch (crawler.getStatus()) {
            case RUNNING:
            case PAUSED:
                crawler.stop();
                break;
        }

        setTranslatedStatusMessage("crawlerStopped");
    }

    private void tryDisconnect()
    {
        if (ftpClient != null && ftpClient.isConnected()) {
            setTranslatedStatusMessage("crawlerTryDisconnect");

            try {
                if (loggedIn) {
                    ftpClient.logout();
                }

                ftpClient.disconnect();

                setTranslatedStatusMessage("crawlerDisconnected");
            } catch (IOException e) {
                messageQueue.add(e.getMessage());
            }
        }

        loggedIn = false;
        currentServer = null;
    }

}