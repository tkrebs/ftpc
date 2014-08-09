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

/**
 * Fakes a normal label by using a textarea.
 * This enables cutting off too long lines in the layouts and
 * makes multiple lines easier without HTML cluttering.
 */
public class TextAreaLabel extends JTextArea
{

    public TextAreaLabel(String text)
    {
        super(" " + text + " ");

        setAutoscrolls(false);
        setBorder(null);
        setEditable(false);
        setFocusable(false);
        setFont(new JLabel().getFont());
        setMargin(null);
        setOpaque(false);
    }

}