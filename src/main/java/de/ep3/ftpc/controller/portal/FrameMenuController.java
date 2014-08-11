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
import de.ep3.ftpc.model.ConfigurationManager;
import de.ep3.ftpc.model.ServerListManager;
import de.ep3.ftpc.view.portal.PortalFrame;
import de.ep3.ftpc.view.portal.PortalMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class FrameMenuController extends AbstractController implements ActionListener
{

    private PortalFrame portalFrame;
    private PortalMenu portalMenu;
    private ConfigurationManager configManager;
    private ServerListManager serverListManager;

    public FrameMenuController(PortalFrame portalFrame, ConfigurationManager configManager, ServerListManager serverListManager)
    {
        this.portalFrame = portalFrame;
        this.portalMenu = portalFrame.getPortalMenu();
        this.configManager = configManager;
        this.serverListManager = serverListManager;
    }

    @Override
    public void dispatch()
    {
        portalMenu.getFilePrint().addActionListener(this);
        portalMenu.getFileExit().addActionListener(this);
        portalMenu.getEditSettings().addActionListener(this);
        portalMenu.getServersCreate().addActionListener(this);
        portalMenu.getServersClear().addActionListener(this);
        portalMenu.getViewScroll().addActionListener(this);
        portalMenu.getHelpAbout().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JMenuItem menuItem = (JMenuItem) e.getSource();

        switch (menuItem.getName()) {
            case "filePrint":
                PrinterJob pj = PrinterJob.getPrinterJob();

                if (pj.printDialog()) {
                    try {
                        Printable printer = App.getContext().getBean("crawlerPrinter", Printable.class);

                        pj.setPrintable(printer);
                        pj.print();
                    } catch (PrinterException pe) {
                        JOptionPane.showMessageDialog(portalFrame, i18n.translate("errorPrinting", pe.getMessage()),
                            null, JOptionPane.ERROR_MESSAGE);
                    }
                }
                break;
            case "fileExit":
                App.exit();
                break;
            case "editSettings":
                {
                    Controller c = App.getContext().getBean("settingsController", Controller.class);

                    c.dispatch();
                }
                break;
            case "serversCreate":
                {
                    Controller c = App.getContext().getBean("portalServerSettingsController", Controller.class);

                    c.dispatch();
                }
                break;
            case "serversClear":
                int confirmation = JOptionPane.showConfirmDialog(portalFrame, i18n.translate("questionDeleteAllServers"));

                if (confirmation == 0) {
                    serverListManager.getServerList().clear();
                }
                break;
            case "viewScroll":
                JCheckBoxMenuItem viewScrollItem = (JCheckBoxMenuItem) menuItem;

                if (viewScrollItem.getState()) {
                    configManager.getConfig().put("app.view.scroll", "true");
                } else {
                    configManager.getConfig().put("app.view.scroll", "false");
                }

                configManager.saveConfig();
                break;
            case "helpAbout":
                {
                    Controller c = App.getContext().getBean("aboutController", Controller.class);

                    c.dispatch();
                }
                break;
        }
    }

}