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
import de.ep3.ftpc.model.Server;
import de.ep3.ftpc.model.ServerList;
import de.ep3.ftpc.view.core.ServerSettingsDialog;
import de.ep3.ftpc.view.core.ServerSettingsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ServerSettingsController extends AbstractController implements WindowListener, ActionListener
{

    private JFrame parentFrame;
    private ServerSettingsDialog serverSettingsDialog;
    private ServerSettingsPanel serverSettingsPanel;
    private ServerList serverList;

    private Server server;

    public ServerSettingsController(JFrame parentFrame, ServerSettingsDialog serverSettingsDialog, ServerList serverList)
    {
        this.parentFrame = parentFrame;
        this.serverSettingsDialog = serverSettingsDialog;
        this.serverSettingsPanel = serverSettingsDialog.getServerSettingsPanel();
        this.serverList = serverList;
    }

    public void setServer(Server server)
    {
        this.server = server;
    }

    @Override
    public void dispatch()
    {
        serverSettingsDialog.addWindowListener(this);

        serverSettingsPanel.getButtonSave().addActionListener(this);
        serverSettingsPanel.getButtonCancel().addActionListener(this);

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                if (server != null) {
                    serverSettingsPanel.setServerName(server.need("server.name"));
                    serverSettingsPanel.setServerIP(server.need("server.ip"));
                    serverSettingsPanel.setServerPort(server.need("server.port"));

                    serverSettingsPanel.setUserName(server.get("user.name"));
                    serverSettingsPanel.setUserPassword(server.getTemporary("user.password"));

                    serverSettingsPanel.setIncludePaths(server.get("include-paths"));
                    serverSettingsPanel.setExcludePaths(server.get("exclude-paths"));
                }

                serverSettingsDialog.pack();
                serverSettingsDialog.setLocationRelativeTo(parentFrame);
                serverSettingsDialog.setVisible(true);
            }
        });
    }

    @Override
    public void windowOpened(WindowEvent e)
    { }

    @Override
    public void windowClosing(WindowEvent e)
    {
        closeDialog();
    }

    @Override
    public void windowClosed(WindowEvent e)
    { }

    @Override
    public void windowIconified(WindowEvent e)
    { }

    @Override
    public void windowDeiconified(WindowEvent e)
    { }

    @Override
    public void windowActivated(WindowEvent e)
    { }

    @Override
    public void windowDeactivated(WindowEvent e)
    { }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JButton button = (JButton) e.getSource();

        switch (button.getName()) {
            case "saveButton":
                String serverName = serverSettingsPanel.getServerName();
                String serverIP = serverSettingsPanel.getServerIP();
                String serverPort = serverSettingsPanel.getServerPort();

                String userName = serverSettingsPanel.getUserName();
                String userPassword = serverSettingsPanel.getUserPassword();

                String includePaths = serverSettingsPanel.getIncludePaths();
                String excludePaths = serverSettingsPanel.getExcludePaths();

                try {
                    serverName = serverName.replaceAll("(<.*?>)", "");

                    if (serverName.length() < 3) {
                        throw new IllegalStateException(
                            i18n.translate("serverSettingsServerName") + ":   " + i18n.translate("errorInputTooShort"));
                    }

                    serverIP = serverIP.replaceAll("(<.*?>)", "");

                    if (serverIP.length() < 3) {
                        throw new IllegalStateException(
                            i18n.translate("serverSettingsServerIP") + ":   " + i18n.translate("errorInputTooShort"));
                    }

                    if (! serverPort.matches("^[1-9][0-9]*$")) {
                        throw new IllegalStateException(
                            i18n.translate("serverSettingsServerPort") + ":   " + i18n.translate("errorInputNoInteger"));
                    }

                    for (String pathLine : includePaths.split("\n")) {
                        pathLine = pathLine.trim();

                        if (pathLine.length() > 0 && pathLine.charAt(0) != '/') {
                            throw new IllegalStateException(
                                i18n.translate("serverSettingsIncludePaths") + ":   " + i18n.translate("errorInputNoPaths"));
                        }
                    }

                    for (String pathLine : excludePaths.split("\n")) {
                        pathLine = pathLine.trim();

                        if (pathLine.length() > 0 && pathLine.charAt(0) != '/') {
                            throw new IllegalStateException(
                                i18n.translate("serverSettingsExcludePaths") + ":   " + i18n.translate("errorInputNoPaths"));
                        }
                    }
                } catch (IllegalStateException ex) {
                    JOptionPane.showMessageDialog(serverSettingsDialog, ex.getMessage(), null, JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (server == null) {
                    server = new Server();
                }

                server.put("server.name", serverName);
                server.put("server.ip", serverIP);
                server.put("server.port", serverPort);

                server.put("user.name", userName);
                server.putTemporary("user.password", userPassword);

                server.put("include-paths", includePaths);
                server.put("exclude-paths", excludePaths);

                serverList.save(server);
                break;
        }

        closeDialog();
    }

    private void closeDialog()
    {
        serverSettingsDialog.dispose();
    }

}