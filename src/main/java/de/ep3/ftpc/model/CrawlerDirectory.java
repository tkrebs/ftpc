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

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

import java.io.IOException;

/**
 * Represents a single (FTP) directory and manages the list of files and sub directories within it.
 *
 * This is a state machine, as it remembers the current position and advances on getNextFile() calls.
 */
public class CrawlerDirectory implements FTPFileFilter
{

    private FTPFile[] files;
    private int position;

    private String path;

    public CrawlerDirectory(FTPClient ftpClient, String path) throws IOException
    {
        files = ftpClient.listFiles(path, this);
        position = 0;

        this.path = path;
    }

    public FTPFile getNextFile()
    {
        if (position >= files.length) {
            return null;
        }

        FTPFile ftpFile = files[position];

        position++;

        return ftpFile;
    }

    @Override
    public boolean accept(FTPFile file)
    {
        if (file.getName().equals(".") || file.getName().equals("..")) {
            return false;
        } else {
            return true;
        }
    }

    public String getPath()
    {
        if (! path.endsWith("/")) {
            return path + "/";
        } else {
            return path;
        }
    }

}