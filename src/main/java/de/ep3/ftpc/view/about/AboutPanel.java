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

package de.ep3.ftpc.view.about;

import de.ep3.ftpc.model.Configuration;
import de.ep3.ftpc.model.i18n.I18nManager;
import de.ep3.ftpc.view.component.TextButton;
import de.ep3.ftpc.view.designer.UIDesigner;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

public class AboutPanel extends JPanel implements ActionListener
{

    private Configuration config;

    public AboutPanel(I18nManager i18n, UIDesigner uiDesigner, Configuration config)
    {
        setLayout(new MigLayout("inset 32px"));

        JLabel logo = new JLabel(new ImageIcon(uiDesigner.getDefaultImage("logo", "256.png")));
        JPanel about = new JPanel();

        add(logo, "align left top");
        add(about, "align left top");

        /* About panel */

        about.setLayout(new MigLayout("flowy, gap unrelated", "", "[][][]0px[][][]"));

        about.add(new JLabel(config.get("app.name")));

        about.add(new JLabel(i18n.translate("appAboutVersion") + " " + config.get("app.version")));

        JButton homepageButton = new TextButton(uiDesigner);
        homepageButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        homepageButton.setText(config.get("app.author.homepage"));
        homepageButton.addActionListener(this);

        about.add(homepageButton, "align left");

        JButton githubButton = new TextButton(uiDesigner);
        githubButton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        githubButton.setText(config.get("app.source.homepage"));
        githubButton.addActionListener(this);

        about.add(githubButton, "align left");

        about.add(new JLabel(i18n.translate("appLicense", config.get("app.license"))));

        about.add(new JLabel("\u00A9" + " " + config.get("app.version.year") + " " + config.get("app.author.name")));

        this.config = config;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String homepage = ((JButton) e.getSource()).getText();

        if (Desktop.isDesktopSupported()) {
            Desktop d = Desktop.getDesktop();

            try {
                d.browse(URI.create(homepage));
            } catch (IOException ex) { }
        }
    }

}