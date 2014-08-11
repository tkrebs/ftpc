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

package de.ep3.ftpc.model;

import de.ep3.ftpc.model.i18n.I18nManager;
import de.ep3.ftpc.view.core.CrawlerResultsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;

/**
 * The crawler printer renders the crawler result items onto the printing page.
 */
public class CrawlerPrinter implements Printable
{

    private CrawlerResultsPanel crawlerResultsPanel;
    private I18nManager i18n;

    public CrawlerPrinter(CrawlerResultsPanel crawlerResultsPanel, I18nManager i18n)
    {
        this.crawlerResultsPanel = crawlerResultsPanel;
        this.i18n = i18n;
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException
    {
        JPanel resultsPanel = crawlerResultsPanel.getResultsPanel();

        if (resultsPanel == null) {
            return NO_SUCH_PAGE;
        }

        int componentCount = resultsPanel.getComponentCount();

        if (componentCount == 0) {
            return NO_SUCH_PAGE;
        }

        /* Prepare page data */

        int pagePadding = (int) Math.floor(pageFormat.getImageableWidth() / 25);

        int pageWidth = (int) Math.floor(pageFormat.getImageableWidth() - (pagePadding * 2));
        int pageHeight = (int) Math.floor(pageFormat.getImageableHeight() - (pagePadding * 2));
        int pageX = (int) pageFormat.getImageableX() + pagePadding;
        int pageY = (int) pageFormat.getImageableY() + pagePadding;

        /* Prepare component data */

        int componentsPerRow = 2;
        int componentsPerCol = 8;

        int componentWidth = (int) Math.floor(pageWidth / ((float) componentsPerRow));
        int componentHeight = (int) Math.floor(pageHeight / ((float) componentsPerCol));

        /* Prepare layout data */

        int componentsPerPage = componentsPerCol * componentsPerRow;

        int componentCursor = pageIndex * componentsPerPage;
        int componentLimit = componentCursor + componentsPerPage;

        if (componentCursor >= componentCount) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pageX, pageY);
        g2d.setClip(0, 0, pageWidth, pageHeight);

        for (int i = componentCursor; i < componentLimit; i++) {
            Component component;

            try {
                component = resultsPanel.getComponent(i);
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }

            try {
                int imageWidth = Math.round(componentWidth * 1.5F);
                int imageHeight = component.getHeight();

                BufferedImage componentImage = printComponent(component, imageWidth, imageHeight);

                int relativeI = i - componentCursor;
                int colI = relativeI % componentsPerRow;

                int offsetX = colI * componentWidth;
                int offsetY = ((relativeI - colI) / componentsPerRow) * componentHeight;

                int renderWidth = componentWidth;
                float scalingFactor = (componentWidth / ((float) imageWidth));
                int renderHeight = Math.round(imageHeight * scalingFactor);

                g2d.drawImage(componentImage, offsetX, offsetY, renderWidth, renderHeight, null);
            } catch (IOException e) {
                throw new PrinterException(e.getMessage());
            }
        }

        /* Draw page number */

        String pageString = i18n.translate("printPage", (pageIndex + 1), (int) Math.ceil(componentCount / ((float) componentsPerPage)));
        String resultString = i18n.translate("printResults", componentCount);

        FontMetrics fm = g2d.getFontMetrics(g2d.getFont());

        g2d.drawString(resultString, 0, pageHeight - 8);
        g2d.drawString(pageString, Math.round(pageWidth - fm.stringWidth(pageString)), pageHeight - 8);

        return PAGE_EXISTS;
    }

    private BufferedImage printComponent(Component component, int width, int height) throws IOException
    {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g = img.getGraphics();

        g.setColor(component.getForeground());
        g.setFont(component.getFont());

        int originalWidth = component.getWidth();
        int originalHeight = component.getHeight();

        component.setSize(width, height);

        component.paintAll(g);

        component.setSize(originalWidth, originalHeight);

        return img;
    }

}