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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This is a normal textarea which can be tabbed out (instead of printing tab characters).
 */
public class UntabbedTextArea extends JTextArea implements KeyListener
{

    public UntabbedTextArea()
    {
        super();

        addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e)
    { }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_TAB:
                e.consume(); // See? This is where we prevent the tab char. He he he ...

                if (e.isShiftDown()) {
                    transferFocusBackward();
                } else {
                    transferFocus();
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    { }

}