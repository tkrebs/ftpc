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

package de.ep3.ftpc.view.component;

import javax.swing.*;
import java.awt.*;

/**
 * Renders a (preferably) flat icon (button) with some mouse over and pressed effects.
 */
public class IconButton extends JButton
{

    public IconButton(Icon icon)
    {
        super(icon);

        setBorder(null);
        setContentAreaFilled(false);
        setFocusPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        float opacity = 0.5F;

        if (getModel().isRollover()) {
            opacity = 0.75F;
        }

        if (getModel().isPressed()) {
            opacity = 1.0F;
        }

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

        super.paintComponent(g);
    }

}