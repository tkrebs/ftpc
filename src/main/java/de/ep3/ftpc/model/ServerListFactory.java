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

import de.ep3.ftpc.AppDirectory;

import java.io.IOException;

public class ServerListFactory
{

    public static ServerList createInstance(AppDirectory appDirectory)
    {
        ServerList serverList;

        if (appDirectory.exists("servers")) {
            try {
                serverList = (ServerList) appDirectory.load("servers");
            } catch (ClassNotFoundException | IOException e) {
                appDirectory.rename("servers", "servers.corrupt");

                throw new RuntimeException(e.getMessage());
            }
        } else {
            serverList = new ServerList();
        }

        return serverList;
    }

}