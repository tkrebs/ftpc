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
import de.ep3.ftpc.model.StatusManager;
import de.ep3.ftpc.view.portal.PortalFrame;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Essentially bootstraps the other controllers by requesting them from the DIC.
 *
 * And exits the app.
 */
public class FrameController extends AbstractController implements WindowListener
{

    private PortalFrame portalFrame;
    private StatusManager statusManager;

    private Controller portalFrameMenuController;
    private Controller portalServerListController;
    private Controller portalCrawlerSettingsController;
    private Controller portalCrawlerResultsController;
    private Controller portalCrawlerController;

    public FrameController(PortalFrame portalFrame, StatusManager statusManager)
    {
        this.portalFrame = portalFrame;
        this.statusManager = statusManager;

        portalFrameMenuController = App.getContext().getBean("portalFrameMenuController", Controller.class);
        portalServerListController = App.getContext().getBean("portalServerListController", Controller.class);
        portalCrawlerSettingsController = App.getContext().getBean("portalCrawlerSettingsController", Controller.class);
        portalCrawlerResultsController = App.getContext().getBean("portalCrawlerResultsController", Controller.class);
        portalCrawlerController = App.getContext().getBean("portalCrawlerController", Controller.class);
    }

    @Override
    public void dispatch()
    {
        portalFrame.addWindowListener(this);

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                portalFrame.pack();
                portalFrame.setLocationRelativeTo(null);
                portalFrame.setVisible(true);
            }
        });

        portalFrameMenuController.dispatch();
        portalServerListController.dispatch();
        portalCrawlerSettingsController.dispatch();
        portalCrawlerResultsController.dispatch();
        portalCrawlerController.dispatch();
    }

    @Override
    public void windowOpened(WindowEvent e)
    { }

    @Override
    public void windowClosing(WindowEvent e)
    {
        App.exit();
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

}