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
import de.ep3.ftpc.view.designer.UIDesigner;

import javax.swing.*;

public class ServerSettingsDialog extends JDialog
{

    private ServerSettingsPanel serverSettingsPanel;

    public ServerSettingsDialog(JFrame parentFrame, ServerSettingsPanel serverSettingsPanel,
        I18nManager i18n, UIDesigner uiDesigner)
    {
        super(parentFrame);

        setTitle(i18n.translate("serverSettingsTitle"));
        setIconImages(uiDesigner.getDefaultAppIcons());

        setModalityType(ModalityType.APPLICATION_MODAL);

        setResizable(false);

        add(serverSettingsPanel);

        getRootPane().setDefaultButton(serverSettingsPanel.getButtonSave());

        this.serverSettingsPanel = serverSettingsPanel;
    }

    public ServerSettingsPanel getServerSettingsPanel()
    {
        return serverSettingsPanel;
    }

}