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

import javax.swing.*;
import javax.swing.event.EventListenerList;

public class Crawler
{

    private EventListenerList listeners;

    private Status status = Status.IDLE;
    private Server server;

    public synchronized void addListener(CrawlerListener listener)
    {
        getListenerList().add(CrawlerListener.class, listener);
    }

    public synchronized void removeListener(CrawlerListener listener)
    {
        getListenerList().remove(CrawlerListener.class, listener);
    }

    private synchronized EventListenerList getListenerList()
    {
        if (listeners == null) {
            listeners = new EventListenerList();
        }

        return listeners;
    }

    private synchronized void notifyStart()
    {
        CrawlerEvent e = new CrawlerEvent(this);

        for (CrawlerListener l : getListenerList().getListeners(CrawlerListener.class)) {
            l.crawlerStarted(new CrawlerEvent(this));
        }
    }

    private synchronized void notifyPause()
    {
        CrawlerEvent e = new CrawlerEvent(this);

        for (CrawlerListener l : getListenerList().getListeners(CrawlerListener.class)) {
            l.crawlerPaused(new CrawlerEvent(this));
        }
    }

    private synchronized void notifyResume()
    {
        CrawlerEvent e = new CrawlerEvent(this);

        for (CrawlerListener l : getListenerList().getListeners(CrawlerListener.class)) {
            l.crawlerResumed(new CrawlerEvent(this));
        }
    }

    private synchronized void notifyStop()
    {
        CrawlerEvent e = new CrawlerEvent(this);

        for (CrawlerListener l : getListenerList().getListeners(CrawlerListener.class)) {
            l.crawlerStopped(new CrawlerEvent(this));
        }
    }

    public synchronized void start(Server server)
    {
        this.server = server;

        status = Status.RUNNING;

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                notifyStart();
            }
        });
    }

    public synchronized void pause()
    {
        status = Status.PAUSED;

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                notifyPause();
            }
        });
    }

    public synchronized void resume()
    {
        status = Status.RUNNING;

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                notifyResume();
            }
        });
    }

    public synchronized void stop()
    {
        status = Status.IDLE;

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                notifyStop();
            }
        });
    }

    public synchronized Status getStatus()
    {
        return status;
    }

    public synchronized Server getServer()
    {
        return server;
    }

    public static enum Status
    {
        IDLE, RUNNING, PAUSED
    }

}