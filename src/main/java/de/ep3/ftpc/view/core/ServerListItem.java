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
import de.ep3.ftpc.view.component.IconButton;
import de.ep3.ftpc.view.component.TextFieldLabel;
import de.ep3.ftpc.view.designer.UIDesigner;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * A single server list item within the server list panel.
 */
public class ServerListItem extends JPanel
{

    private Server server;
    private TextFieldLabel serverName;
    private TextFieldLabel serverIP;
    private JButton editButton;
    private JButton deleteButton;

    public ServerListItem(Server server, UIDesigner uiDesigner)
    {
        setLayout(new MigLayout("fill, insets 0", "[grow]unrelated[min!][min!]", "[]0[]"));

        /* Create elements */

        serverName = new TextFieldLabel(server.need("server.name"));
        serverIP = new TextFieldLabel(server.need("server.ip"));
        serverIP.setForeground(uiDesigner.getDefaultBorderColor());

        editButton = new IconButton(uiDesigner.getDefaultIcon("edit.png"));
        editButton.setName("editServer");

        deleteButton = new IconButton(uiDesigner.getDefaultIcon("delete.png"));
        deleteButton.setName("deleteServer");

        /* Add elements to panel */

        add(serverName, "cell 0 0");
        add(serverIP, "cell 0 1");
        add(editButton, "cell 1 0, span 1 2, grow");
        add(deleteButton, "cell 2 0, span 1 2, grow");

        /* Prepare panel */

        setBackground(new Color(220, 220, 220));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);

        this.server = server;
    }

    /**
     * Updates the background color of this item depending on whether it's currently active or not.
     */
    public void updateState()
    {
        if (server.hasTemporary("active") && server.getTemporary("active").equals("true")) {
            setOpaque(true);
        } else {
            setOpaque(false);
        }

        repaint();
    }

    public Server getServer()
    {
        return server;
    }

    public TextFieldLabel getServerName()
    {
        return serverName;
    }

    public TextFieldLabel getServerIP()
    {
        return serverIP;
    }

    public JButton getEditButton()
    {
        return editButton;
    }

    public JButton getDeleteButton()
    {
        return deleteButton;
    }

}