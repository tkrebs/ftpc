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

package de.ep3.ftpc.controller;

import de.ep3.ftpc.view.about.AboutDialog;
import de.ep3.ftpc.view.about.AboutPanel;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AboutController extends AbstractController implements WindowListener
{

    private JFrame parentFrame;
    private AboutDialog aboutDialog;
    private AboutPanel aboutPanel;

    public AboutController(JFrame parentFrame, AboutDialog aboutDialog)
    {
        this.parentFrame = parentFrame;
        this.aboutDialog = aboutDialog;
        this.aboutPanel = aboutDialog.getAboutPanel();
    }

    @Override
    public void dispatch()
    {
        aboutDialog.addWindowListener(this);

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                aboutDialog.pack();
                aboutDialog.setLocationRelativeTo(parentFrame);
                aboutDialog.setVisible(true);
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

    private void closeDialog()
    {
        aboutDialog.dispose();
    }

}