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

import javax.swing.event.EventListenerList;
import java.util.Collections;
import java.util.Vector;

/**
 * The server list contains all servers currently setup.
 *
 * It enables other classes to register listeners for server list events.
 */
public class ServerList extends Vector<Server>
{

    private static final long serialVersionUID = 4368331963622179354L;

    private transient EventListenerList listeners;

    public synchronized void addListener(ServerListListener listener)
    {
        getListenerList().add(ServerListListener.class, listener);
    }

    public synchronized void removeListener(ServerListListener listener)
    {
        getListenerList().remove(ServerListListener.class, listener);
    }

    private synchronized void notifyUpdate()
    {
        ServerListEvent e = new ServerListEvent(this);

        for (ServerListListener l : getListenerList().getListeners(ServerListListener.class)) {
            l.serverListUpdated(e);
        }
    }

    private synchronized void notifyActivation()
    {
        ServerListEvent e = new ServerListEvent(this);

        for (ServerListListener l :listeners.getListeners(ServerListListener.class)) {
            l.serverActivated(e);
        }
    }

    private synchronized EventListenerList getListenerList()
    {
        if (listeners == null) {
            listeners = new EventListenerList();
        }

        return listeners;
    }

    public void update()
    {
        Collections.sort(this);

        notifyUpdate();
    }

    @Override
    public synchronized boolean add(Server server)
    {
        boolean res = super.add(server);

        update();

        return res;
    }

    @Override
    public boolean remove(Object server)
    {
        boolean res = super.remove(server);

        update();

        return res;
    }

    public synchronized boolean save(Server server)
    {
        boolean res;

        if (! contains(server)) {
            res = add(server);
        } else {
            update();

            res = true;
        }

        return res;
    }

    @Override
    public void clear()
    {
        super.clear();

        notifyUpdate();
        notifyActivation();
    }

    public synchronized void setActiveServer(Server activeServer)
    {
        for (Server server : this) {
            server.putTemporary("active", "false");
        }

        if (activeServer != null) {
            activeServer.putTemporary("active", "true");
        }

        notifyActivation();
    }

    public synchronized Server getActiveServer()
    {
        for (Server server : this) {
            if (server.hasTemporary("active") && server.getTemporary("active").equals("true")) {
                return server;
            }
        }

        return null;
    }

}