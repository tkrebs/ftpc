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

import de.ep3.ftpc.view.designer.UIDesigner;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Renders a flat button using the UIDesigner's default background color with white text.
 */
public class FlatButton extends JButton
{

    UIDesigner uiDesigner;

    public FlatButton(UIDesigner uiDesigner)
    {
        this.uiDesigner = uiDesigner;

        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Color backgroundColor = uiDesigner.getDefaultBorderColor();

        if (getModel().isRollover()) {
            backgroundColor = backgroundColor.darker();
        }

        if (getModel().isPressed()) {
            backgroundColor = backgroundColor.darker();
        }

        g2d.setBackground(backgroundColor);
        g2d.clearRect(0, 0, getWidth(), getHeight());

        String text = getText();

        if (text.length() == 1) {
            g2d.setFont(g2d.getFont().deriveFont(18.0F).deriveFont(Font.BOLD));
        }

        FontMetrics fontMetrics = g2d.getFontMetrics();

        Rectangle2D textBounds = fontMetrics.getStringBounds(text, g2d);

        int textX = Math.round(getWidth() / 2.0F - (int) textBounds.getWidth() / 2.0F);
        int textY = Math.round(getHeight() / 2.0F + (int) textBounds.getHeight() / 4.0F);

        g2d.setColor(Color.WHITE);
        g2d.drawString(text, textX, textY);
    }

}