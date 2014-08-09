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

package de.ep3.ftpc.view.settings;

import de.ep3.ftpc.model.Configuration;
import de.ep3.ftpc.model.i18n.I18nManager;
import de.ep3.ftpc.view.core.AbstractSettingsPanel;

import javax.swing.*;

public class SettingsPanel extends AbstractSettingsPanel
{

    private String[] localeKeys;
    private String[] localeValues;

    private JComboBox<String> locale;

    private JButton buttonSave;
    private JButton buttonCancel;

    public SettingsPanel(Configuration config, I18nManager i18n)
    {
        setSettingsLayout();

        /* Add form fields */

        localeKeys = new String[]{ "en-US", "de-DE" };
        localeValues = new String[]{ "English", "Deutsch" };

        locale = addComboBox(i18n.translate("appSettingsLocale"), localeValues);

        for (int i = 0; i < localeKeys.length; i++) {
            if (config.need("app.locale").equals(localeKeys[i])) {
                locale.setSelectedIndex(i);
            }
        }

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

    public String[] getLocaleKeys()
    {
        return localeKeys;
    }

    public int getLocaleKey()
    {
        return locale.getSelectedIndex();
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