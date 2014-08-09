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

package de.ep3.ftpc.view.portal;

import de.ep3.ftpc.model.Configuration;
import de.ep3.ftpc.model.i18n.I18nManager;

import javax.swing.*;

/**
 * Not seeing a reason to explain anything here ... :P
 */
public class PortalMenu extends JMenuBar
{

    private JMenuItem filePrint;
    private JMenuItem fileExit;

    private JMenuItem editSettings;

    private JMenuItem serversCreate;
    private JMenuItem serversClear;

    private JCheckBoxMenuItem viewScroll;

    private JMenuItem helpAbout;

    public PortalMenu(Configuration config, I18nManager i18n)
    {
        /* File */

        JMenu file = new JMenu(i18n.translate("menuFile"));

        filePrint = new JMenuItem(i18n.translate("menuFilePrint"));
        filePrint.setName("filePrint");

        fileExit = new JMenuItem(i18n.translate("menuFileExit"));
        fileExit.setName("fileExit");

        file.add(filePrint);
        file.addSeparator();
        file.add(fileExit);

        /* Edit */

        JMenu edit = new JMenu(i18n.translate("menuEdit"));

        editSettings = new JMenuItem(i18n.translate("menuEditSettings"));
        editSettings.setName("editSettings");

        edit.add(editSettings);

        /* Servers */

        JMenu servers = new JMenu(i18n.translate("menuServers"));

        serversCreate = new JMenuItem(i18n.translate("menuServersCreate"));
        serversCreate.setName("serversCreate");

        serversClear = new JMenuItem(i18n.translate("menuServersClear"));
        serversClear.setName("serversClear");

        servers.add(serversCreate);
        servers.addSeparator();
        servers.add(serversClear);

        /* View */

        JMenu view = new JMenu(i18n.translate("menuView"));

        viewScroll = new JCheckBoxMenuItem(i18n.translate("menuViewScroll"));
        viewScroll.setName("viewScroll");

        if (config.has("app.view.scroll") && config.get("app.view.scroll").equals("false")) {
            viewScroll.setState(false);
        } else {
            viewScroll.setState(true);
        }

        view.add(viewScroll);

        /* Help */

        JMenu help = new JMenu(i18n.translate("menuHelp"));

        helpAbout = new JMenuItem(i18n.translate("menuHelpAbout"));
        helpAbout.setName("helpAbout");

        help.add(helpAbout);

        /* Assemble ... */

        add(file);
        add(edit);
        add(servers);
        add(view);
        add(help);
    }

    public JMenuItem getFilePrint()
    {
        return filePrint;
    }

    public JMenuItem getFileExit()
    {
        return fileExit;
    }

    public JMenuItem getEditSettings()
    {
        return editSettings;
    }

    public JMenuItem getServersCreate()
    {
        return serversCreate;
    }

    public JMenuItem getServersClear()
    {
        return serversClear;
    }

    public JCheckBoxMenuItem getViewScroll()
    {
        return viewScroll;
    }

    public JMenuItem getHelpAbout()
    {
        return helpAbout;
    }

}