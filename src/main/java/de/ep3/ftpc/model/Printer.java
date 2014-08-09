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

import de.ep3.ftpc.view.core.CrawlerResultsPanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;

/**
 * The printer essentially grabs the image (the crawler result panel knows how to render)
 * and draws it for the printer.
 *
 * The image will be scaled according to the panel AND the page, so resizing the portal window
 * does in fact have impact on the printing result. Not the most professional way, but it's sufficient for now.
 */
public class Printer implements Printable
{

    private CrawlerResultsPanel crawlerResultsPanel;

    public Printer(CrawlerResultsPanel crawlerResultsPanel)
    {
        this.crawlerResultsPanel = crawlerResultsPanel;
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException
    {
        if (pageIndex >= 300) {
            return NO_SUCH_PAGE;
        }

        double pageWidth = pageFormat.getImageableWidth();
        double pageHeight = pageFormat.getImageableHeight();
        double pageX = pageFormat.getImageableX();
        double pageY = pageFormat.getImageableY();

        Component pane = crawlerResultsPanel.getScrollPane().getViewport().getView();

        double scalingFactor = pageWidth / pane.getWidth();

        int buffer = 32; // The buffer will render the same image contents twice on page breaks.

        double paneWidth = pane.getWidth();
        double paneHeight = pageHeight * (1 / scalingFactor);
        double paneX = 0;
        double paneY = (pageIndex * paneHeight) - (pageIndex * buffer);

        if (paneY >= pane.getHeight()) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pageX, pageY);
        g2d.setClip(0, 0, (int) pageWidth, (int) pageHeight);

        try {
            if (paneY + paneHeight > pane.getHeight()) {
                paneHeight = pane.getHeight() - paneY;
            }

            Rectangle paneRegion = new Rectangle((int) paneX, (int) paneY, (int) paneWidth, (int) paneHeight);

            BufferedImage paneImage = printComponent(pane, paneRegion);

            g2d.drawImage(paneImage, 0, 0, (int) pageWidth, (int) (paneImage.getHeight() * scalingFactor), null);
        } catch (IOException e) {
            throw new PrinterException(e.getMessage());
        }

        return PAGE_EXISTS;
    }

    private BufferedImage printComponent(Component component, Rectangle region) throws IOException
    {
        BufferedImage img = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics g = img.getGraphics();

        g.setColor(component.getForeground());
        g.setFont(component.getFont());

        component.paintAll(g);

        if (region == null) {
            region = new Rectangle(0, 0, img.getWidth(), img.getHeight());
        }

        return img.getSubimage(region.x, region.y, region.width, region.height);
    }

}