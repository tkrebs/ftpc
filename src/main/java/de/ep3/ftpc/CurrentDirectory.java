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
 * Provides methods to save files to and load files from the current working directory.
 */
public class CurrentDirectory
{

    protected String currentDirectoryPath;
    protected String serializedFileExtension = ".ser";

    public String getCurrentDirectoryPath()
    {
        if (currentDirectoryPath == null) {
            String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

            path = path.substring(0, path.lastIndexOf("/"));
            path = path.replaceAll("%20", " ");
            path = path.replaceAll("//", File.separator);

            currentDirectoryPath = path + File.separator;
        }

        return currentDirectoryPath;
    }

    public File save(Object object, String identifier) throws IOException
    {
        String fileName = identifier + serializedFileExtension;

        File filePath = new File(getCurrentDirectoryPath());
        File file = new File(filePath, fileName);

        if (! filePath.exists()) {
            if (! filePath.mkdirs()) {
                throw new RuntimeException("Directories for file " + fileName + " could not be created");
            }
        }

        try (
            OutputStream fileStream = new FileOutputStream(file);
            OutputStream bufferedFileStream = new BufferedOutputStream(fileStream);
            ObjectOutput outputStream = new ObjectOutputStream(bufferedFileStream);
        ) {
            outputStream.writeObject(object);
        } catch (IOException e) {
            throw new IOException("File " + fileName + " could not be saved (" + e.getMessage() + ")");
        }

        return file;
    }

    public Object load(String identifier) throws ClassNotFoundException, IOException
    {
        String fileName = identifier + serializedFileExtension;

        File filePath = new File(getCurrentDirectoryPath());
        File file = new File(filePath, fileName);

        Object object;

        try (
            InputStream fileStream = new FileInputStream(file);
            InputStream bufferedFileStream = new BufferedInputStream(fileStream);
            ObjectInput inputStream = new ObjectInputStream (bufferedFileStream);
        ) {
            object = inputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("File " + fileName + " could not be parsed (" + e.getMessage() + ")");
        } catch (IOException e) {
            throw new IOException("File " + fileName + " could not be loaded (" + e.getMessage() + ")");
        }

        return object;
    }

    public boolean exists(String identifier)
    {
        String fileName = identifier + serializedFileExtension;

        File filePath = new File(getCurrentDirectoryPath());
        File file = new File(filePath, fileName);

        if (file.exists() && ! file.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean rename(String currentIdentifier, String newIdentifier)
    {
        if (exists(currentIdentifier)) {
            String fileNameCurrent = currentIdentifier + serializedFileExtension;
            String fileNameNew = newIdentifier + serializedFileExtension;

            File filePath = new File(getCurrentDirectoryPath());
            File fileCurrent = new File(filePath, fileNameCurrent);
            File fileNew = new File(filePath, fileNameNew);

            return fileCurrent.renameTo(fileNew);
        }

        return false;
    }

    public boolean delete(String identifier)
    {
        String fileName = identifier + serializedFileExtension;

        File filePath = new File(getCurrentDirectoryPath());
        File file = new File(filePath, fileName);

        return file.delete();
    }

}