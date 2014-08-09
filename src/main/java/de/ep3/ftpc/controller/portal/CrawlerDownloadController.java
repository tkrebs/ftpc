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
import de.ep3.ftpc.model.CrawlerFile;
import de.ep3.ftpc.model.CrawlerResult;
import de.ep3.ftpc.model.Server;
import de.ep3.ftpc.view.core.CrawlerResultsItem;
import de.ep3.ftpc.view.portal.PortalFrame;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Controls the download of a crawler result file to a local file by clicking on it in the crawler results panel.
 */
public class CrawlerDownloadController extends AbstractController implements MouseListener
{

    private PortalFrame portalFrame;

    public CrawlerDownloadController(PortalFrame portalFrame)
    {
        this.portalFrame = portalFrame;
    }

    @Override
    public void dispatch()
    { }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        CrawlerResultsItem.PreviewPanel previewPanel = (CrawlerResultsItem.PreviewPanel) e.getSource();
        CrawlerResult crawlerResult = previewPanel.getCrawlerResult();
        CrawlerFile crawlerFile = crawlerResult.getFile();

        FTPClient ftpClient = crawlerResult.getFtpClient();

        if (ftpClient.isConnected()) {
            JOptionPane.showMessageDialog(portalFrame, i18n.translate("crawlerDownloadWhileConnected"), null, JOptionPane.ERROR_MESSAGE);
            return;
        }

        String fileExtension = crawlerFile.getExtension();

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter chooserFilter = new FileNameExtensionFilter(
            i18n.translate("fileType", fileExtension.toUpperCase()), crawlerFile.getExtension());

        chooser.setApproveButtonText(i18n.translate("buttonSave"));
        chooser.setDialogTitle(i18n.translate("fileDownloadTo"));
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setFileFilter(chooserFilter);
        chooser.setSelectedFile(new File(crawlerFile.getName()));

        int selection = chooser.showSaveDialog(portalFrame);

        if (selection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = chooser.getSelectedFile();

            Server relatedServer = crawlerResult.getServer();

            try {
                ftpClient.connect(relatedServer.need("server.ip"), Integer.parseInt(relatedServer.need("server.port")));

                int replyCode = ftpClient.getReplyCode();

                if (! FTPReply.isPositiveCompletion(replyCode)) {
                    throw new IOException(i18n.translate("crawlerServerRefused"));
                }

                if (relatedServer.has("user.name")) {
                    String userName = relatedServer.get("user.name");
                    String userPassword = "";

                    if (relatedServer.hasTemporary("user.password")) {
                        userPassword = relatedServer.getTemporary("user.password");
                    }

                    boolean loggedIn = ftpClient.login(userName, userPassword);

                    if (! loggedIn) {
                        throw new IOException(i18n.translate("crawlerServerAuthFail"));
                    }
                }

                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                /* Download file */

                InputStream is = ftpClient.retrieveFileStream(crawlerFile.getFullName());

                if (is != null) {

                    byte[] rawFile = new byte[(int) crawlerFile.getSize()];

                    int i = 0;

                    while (true) {
                        int b = is.read();

                        if (b == -1) {
                            break;
                        }

                        rawFile[i] = (byte) b;
                        i++;

                        /* Occasionally update the download progress */

                        if (i % 1024 == 0) {
                            int progress = Math.round((((float) i) / crawlerFile.getSize()) * 100);

                            status.add(i18n.translate("crawlerDownloadProgress", progress));
                        }
                    }

                    is.close();
                    is = null;

                    if (! ftpClient.completePendingCommand()) {
                        throw new IOException();
                    }

                    Files.write(fileToSave.toPath(), rawFile);
                }

                /* Logout and disconnect */

                ftpClient.logout();

                tryDisconnect(ftpClient);

                status.add(i18n.translate("crawlerDownloadDone"));
            } catch (IOException ex) {
                tryDisconnect(ftpClient);

                JOptionPane.showMessageDialog(portalFrame, i18n.translate("crawlerDownloadFailed", ex.getMessage()), null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    { }

    @Override
    public void mouseReleased(MouseEvent e)
    { }

    @Override
    public void mouseEntered(MouseEvent e)
    { }

    @Override
    public void mouseExited(MouseEvent e)
    { }

    private void tryDisconnect(FTPClient ftpClient)
    {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
            } catch (IOException e) { }
        }
    }

}