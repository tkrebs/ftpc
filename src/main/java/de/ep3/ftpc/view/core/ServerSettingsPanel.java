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

import de.ep3.ftpc.model.i18n.I18nManager;

import javax.swing.*;

public class ServerSettingsPanel extends AbstractSettingsPanel
{

    private JTextField serverName;
    private JTextField serverIP;
    private JTextField serverPort;

    private JTextField userName;
    private JTextField userPassword;

    private JTextArea includePaths;
    private JTextArea excludePaths;

    private JButton buttonSave;
    private JButton buttonCancel;

    public ServerSettingsPanel(I18nManager i18n)
    {
        setSettingsLayout();

        /* Add form fields */

        serverName = addTextFieldSetting(i18n.translate("serverSettingsServerName"));

        serverIP = addTextFieldSetting(i18n.translate("serverSettingsServerIP"), "grow");
        serverPort = addTextFieldSetting(i18n.translate("serverSettingsServerPort"), "grow");

        setServerPort("21");

        addSeparator();

        userName = addTextFieldSetting(i18n.translate("serverSettingsUserName"));
        userPassword = addPasswordFieldSetting(i18n.translate("serverSettingsUserPassword"));
        addHint(i18n.translate("serverSettingsUserPasswordNote"));

        addSeparator();

        includePaths = addTextAreaSetting(i18n.translate("serverSettingsIncludePaths"));

        addHint(i18n.translate("serverSettingsIncludePathsHint"));

        excludePaths = addTextAreaSetting(i18n.translate("serverSettingsExcludePaths"));

        addHint(i18n.translate("serverSettingsExcludePathsHint"));

        /* Add form buttons */

        addSeparator();

        addPlaceholder();

        buttonSave = new JButton(i18n.translate("buttonSave"));
        buttonSave.setName("saveButton");
        buttonCancel = new JButton(i18n.translate("buttonCancel"));
        buttonCancel.setName("cancelButton");

        add(buttonSave, "align right, span 3, split 3");
        add(buttonCancel);
    }

    public String getServerName()
    {
        return serverName.getText().trim();
    }

    public void setServerName(String serverNameText)
    {
        serverName.setText(serverNameText);
    }

    public String getServerIP()
    {
        return serverIP.getText().trim();
    }

    public void setServerIP(String serverIPText)
    {
        serverIP.setText(serverIPText);
    }

    public String getServerPort()
    {
        return serverPort.getText().trim();
    }

    public void setServerPort(String serverPortText)
    {
        serverPort.setText(serverPortText);
    }

    public String getUserName()
    {
        return userName.getText().trim();
    }

    public void setUserName(String userNameText)
    {
        userName.setText(userNameText);
    }

    public String getUserPassword()
    {
        return userPassword.getText().trim();
    }

    public void setUserPassword(String userPasswordText)
    {
        userPassword.setText(userPasswordText);
    }

    public String getIncludePaths()
    {
        return includePaths.getText().trim();
    }

    public void setIncludePaths(String includePathsText)
    {
        includePaths.setText(includePathsText);
    }

    public String getExcludePaths()
    {
        return excludePaths.getText().trim();
    }

    public void setExcludePaths(String excludePathsText)
    {
        excludePaths.setText(excludePathsText);
    }

    public JButton getButtonSave()
    {
        return buttonSave;
    }

    public JButton getButtonCancel()
    {
        return buttonCancel;
    }

}