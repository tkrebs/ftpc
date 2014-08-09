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

import de.ep3.ftpc.view.component.TextAreaLabel;
import de.ep3.ftpc.view.component.UntabbedTextArea;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Provides some convenient methods for extending classes to build simple default settings panels.
 *
 * Not the smartest way, but it does the job! :P
 */
public abstract class AbstractSettingsPanel extends JPanel
{

    protected String defaultLayout = "wrap 4";
    protected String defaultColConstraints = "[125px::][150px::]unrelated[][50px::]";
    protected String defaultRowConstraints = "";
    protected String defaultComponentConstraints = "grow, span 3";

    protected void setSettingsLayout()
    {
        setSettingsLayout(defaultLayout, defaultColConstraints, defaultRowConstraints);
    }

    protected void setSettingsLayout(String layout)
    {
        setSettingsLayout(layout, "", "");
    }

    protected void setSettingsLayout(String layout, String colConstraints)
    {
        setSettingsLayout(layout, colConstraints, "");
    }

    protected void setSettingsLayout(String layout, String colConstraints, String rowConstraints)
    {
        setLayout(new MigLayout(layout, colConstraints, rowConstraints));
    }

    protected JTextField addTextFieldSetting(String label)
    {
        return addTextFieldSetting(label, defaultComponentConstraints);
    }

    protected JTextField addTextFieldSetting(String label, String constraints)
    {
        add(new JLabel(label));

        JTextField textField = new JTextField();

        add(textField, constraints);

        return textField;
    }

    protected JPasswordField addPasswordFieldSetting(String label)
    {
        return addPasswordFieldSetting(label, defaultComponentConstraints);
    }

    protected JPasswordField addPasswordFieldSetting(String label, String constraints)
    {
        add(new JLabel(label));

        JPasswordField passwordField = new JPasswordField();

        add(passwordField, constraints);

        return passwordField;
    }

    protected JComboBox<String> addComboBox(String label, String[] options)
    {
        return addComboBox(label, options, defaultComponentConstraints);
    }

    protected JComboBox<String> addComboBox(String label, String[] options, String constraints)
    {
        add(new JLabel(label));

        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setBorder(null);

        add(comboBox, constraints);

        return comboBox;
    }

    protected JTextArea addTextAreaSetting(String label)
    {
        return addTextAreaSettings(label, defaultComponentConstraints);
    }

    protected JTextArea addTextAreaSettings(String label, String constraints)
    {
        add(new JLabel(label), "align left top");

        JTextArea textArea = new UntabbedTextArea();
        textArea.setRows(5);

        JScrollPane textAreaPane = new JScrollPane(textArea);

        add(textAreaPane, constraints);

        return textArea;
    }

    protected JTextField[] addRangeSetting(String label)
    {
        return addRangeSetting(label, null, defaultComponentConstraints);
    }

    protected JTextField[] addRangeSetting(String label, String unit)
    {
        return addRangeSetting(label, unit, defaultComponentConstraints);
    }

    protected JTextField[] addRangeSetting(String label, String unit, String constraints)
    {
        JTextField[] range = {
            new JTextField(),
            new JTextField(),
        };

        add(new JLabel(label));

        JPanel rangePanel = new JPanel();
        String rangePanelColConstraints = "[grow, sg input][min!][grow, sg input]";

        if (unit != null) {
            rangePanelColConstraints = "[grow, sg input][min!][min!][grow, sg input][min!]";
        }

        rangePanel.setLayout(new MigLayout("fill, insets 0", rangePanelColConstraints));

        rangePanel.add(range[0], "grow");

        if (unit != null) {
            rangePanel.add(new JLabel(unit));
        }

        rangePanel.add(new JLabel("-"));
        rangePanel.add(range[1], "grow");

        if (unit != null) {
            rangePanel.add(new JLabel(unit));
        }

        add(rangePanel, constraints);

        return range;
    }

    protected TextAreaLabel addHint(String hint)
    {
        addPlaceholder();

        TextAreaLabel hintLabel = new TextAreaLabel(hint);
        hintLabel.setForeground(Color.DARK_GRAY);

        add(hintLabel, defaultComponentConstraints);

        return hintLabel;
    }

    protected void addLabel(String text)
    {
        add(new JLabel(text), "span");
    }

    protected void addSeparator()
    {
        add(new JSeparator(), "grow, span, gaptop 8px, gapbottom 8px");
    }

    protected void addPlaceholder()
    {
        addPlaceholder("");
    }

    protected void addPlaceholder(String componentConstraints)
    {
        JPanel placeholder = new JPanel();
        placeholder.setOpaque(false);

        add(placeholder, componentConstraints);
    }

}