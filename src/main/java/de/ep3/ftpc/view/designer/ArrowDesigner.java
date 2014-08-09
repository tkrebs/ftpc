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

package de.ep3.ftpc.view.designer;

import javax.swing.*;
import java.awt.*;

/**
 * This designer just knows how to draw BEAUTIFUL arrows.
 */
public class ArrowDesigner
{

    private int defaultArrowSize = 25;

    private UIDesigner uiDesigner;

    public ArrowDesigner(UIDesigner uiDesigner)
    {
        this.uiDesigner = uiDesigner;
    }

    public int getDefaultArrowSize()
    {
        return defaultArrowSize;
    }

    public void drawDefaultArrow(Graphics g, JComponent c, HorizontalPosition posX, VerticalPosition posY, Orientation orientation)
    {
        int boxSize = getDefaultArrowSize();
        int boxSizeHalf = Math.round((float) boxSize / 2);

        int boxX = 0;
        int boxY = 0;

        switch (posX) {
            case LEFT:
                boxX = 0;
                break;
            case CENTER:
                boxX = Math.round((float) c.getWidth() / 2) - boxSizeHalf;
                break;
            case RIGHT:
                boxX = c.getWidth() - boxSize;
                break;
        }

        switch (posY) {
            case TOP:
                boxY = 0;
                break;
            case CENTER:
                boxY = Math.round((float) c.getHeight() / 2) - boxSizeHalf;
                break;
            case BOTTOM:
                boxY = c.getHeight() - boxSize;
                break;
        }

        int[] xCoords = {};
        int[] yCoords = {};

        switch (orientation) {
            case LEFT:
                xCoords = new int[]{ boxX + boxSize, boxX, boxX + boxSize };
                yCoords = new int[]{ boxY, boxY + boxSizeHalf, boxY + boxSize };
                break;
            case TOP:
                xCoords = new int[]{ boxX, boxX + boxSizeHalf, boxX + boxSize };
                yCoords = new int[]{ boxY + boxSize, boxY, boxY + boxSize };
                break;
            case RIGHT:
                xCoords = new int[]{ boxX, boxX + boxSize, boxX };
                yCoords = new int[]{ boxY, boxY + boxSizeHalf, boxY + boxSize };
                break;
            case BOTTOM:
                xCoords = new int[]{ boxX, boxX + boxSizeHalf, boxX + boxSize };
                yCoords = new int[]{ boxY, boxY + boxSize, boxY };
                break;
        }

        Graphics2D g2d = (Graphics2D) g.create();

        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(uiDesigner.getDefaultBorderColor());

            g2d.fillPolygon(xCoords, yCoords, xCoords.length);
        } finally {
            g2d.dispose();
        }
    }

    public static enum HorizontalPosition
    {
        LEFT, CENTER, RIGHT
    }

    public static enum VerticalPosition
    {
        TOP, CENTER, BOTTOM
    }

    public static enum Orientation
    {
        LEFT, TOP, RIGHT, BOTTOM
    }

}