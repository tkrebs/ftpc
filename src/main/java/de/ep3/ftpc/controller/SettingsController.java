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

import de.ep3.ftpc.model.Configuration;
import de.ep3.ftpc.model.ConfigurationManager;
import de.ep3.ftpc.view.settings.SettingsDialog;
import de.ep3.ftpc.view.settings.SettingsPanel;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class SettingsController extends AbstractController implements WindowListener, ActionListener
{

    private JFrame parentFrame;
    private SettingsDialog settingsDialog;
    private SettingsPanel settingsPanel;
    private ConfigurationManager configManager;

    public SettingsController(JFrame parentFrame, SettingsDialog settingsDialog, ConfigurationManager configManager)
    {
        this.parentFrame = parentFrame;
        this.settingsDialog = settingsDialog;
        this.settingsPanel = settingsDialog.getSettingsPanel();
        this.configManager = configManager;
    }

    @Override
    public void dispatch()
    {
        settingsDialog.addWindowListener(this);

        settingsPanel.getButtonSave().addActionListener(this);
        settingsPanel.getButtonCancel().addActionListener(this);

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                settingsDialog.pack();
                settingsDialog.setLocationRelativeTo(parentFrame);
                settingsDialog.setVisible(true);
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
                boolean notifyRestart = false;

                Configuration config = configManager.getConfig();

                /* Locale */

                int localeSelection = settingsPanel.getLocaleKey();
                String locale = settingsPanel.getLocaleKeys()[localeSelection];

                if (! config.get("app.locale").equals(locale)) {
                    config.put("app.locale", locale);
                    notifyRestart = true;
                }

                /* Save and finish */

                File configFile = configManager.saveConfig();

                status.add(i18n.translate("appSettingsSaved") + " " + configFile.toString());

                if (notifyRestart) {
                    JOptionPane.showMessageDialog(settingsDialog, i18n.translate("notifyRestart"), null, JOptionPane.INFORMATION_MESSAGE);
                }

                break;
        }

        closeDialog();
    }

    private void closeDialog()
    {
        settingsDialog.dispose();
    }

}