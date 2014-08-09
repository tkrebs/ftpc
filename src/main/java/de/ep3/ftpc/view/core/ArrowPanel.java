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

import de.ep3.ftpc.view.designer.ArrowDesigner;

import javax.swing.*;
import java.awt.*;

/**
 * Guess what: This renders an arrow (for the two arrows in the portal window).
 */
public class ArrowPanel extends JPanel
{

    private ArrowDesigner arrowDesigner;

    public ArrowPanel(ArrowDesigner arrowDesigner)
    {
        this.arrowDesigner = arrowDesigner;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        arrowDesigner.drawDefaultArrow(g, this,
            ArrowDesigner.HorizontalPosition.LEFT,
            ArrowDesigner.VerticalPosition.CENTER,
            ArrowDesigner.Orientation.RIGHT);
    }

}