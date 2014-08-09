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

import de.ep3.ftpc.model.CrawlerResult;
import de.ep3.ftpc.view.component.TextFieldLabel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Renders one crawler result item (preview image + info text).
 *
 * Note that the preview label might be null.
 */
public class CrawlerResultsItem extends JPanel
{

    private PreviewPanel previewPanel;

    public CrawlerResultsItem(CrawlerResult result)
    {
        setLayout(new MigLayout("fillx, insets 0", "[min!][grow]", "[min!]"));

        /* Result preview */

        JLabel previewLabel;

        if (result.getPreview() == null) {
            previewLabel = new JLabel("?");
        } else {
            previewLabel = new JLabel(new ImageIcon(result.getPreview()));
        }

        previewLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        previewLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);

        previewPanel = new PreviewPanel();
        previewPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        previewPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        previewPanel.setCrawlerResult(result);
        previewPanel.setLayout(new MigLayout("insets 0",
            String.format("[%spx!]", result.getPreviewSize() + 8),
            String.format("[%spx!]", result.getPreviewSize() + 8)));

        previewPanel.add(previewLabel, "align center center");

        add(previewPanel);

        /* Result info */

        TextFieldLabel pathLabel = new TextFieldLabel(result.getFile().getPath());
        pathLabel.setForeground(Color.GRAY);

        TextFieldLabel nameLabel = new TextFieldLabel(result.getFile().getName());

        int fileSize = Math.round(result.getFile().getSize() / 1024);

        TextFieldLabel sizeLabel = new TextFieldLabel(String.format("%,d kB", fileSize));
        sizeLabel.setForeground(Color.GRAY);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new MigLayout("flowy, insets 0"));

        infoPanel.add(nameLabel, "width 95%");
        infoPanel.add(pathLabel, "width 95%");
        infoPanel.add(sizeLabel);

        if (result.getImageWidth() > 0 && result.getImageHeight() > 0) {
            TextFieldLabel dimensionLabel = new TextFieldLabel(String.format("%s x %s px",
                result.getImageWidth(), result.getImageHeight()));
            dimensionLabel.setForeground(Color.GRAY);

            infoPanel.add(dimensionLabel);
        }

        add(infoPanel, "growx, align left center");
    }

    public PreviewPanel getPreviewPanel()
    {
        return previewPanel;
    }

    public class PreviewPanel extends JPanel
    {

        private CrawlerResult crawlerResult;

        public void setCrawlerResult(CrawlerResult crawlerResult)
        {
            this.crawlerResult = crawlerResult;
        }

        public CrawlerResult getCrawlerResult()
        {
            return crawlerResult;
        }

    }

}