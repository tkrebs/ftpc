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

/**
 * Represents a single server, containing key/value pairs of server configuration.
 */
public class Server extends Hashtable<String, String> implements Comparable<Server>
{

    private static final long serialVersionUID = -9208654236649766213L;

    @Override
    public synchronized boolean has(String key)
    {
        if (super.has(key)) {
            String value = get(key);

            if (value.length() > 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public synchronized boolean hasTemporary(String key)
    {
        if (super.hasTemporary(key)) {
            String value = getTemporary(key);

            if (value.length() > 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int compareTo(Server s)
    {
        String myServerName = get("server.name");

        if (myServerName == null) {
            myServerName = "";
        }

        String hisServerName = s.get("server.name");

        if (hisServerName == null) {
            hisServerName = "";
        }

        return myServerName.compareTo(hisServerName);
    }

}