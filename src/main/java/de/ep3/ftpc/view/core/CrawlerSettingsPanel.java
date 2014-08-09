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

import de.ep3.ftpc.model.Crawler;
import de.ep3.ftpc.model.Server;
import de.ep3.ftpc.model.ServerList;
import de.ep3.ftpc.model.i18n.I18nManager;
import de.ep3.ftpc.view.component.FlatButton;
import de.ep3.ftpc.view.component.TextButton;
import de.ep3.ftpc.view.designer.UIDesigner;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

/**
 * This is the panel where the server crawler settings can be setup.
 *
 * Crawler settings are stored in the server class and will thus be saved on application shutdown.
 */
public class CrawlerSettingsPanel extends AbstractSettingsPanel
{

    private I18nManager i18n;
    private UIDesigner uiDesigner;

    private JComboBox<String> fileType;
    private JTextField fileName;
    private JTextField[] fileSize;
    private JTextField[] fileDate;

    private JPanel buttonPanel;

    private JButton startButton;
    private JButton stopButton;
    private JButton pauseButton;
    private JButton resumeButton;

    public CrawlerSettingsPanel(ServerList serverList, I18nManager i18n, UIDesigner uiDesigner)
    {
        setBorder(uiDesigner.getDefaultBorder());

        this.i18n = i18n;
        this.uiDesigner = uiDesigner;

        initializeButtons();
        initialize(serverList.getActiveServer());
    }

    /**
     * Initializes the buttons (references) so that the controller can add itself as a permanent listener.
     */
    public void initializeButtons()
    {
        buttonPanel = new JPanel(new MigLayout("fill, insets 0", "[grow][min!][30%!]"));

        startButton = new FlatButton(uiDesigner);
        startButton.setText(i18n.translate("portalCrawlerStart"));
        startButton.setName("start");

        stopButton = new FlatButton(uiDesigner);
        stopButton.setText(i18n.translate("portalCrawlerStop"));
        stopButton.setName("stop");

        pauseButton = new TextButton(uiDesigner);
        pauseButton.setText(i18n.translate("portalCrawlerPause"));
        pauseButton.setName("pause");

        resumeButton = new TextButton(uiDesigner);
        resumeButton.setText(i18n.translate("portalCrawlerResume"));
        resumeButton.setName("resume");
    }

    /**
     * Initializes the actual panel elements. Intended to be called when the active server changes.
     *
     * @param server The server for which the panel should render itself. Can be null for no server selection.
     */
    public void initialize(Server server)
    {
        removeAll();

        fileType = null;
        fileName = null;
        fileSize = null;
        fileDate = null;

        if (server == null) {
            setSettingsLayout("fill");

            JLabel noServerLabel = new JLabel(i18n.translate("portalNoServerSelected"));

            add(noServerLabel, "align center");
        } else {
            defaultLayout = "fill, wrap 2";
            defaultColConstraints = "";
            defaultRowConstraints = "[min!][min!][min!][min!][min!][min!][grow][min!]";
            defaultComponentConstraints = "grow";

            setSettingsLayout();

            /* Add form fields */

            addLabel(i18n.translate("portalCrawlerSettings") + ":");

            String[] fileTypes = {
                i18n.translate("imageFileAll"),
                i18n.translate("imageFileCustom"),
                i18n.translate("customFileName"),
            };

            fileType = addComboBox(i18n.translate("portalCrawlerFileType"), fileTypes);

            if (server.get("crawler.file-type") != null) {
                switch (server.get("crawler.file-type")) {
                    case "image-all":
                        fileType.setSelectedIndex(0);
                        break;
                    case "image-custom":
                        fileType.setSelectedIndex(1);
                        break;
                    case "file-custom":
                        fileType.setSelectedIndex(2);
                        break;
                }
            }

            fileName = addTextFieldSetting(i18n.translate("portalCrawlerFileName"));
            fileName.setText(server.get("crawler.file-name"));

            addHint(i18n.translate("portalCrawlerFileNameHint"));

            fileSize = addRangeSetting(i18n.translate("portalCrawlerFileSize"), "kB");
            fileSize[0].setText(server.get("crawler.file-size-min"));
            fileSize[1].setText(server.get("crawler.file-size-max"));

            fileDate = addRangeSetting(i18n.translate("portalCrawlerFileDate"));
            fileDate[0].setText(server.get("crawler.file-date-start"));
            fileDate[1].setText(server.get("crawler.file-date-end"));

            addHint(i18n.translate("portalCrawlerFileDateHint"));

            /* Add form buttons */

            addPlaceholder("grow, span");

            add(buttonPanel, "grow, span");

            updateButtons(null);
        }

        revalidate();
        repaint();
    }

    /**
     * Updates the visible buttons depending on the crawler state.
     *
     * @param crawler The crawler instance form the DIC or null, if none.
     */
    public void updateButtons(Crawler crawler)
    {
        Crawler.Status crawlerStatus;

        if (crawler == null) {
            crawlerStatus = Crawler.Status.IDLE;
        } else {
            crawlerStatus = crawler.getStatus();
        }

        buttonPanel.removeAll();

        switch (crawlerStatus) {
            case RUNNING:
                buttonPanel.add(stopButton, "grow");
                buttonPanel.add(new JLabel(i18n.translate("or")));
                buttonPanel.add(pauseButton);
                break;
            case PAUSED:
                buttonPanel.add(stopButton, "grow");
                buttonPanel.add(new JLabel(i18n.translate("or")));
                buttonPanel.add(resumeButton);
                break;
            case IDLE:
            default:
                buttonPanel.add(startButton, "grow, span 3");
        }

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    public JComboBox<String> getFileType()
    {
        return fileType;
    }

    public JTextField getFileName()
    {
        return fileName;
    }

    public JTextField getFileSizeMin()
    {
        return fileSize[0];
    }

    public JTextField getFileSizeMax()
    {
        return fileSize[1];
    }

    public JTextField getFileDateStart()
    {
        return fileDate[0];
    }

    public JTextField getFileDateEnd()
    {
        return fileDate[1];
    }

    public JButton getStartButton()
    {
        return startButton;
    }

    public JButton getStopButton()
    {
        return stopButton;
    }

    public JButton getPauseButton()
    {
        return pauseButton;
    }

    public JButton getResumeButton()
    {
        return resumeButton;
    }

}