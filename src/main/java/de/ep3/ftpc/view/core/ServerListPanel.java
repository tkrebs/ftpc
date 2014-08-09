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

package de.ep3.ftpc.view.core;

import de.ep3.ftpc.model.Server;
import de.ep3.ftpc.model.ServerList;
import de.ep3.ftpc.model.i18n.I18nManager;
import de.ep3.ftpc.view.component.FlatButton;
import de.ep3.ftpc.view.component.TextButton;
import de.ep3.ftpc.view.designer.UIDesigner;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.Vector;

/**
 * This panel contains the single server items (panels).
 */
public class ServerListPanel extends JPanel
{

    private I18nManager i18n;
    private UIDesigner uiDesigner;

    private Vector<ServerListItem> serverListItems;

    private JPanel placeholder;

    private JButton noServerButton;
    private JButton addServerButton;

    public ServerListPanel(ServerList serverList, I18nManager i18n, UIDesigner uiDesigner)
    {
        setBorder(uiDesigner.getDefaultBorder());

        this.i18n = i18n;
        this.uiDesigner = uiDesigner;

        initialize(serverList);
    }

    /**
     * Initializes the server list. Indended to be called when the server list changes.
     *
     * @param serverList The current server list to render.
     */
    public void initialize(ServerList serverList)
    {
        removeAll();

        serverListItems = new Vector<>();

        StringBuilder rowConstraints = new StringBuilder();

        for (Server server : serverList) {
            rowConstraints.append("[min!]");
        }

        rowConstraints.append("[grow][min!]");

        setLayout(new MigLayout("fill, flowy", "", rowConstraints.toString()));

        for (Server server : serverList) {
            ServerListItem serverListItem = new ServerListItem(server, uiDesigner);
            serverListItems.add(serverListItem);

            add(serverListItem, "grow");
        }

        placeholder = new JPanel();

        if (serverList.size() == 0) {
            FlatButton layoutCorrector = new FlatButton(uiDesigner);
            layoutCorrector.setText("-");
            layoutCorrector.setVisible(false);

            JLabel noServerLabel = new JLabel(i18n.translate("portalNoServers"));

            noServerButton = new TextButton(uiDesigner);
            noServerButton.setAlignmentX(CENTER_ALIGNMENT);
            noServerButton.setText(i18n.translate("portalCreateServer"));
            noServerButton.setName("addServer");

            placeholder.setLayout(new MigLayout("fill, flowy", "", "[min!][grow][grow]"));

            placeholder.add(layoutCorrector, "grow");
            placeholder.add(noServerLabel, "align center bottom");
            placeholder.add(noServerButton, "align center top");
        } else {
            noServerButton = null;
        }

        add(placeholder, "grow");

        addServerButton = new FlatButton(uiDesigner);
        addServerButton.setText("+");
        addServerButton.setName("addServer");

        add(addServerButton, "grow");

        updateListState();

        revalidate();
    }

    /**
     * Convenience method to update the list state of each server list item
     * (i.e. whether currently active or not).
     */
    public void updateListState()
    {
        for (ServerListItem sli : serverListItems) {
            sli.updateState();
        }
    }

    public Vector<ServerListItem> getServerListItems()
    {
        return serverListItems;
    }

    public JPanel getPlaceholder()
    {
        return placeholder;
    }

    public JButton getNoServerButton()
    {
        return noServerButton;
    }

    public JButton getAddServerButton()
    {
        return addServerButton;
    }

}