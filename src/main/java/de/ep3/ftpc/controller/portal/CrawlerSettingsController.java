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
import de.ep3.ftpc.view.core.CrawlerSettingsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CrawlerSettingsController extends AbstractController implements ServerListListener, FocusListener, ActionListener
{

    private CrawlerSettingsPanel crawlerSettingsPanel;
    private ServerListManager serverListManager;

    public CrawlerSettingsController(CrawlerSettingsPanel crawlerSettingsPanel, ServerListManager serverListManager)
    {
        this.crawlerSettingsPanel = crawlerSettingsPanel;
        this.serverListManager = serverListManager;
    }

    @Override
    public void dispatch()
    {
        serverListManager.getServerList().addListener(this);

        registerFormListeners();
        storeCrawlerSettings();
        updateCrawlerSettings();
    }

    private void registerFormListeners()
    {
        if (crawlerSettingsPanel.getFileType() != null) {
            crawlerSettingsPanel.getFileType().addFocusListener(this);
            crawlerSettingsPanel.getFileName().addFocusListener(this);
            crawlerSettingsPanel.getFileSizeMin().addFocusListener(this);
            crawlerSettingsPanel.getFileSizeMax().addFocusListener(this);
            crawlerSettingsPanel.getFileDateStart().addFocusListener(this);
            crawlerSettingsPanel.getFileDateEnd().addFocusListener(this);

            crawlerSettingsPanel.getFileType().addActionListener(this);
        }
    }

    @Override
    public void serverListUpdated(ServerListEvent e)
    { }

    @Override
    public void serverActivated(ServerListEvent e)
    {
        ServerList serverList = (ServerList) e.getSource();

        crawlerSettingsPanel.initialize(serverList.getActiveServer());

        registerFormListeners();
        storeCrawlerSettings();
        updateCrawlerSettings();
    }

    private void storeCrawlerSettings()
    {
        Server server = serverListManager.getServerList().getActiveServer();

        if (server != null && crawlerSettingsPanel.getFileType() != null) {

            int fileTypeIndex = crawlerSettingsPanel.getFileType().getSelectedIndex();

            String fileType;
            String fileName = crawlerSettingsPanel.getFileName().getText().trim();
            String fileSizeMin = crawlerSettingsPanel.getFileSizeMin().getText().trim();
            String fileSizeMax = crawlerSettingsPanel.getFileSizeMax().getText().trim();
            String fileDateStart = crawlerSettingsPanel.getFileDateStart().getText().trim();
            String fileDateEnd = crawlerSettingsPanel.getFileDateEnd().getText().trim();

            String fileNameImageRegex = ".+(\\.jpg|\\.jpeg|\\.png|\\.gif)$";

            switch (fileTypeIndex) {
                case 0:
                    fileType = "image-all";
                    fileName = fileNameImageRegex;
                    break;
                case 1:
                    fileType = "image-custom";
                    break;
                case 2:
                    fileType = "file-custom";

                    if (fileName.equals(fileNameImageRegex)) {
                        fileName = "";
                    }
                    break;
                default:
                    throw new IllegalStateException("Unsupported file type selected");
            }

            server.put("crawler.file-type", fileType);
            server.put("crawler.file-name", fileName);

            try {
                if (! fileSizeMin.matches("^[0-9]*$")) {
                    crawlerSettingsPanel.getFileSizeMin().setText(server.get("crawler.file-size-min"));

                    throw new IllegalStateException(
                        i18n.translate("portalCrawlerFileSize") + ":   " + i18n.translate("errorInputNoInteger"));
                }

                server.put("crawler.file-size-min", fileSizeMin);

                if (! fileSizeMax.matches("^[0-9]*$")) {
                    crawlerSettingsPanel.getFileSizeMax().setText(server.get("crawler.file-size-max"));

                    throw new IllegalStateException(
                        i18n.translate("portalCrawlerFileSize") + ":   " + i18n.translate("errorInputNoInteger"));
                }

                server.put("crawler.file-size-max", fileSizeMax);

                if (fileDateStart.length() > 0 && ! i18n.isDate(fileDateStart)) {
                    crawlerSettingsPanel.getFileDateStart().setText(server.get("crawler.file-date-start"));

                    throw new IllegalStateException(
                        i18n.translate("portalCrawlerFileDate") + ":   " + i18n.translate("errorInputNoDate"));
                }

                server.put("crawler.file-date-start", fileDateStart);

                if (fileDateEnd.length() > 0 && ! i18n.isDate(fileDateEnd)) {
                    crawlerSettingsPanel.getFileDateEnd().setText(server.get("crawler.file-date-end"));

                    throw new IllegalStateException(
                        i18n.translate("portalCrawlerFileDate") + ":   " + i18n.translate("errorInputNoDate"));
                }

                server.put("crawler.file-date-end", fileDateEnd);
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(crawlerSettingsPanel, ex.getMessage(), null, JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void updateCrawlerSettings()
    {
        Server server = serverListManager.getServerList().getActiveServer();

        if (server != null && crawlerSettingsPanel.getFileType() != null) {
            switch (server.get("crawler.file-type")) {
                case "image-all":
                    crawlerSettingsPanel.getFileName().setEnabled(false);
                    break;
                case "image-custom":
                case "file-custom":
                    crawlerSettingsPanel.getFileName().setEnabled(true);
                    break;
            }

            crawlerSettingsPanel.getFileName().setText(server.get("crawler.file-name"));
        }
    }

    @Override
    public void focusGained(FocusEvent e)
    { }

    @Override
    public void focusLost(FocusEvent e)
    {
        storeCrawlerSettings();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        storeCrawlerSettings();
        updateCrawlerSettings();
    }

}