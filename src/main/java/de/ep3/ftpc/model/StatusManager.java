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
import de.ep3.ftpc.view.portal.PortalStatusBar;

import javax.swing.*;

/**
 * Manages the status bar and its contents asynchronously in an own thread.
 *
 * Status bar messages will fade away after some delay.
 */
public class StatusManager extends Thread
{

    private final PortalStatusBar statusBar;
    private final I18nManager i18n;
    private final StatusList statusList;

    private int defaultStatusDelay = 12000;

    public StatusManager(PortalStatusBar statusBar, I18nManager i18n)
    {
        this.statusBar = statusBar;
        this.i18n = i18n;
        this.statusList = new StatusList(10);

        setDaemon(true);
        setPriority(3);
        start();
    }

    /**
     * Adds, rather sets, the current status bar line (left bottom corner).
     *
     * @param status The new status bar line.
     */
    public void add(String status)
    {
        synchronized (statusList) {
            statusList.add(status);
            statusList.notify();
        }
    }

    /**
     * Sets the current status bar notes line (right bottom corner).
     *
     * @param notes The new status bar notes line.
     */
    public void setNotes(String notes)
    {
        statusBar.getNotesLabel().setText(notes);
    }

    @Override
    public void run()
    {
        String idleMessage = i18n.translate("portalStatusIdle");

        while (true) {
            synchronized (statusList) {
                String lastStatusString = statusList.getLast();

                if (lastStatusString != null) {
                    setStatus(lastStatusString);

                    try {
                        statusList.wait(defaultStatusDelay);
                    } catch (InterruptedException e) { }
                }

                if (lastStatusString == null || lastStatusString.equals(statusList.getLast())) {
                    setStatus(idleMessage);

                    try {
                        statusList.wait();
                    } catch (InterruptedException e) { }
                }
            }
        }
    }

    private void setStatus(final String status)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                statusBar.getStatusLabel().setText(status);
            }
        });
    }

}