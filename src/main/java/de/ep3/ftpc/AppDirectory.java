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

package de.ep3.ftpc;

import java.io.*;

/**
 * Provides methods to save files to and load files from the application home directory.
 *
 * Used for storing serialized configuration and server list data structures.
 */
public class AppDirectory extends CurrentDirectory
{

    @Override
    public String getCurrentDirectoryPath()
    {
        if (currentDirectoryPath == null) {
            currentDirectoryPath = System.getProperty("user.home")
                + File.separator
                + "." + App.getID().toLowerCase()
                + File.separator
                + App.getVersion()
                + File.separator;
        }

        return currentDirectoryPath;
    }

}