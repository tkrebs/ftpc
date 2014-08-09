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

import java.io.IOException;
import java.util.Stack;
import java.util.Vector;

/**
 * This class transforms the user given include and exclude paths into lists.
 *
 * It manages a directory stack to recursively step through the FTP directories
 * without using recursion in the code.
 */
public class CrawlerDirectories
{

    private FTPClient ftpClient;

    private Vector<String> includePaths;
    private Vector<String> excludePaths;

    private Stack<CrawlerDirectory> directoryStack;

    public CrawlerDirectories(FTPClient ftpClient, String includePaths, String excludePaths)
    {
        this.ftpClient = ftpClient;

        if (includePaths == null || includePaths.length() == 0) {
            includePaths = "/";
        }

        if (excludePaths == null || excludePaths.length() == 0) {
            excludePaths = null;
        }

        this.includePaths = new Vector<>();

        for (String includePath : includePaths.split("\n")) {
            this.includePaths.add(includePath.trim());
        }

        this.excludePaths = new Vector<>();

        if (excludePaths != null) {
            for (String excludePath : excludePaths.split("\n")) {
                this.excludePaths.add(excludePath.trim());
            }
        }

        directoryStack = new Stack<>();
    }

    /**
     * Provides the next file or directory in the current stack.
     *
     * Since this is a state machine, it will return a different results on
     * consecutive calls.
     *
     * @return File, directory or null, if finished.
     * @throws IOException
     */
    public CrawlerFile getNextFile() throws IOException
    {
        if (directoryStack.isEmpty()) {
            if (includePaths.isEmpty()) {
                return null;
            }

            String nextIncludePath = includePaths.firstElement();
            includePaths.remove(nextIncludePath);

            directoryStack.push(new CrawlerDirectory(ftpClient, nextIncludePath));
        }

        CrawlerDirectory currentDirectory = directoryStack.peek();

        FTPFile file = currentDirectory.getNextFile();

        if (file == null) {
            directoryStack.pop();

            return getNextFile();
        }

        if (file.getType() == FTPFile.DIRECTORY_TYPE) {
            String nextPath = currentDirectory.getPath() + file.getName();

            if (! excludePaths.contains(nextPath)) {
                directoryStack.push(new CrawlerDirectory(ftpClient, nextPath));
            }
        }

        return new CrawlerFile(file, currentDirectory.getPath());
    }

}