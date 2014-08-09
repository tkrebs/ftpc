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
import de.ep3.ftpc.view.core.CrawlerResultsPanel;
import de.ep3.ftpc.view.core.CrawlerSettingsPanel;
import de.ep3.ftpc.view.core.ServerListItem;
import de.ep3.ftpc.view.core.ServerListPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Manages starting, stopping, pausing and resuming the crawler from the crawler settings panel buttons.
 *
 * When crawling is started, components like the server list and the crawler settings are disabled,
 * so that they cannot be altered during crawling.
 */
public class CrawlerController extends AbstractController implements ActionListener, CrawlerListener
{

    private ServerListPanel serverListPanel;
    private CrawlerSettingsPanel crawlerSettingsPanel;
    private CrawlerResultsPanel crawlerResultsPanel;

    private ServerListManager serverListManager;
    private CrawlerManager crawlerManager;

    public CrawlerController(ServerListPanel serverListPanel, CrawlerSettingsPanel crawlerSettingsPanel, CrawlerResultsPanel crawlerResultsPanel,
        ServerListManager serverListManager, CrawlerManager crawlerManager)
    {
        this.serverListPanel = serverListPanel;
        this.crawlerSettingsPanel = crawlerSettingsPanel;
        this.crawlerResultsPanel = crawlerResultsPanel;

        this.serverListManager = serverListManager;
        this.crawlerManager = crawlerManager;
    }

    @Override
    public void dispatch()
    {
        registerFormListeners();

        crawlerManager.getCrawler().addListener(this);
    }

    private void registerFormListeners()
    {
        if (crawlerSettingsPanel.getStartButton() != null) {
            crawlerSettingsPanel.getStartButton().addActionListener(this);
            crawlerSettingsPanel.getStopButton().addActionListener(this);
            crawlerSettingsPanel.getPauseButton().addActionListener(this);
            crawlerSettingsPanel.getResumeButton().addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JButton button = (JButton) e.getSource();

        switch (button.getName()) {
            case "start":
                Server activeServer = serverListManager.getServerList().getActiveServer();

                /*
                 * If the user has not yet provided a user name for the current server,
                 * he will be prompted to provide one now, but can also continue with an empty name.
                 */

                if (! activeServer.has("user.name")) {
                    String userName = JOptionPane.showInputDialog(crawlerSettingsPanel,
                        i18n.translate("serverSettingsUserName"),
                        i18n.translate("serverSettingsTitle"), JOptionPane.PLAIN_MESSAGE);

                    if (userName == null) {
                        break;
                    }

                    activeServer.put("user.name", userName);
                }

                /*
                 * If the user has not yet provided a user password for the current server,
                 * he will be prompted to provide one now, but can also continue with an empty password.
                 */

                if (activeServer.has("user.name") && ! activeServer.hasTemporary("user.password")) {
                    String userPassword = JOptionPane.showInputDialog(crawlerSettingsPanel,
                        i18n.translate("serverSettingsUserPassword") + " (" + i18n.translate("serverSettingsUserPasswordNote") + ")",
                        i18n.translate("serverSettingsTitle"), JOptionPane.PLAIN_MESSAGE);

                    if (userPassword == null) {
                        break;
                    }

                    activeServer.putTemporary("user.password", userPassword);
                }

                crawlerManager.getCrawler().start(activeServer);
                break;
            case "stop":
                crawlerManager.getCrawler().stop();
                break;
            case "pause":
                crawlerManager.getCrawler().pause();
                break;
            case "resume":
                crawlerManager.getCrawler().resume();
                break;
        }
    }

    @Override
    public void crawlerStarted(CrawlerEvent e)
    {
        setCrawlerDependendComponentsEnabled(false);
        setCrawlerUpdate();
    }

    @Override
    public void crawlerPaused(CrawlerEvent e)
    {
        setCrawlerUpdate();
    }

    @Override
    public void crawlerResumed(CrawlerEvent e)
    {
        setCrawlerUpdate();
    }

    @Override
    public void crawlerStopped(CrawlerEvent e)
    {
        setCrawlerDependendComponentsEnabled(true);
        setCrawlerUpdate();
    }

    private void setCrawlerUpdate()
    {
        crawlerSettingsPanel.updateButtons(crawlerManager.getCrawler());

        crawlerResultsPanel.setStatus(crawlerManager.getCrawler().getStatus());
    }

    private void setCrawlerDependendComponentsEnabled(boolean enabled)
    {
        serverListPanel.setEnabled(enabled);
        serverListPanel.getAddServerButton().setEnabled(enabled);

        for (ServerListItem s : serverListPanel.getServerListItems()) {
            s.getEditButton().setEnabled(enabled);
            s.getDeleteButton().setEnabled(enabled);
        }

        crawlerSettingsPanel.getFileType().setEnabled(enabled);

        if (crawlerSettingsPanel.getFileName().isEnabled()) {
            crawlerSettingsPanel.getFileName().setEnabled(enabled);
        }

        crawlerSettingsPanel.getFileSizeMin().setEnabled(enabled);
        crawlerSettingsPanel.getFileSizeMax().setEnabled(enabled);
        crawlerSettingsPanel.getFileDateStart().setEnabled(enabled);
        crawlerSettingsPanel.getFileDateEnd().setEnabled(enabled);
    }

}