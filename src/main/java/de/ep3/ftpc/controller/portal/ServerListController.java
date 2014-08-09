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

import de.ep3.ftpc.App;
import de.ep3.ftpc.controller.AbstractController;
import de.ep3.ftpc.controller.Controller;
import de.ep3.ftpc.model.*;
import de.ep3.ftpc.view.component.TextFieldLabel;
import de.ep3.ftpc.view.core.ServerListItem;
import de.ep3.ftpc.view.core.ServerListPanel;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;

/**
 * Manages the process of adding, editing and deleting servers from the server list.
 */
public class ServerListController extends AbstractController implements ActionListener, MouseListener, ServerListListener
{

    private ServerListPanel serverListPanel;
    private ServerListManager serverListManager;

    public ServerListController(ServerListPanel serverListPanel, ServerListManager serverListManager)
    {
        this.serverListPanel = serverListPanel;
        this.serverListManager = serverListManager;
    }

    @Override
    public void dispatch()
    {
        registerServerListListeners();

        serverListManager.getServerList().addListener(this);
    }

    private void registerServerListListeners()
    {
        Vector<ServerListItem> serverListItems = serverListPanel.getServerListItems();

        for (ServerListItem sli : serverListItems) {
            sli.addMouseListener(this);

            sli.getServerName().addMouseListener(this);
            sli.getServerIP().addMouseListener(this);

            JButton editButton = sli.getEditButton();
            JButton deleteButton = sli.getDeleteButton();

            editButton.addActionListener(this);
            deleteButton.addActionListener(this);
        }

        JPanel placeholder = serverListPanel.getPlaceholder();

        if (placeholder != null) {
            placeholder.addMouseListener(this);
        }

        JButton noServerButton = serverListPanel.getNoServerButton();
        JButton addServerButton = serverListPanel.getAddServerButton();

        if (noServerButton != null) {
            noServerButton.addActionListener(this);
        }

        if (addServerButton != null) {
            addServerButton.addActionListener(this);
        }
    }

    /**
     * Click on a button within the server list panel.
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JButton button = (JButton) e.getSource();

        switch (button.getName()) {
            case "addServer":
                {
                    Controller c = App.getContext().getBean("portalServerSettingsController", Controller.class);

                    c.dispatch();
                }
                break;
            case "editServer":
                {
                    ServerListItem serverListItem = (ServerListItem) button.getParent();
                    Server server = serverListItem.getServer();

                    ServerSettingsController c = (ServerSettingsController) App.getContext().getBean("portalServerSettingsController", Controller.class);

                    c.setServer(server);
                    c.dispatch();
                }
                break;
            case "deleteServer":
                {
                    ServerListItem serverListItem = (ServerListItem) button.getParent();
                    Server server = serverListItem.getServer();

                    int confirmation = JOptionPane.showConfirmDialog(serverListPanel, i18n.translate("questionDeleteServer"));

                    if (confirmation == 0) {
                        serverListManager.getServerList().remove(server);

                        if (server.hasTemporary("active") && server.getTemporary("active").equals("true")) {
                            serverListManager.getServerList().setActiveServer(null);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void serverListUpdated(ServerListEvent e)
    {
        ServerList serverList = (ServerList) e.getSource();

        /* Update server list panel */

        serverListPanel.initialize(serverList);

        registerServerListListeners();

        /* Save servers to file */

        File serverListFile = serverListManager.saveServerList();

        status.add(i18n.translate("serverSettingsSaved") + " " + serverListFile.toString());
    }

    @Override
    public void serverActivated(ServerListEvent e)
    {
        serverListPanel.updateListState();
    }

    /**
     * Click on a server list item (or one of its labels) to activate it.
     * Click on the placeholder panel (or actually anything else) to deactivate it.
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (serverListPanel.isEnabled()) {
            Server server = null;

            Object source = e.getSource();

            if (source instanceof ServerListItem) {
                ServerListItem serverListItem = (ServerListItem) source;
                server = serverListItem.getServer();
            } else if (source instanceof TextFieldLabel) {
                ServerListItem serverListItem = (ServerListItem) ((TextFieldLabel) source).getParent();
                server = serverListItem.getServer();
            }

            serverListManager.getServerList().setActiveServer(server);
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

}