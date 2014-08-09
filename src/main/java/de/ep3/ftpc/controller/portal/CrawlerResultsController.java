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

package de.ep3.ftpc.controller.portal;

import de.ep3.ftpc.controller.AbstractController;
import de.ep3.ftpc.model.*;
import de.ep3.ftpc.view.core.CrawlerResultsItem;
import de.ep3.ftpc.view.core.CrawlerResultsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This controller uses a swing timer to periodically update the GUI to reflect the current crawler results.
 *
 * This includes sending messages to the status bar and adding crawler result to the panel.
 *
 * Once started, the update timer will continue to run in order to process delayed information from
 * the crawler thread.
 */
public class CrawlerResultsController extends AbstractController implements CrawlerListener, ActionListener
{

    private CrawlerDownloadController crawlerDownloadController;
    private CrawlerResultsPanel crawlerResultsPanel;
    private CrawlerManager crawlerManager;
    private StatusManager statusManager;
    private ConfigurationManager configManager;

    private Timer updateTimer;

    private JPanel crawlerResultsScrollPanel;
    private int crawlerResultsProcessed;
    private String lastCrawlerStatusMessage;

    public CrawlerResultsController(CrawlerDownloadController crawlerDownloadController, CrawlerResultsPanel crawlerResultsPanel,
        CrawlerManager crawlerManager, StatusManager statusManager, ConfigurationManager configManager)
    {
        this.crawlerDownloadController = crawlerDownloadController;
        this.crawlerResultsPanel = crawlerResultsPanel;
        this.crawlerManager = crawlerManager;
        this.statusManager = statusManager;
        this.configManager = configManager;

        updateTimer = new Timer(100, this);
    }

    @Override
    public void dispatch()
    {
        crawlerManager.getCrawler().addListener(this);
    }

    @Override
    public void crawlerStarted(CrawlerEvent e)
    {
        if (updateTimer.isRunning()) {
            updateTimer.stop();
        }

        crawlerResultsProcessed = 0;

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                crawlerResultsScrollPanel = crawlerResultsPanel.initializeForResults();

                statusManager.setNotes(i18n.translate("portalResultCount", 0));
            }
        });

        updateTimer.start();
    }

    @Override
    public void crawlerPaused(CrawlerEvent e)
    { }

    @Override
    public void crawlerResumed(CrawlerEvent e)
    { }

    @Override
    public void crawlerStopped(CrawlerEvent e)
    { }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        displayThreadMessages();
        updateResultsPanel();
        updateResultsPanelScroll();
        updateStatusBar();
    }

    private void displayThreadMessages()
    {
        ConcurrentLinkedQueue<String> messageQueue = crawlerManager.getCrawlerThread().getMessageQueue();

        while (messageQueue.peek() != null) {
            String message = messageQueue.poll();

            JOptionPane.showMessageDialog(crawlerResultsPanel, message, null, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateResultsPanel()
    {
        CrawlerThread crawlerThread = crawlerManager.getCrawlerThread();

        int crawlerResultsCount = crawlerThread.getCrawlerResults().size();

        if (crawlerResultsCount > crawlerResultsProcessed) {
            CrawlerResult crawlerResult = crawlerThread.getCrawlerResults().get(crawlerResultsProcessed);

            if (crawlerResult.getPreviewPercentage() == 100) {
                CrawlerResultsItem item = new CrawlerResultsItem(crawlerResult);

                if (crawlerResultsScrollPanel != null) {
                    crawlerResultsScrollPanel.add(item, "grow");
                    crawlerResultsProcessed++;

                    item.getPreviewPanel().addMouseListener(crawlerDownloadController);

                    statusManager.setNotes(i18n.translate("portalResultCount", crawlerResultsCount));
                }
            }
        }
    }

    private void updateResultsPanelScroll()
    {
        if (crawlerManager.getCrawler().getStatus() == Crawler.Status.RUNNING) {
            Configuration config = configManager.getConfig();

            if (! (config.has("app.view.scroll") && config.get("app.view.scroll").equals("false"))) {
                JScrollPane scrollPane = crawlerResultsPanel.getScrollPane();

                if (scrollPane != null) {
                    JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
                    scrollBar.setValue(scrollBar.getMaximum());
                }
            }
        }
    }

    private void updateStatusBar()
    {
        CrawlerThread crawlerThread = crawlerManager.getCrawlerThread();

        String crawlerStatusMessage = crawlerThread.getStatusMessage();

        if (crawlerStatusMessage != null && crawlerStatusMessage.length() > 0) {
            if (crawlerThread.getCrawlerResults().size() > crawlerResultsProcessed) {
                int previewPercentage = 0;

                if (crawlerThread.getCrawlerResults().size() > 0) {
                    previewPercentage = crawlerThread.getCrawlerResults().lastElement().getPreviewPercentage();
                }

                if (previewPercentage > 0) {
                    crawlerStatusMessage += " (" + previewPercentage + "%)";
                }
            }

            if (lastCrawlerStatusMessage == null || ! lastCrawlerStatusMessage.equals(crawlerStatusMessage)) {
                lastCrawlerStatusMessage = crawlerStatusMessage;

                statusManager.add(crawlerStatusMessage);
            }
        }
    }

}