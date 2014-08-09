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
import org.apache.commons.net.ftp.FTPFile;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.PatternSyntaxException;

/**
 * A wrapper around the "native" FTPFile class.
 *
 * Adds path information and logic to decide whether this file is relevant for crawling or not
 * (depending on the crawler settings provided by the user).
 */
public class CrawlerFile
{

    private FTPFile ftpFile;
    private String path;

    public CrawlerFile(FTPFile ftpFile, String path)
    {
        this.ftpFile = ftpFile;
        this.path = path;
    }

    public String getPath()
    {
        return path;
    }

    public String getName()
    {
        return ftpFile.getName();
    }

    public String getFullName()
    {
        return getPath() + getName();
    }

    public String getExtension()
    {
        String[] segments = getName().split("\\.");

        return segments[segments.length - 1];
    }

    public long getSize()
    {
        return ftpFile.getSize();
    }

    public boolean isDir()
    {
        if (ftpFile.getType() == FTPFile.DIRECTORY_TYPE) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isFile()
    {
        if (ftpFile.getType() == FTPFile.FILE_TYPE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Decides whether this file is relevant for crawling or not
     * (depending on the crawler settings provided by the user).
     *
     * @param server Contains the crawler settings.
     * @param i18n To check and parse the user provided dates.
     * @return The decision.
     */
    public boolean isRelevant(Server server, I18nManager i18n)
    {
        if (! isFile()) {
            return false;
        }

        if (server == null) {
            return false;
        }

        try {
            if (! server.has("crawler.file-name")) {
                return false;
            }

            if (! getName().matches(server.get("crawler.file-name"))) {
                return false;
            }
        } catch (PatternSyntaxException e) {
            return false;
        }

        long size = ftpFile.getSize() / 1024;

        if (server.has("crawler.file-size-min")) {
            long minSize = Long.parseLong(server.get("crawler.file-size-min"));

            if (size < minSize) {
                return false;
            }
        }

        if (server.has("crawler.file-size-max")) {
            long maxSize = Long.parseLong(server.get("crawler.file-size-max"));

            if (size > maxSize) {
                return false;
            }
        }

        Date date = ftpFile.getTimestamp().getTime();

        if (server.has("crawler.file-date-start")) {
            try {
                Date startDate = i18n.parseDate(server.get("crawler.file-date-start"));

                if (date.before(startDate)) {
                    return false;
                }
            } catch (ParseException e) {
                return false;
            }
        }

        if (server.has("crawler.file-date-end")) {
            try {
                Date endDate = i18n.parseDate(server.get("crawler.file-date-end"));

                if (date.after(endDate)) {
                    return false;
                }
            } catch (ParseException e) {
                return false;
            }
        }

        return true;
    }

}