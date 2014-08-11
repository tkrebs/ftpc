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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Represents one result of the crawler (usually an image file).
 *
 * Besides containing some references it can also generate the preview image.
 */
public class CrawlerResult
{

    private CrawlerFile file;
    private String fileType;

    private Server server;
    private FTPClient ftpClient;

    private BufferedImage preview;
    private int previewSize = 100;
    private int previewPercentage = 0;

    private int imageWidth = 0;
    private int imageHeight = 0;

    public CrawlerResult(CrawlerFile file, String fileType, Server server, FTPClient ftpClient)
    {
        this.file = file;
        this.fileType = fileType;

        this.server = server;
        this.ftpClient = ftpClient;
    }

    /**
     * Downloads the image file this result refers to and generates a small, low quality preview image from it.
     *
     * The download progress in percent is also regularly updated.
     *
     * @throws IOException
     */
    public void generatePreview() throws IOException
    {
        InputStream is = ftpClient.retrieveFileStream(file.getFullName());

        if (is != null) {

            /* Download raw image file */

            byte[] rawFile = new byte[(int) file.getSize()];

            int i = 0;

            while (true) {
                int b = is.read();

                if (b == -1) {
                    break;
                }

                rawFile[i] = (byte) b;
                i++;

                /* Occasionally update the download progress */

                if (i % 1024 == 0) {
                    int progress = Math.round((((float) i) / file.getSize()) * 100);

                    // Limit the progress to 99%, since preview generation is about to follow
                    progress = Math.min(progress, 99);

                    setPreviewPercentage(progress);
                }
            }

            is.close();
            is = null;

            if (! ftpClient.completePendingCommand()) {
                throw new IOException();
            }

            /* Generate preview image */

            InputStream bis = new ByteArrayInputStream(rawFile);

            BufferedImage image = ImageIO.read(bis);

            if (image == null) {
                bis.close();
            } else {
                imageWidth = image.getWidth();
                imageHeight = image.getHeight();

                if (imageWidth > previewSize || imageHeight > previewSize) {
                    Image tmpImage;

                    if (imageWidth > previewSize && imageHeight > previewSize) {
                        if (imageWidth > imageHeight) {
                            tmpImage = image.getScaledInstance(previewSize, -1, Image.SCALE_FAST);
                        } else {
                            tmpImage = image.getScaledInstance(-1, previewSize, Image.SCALE_FAST);
                        }
                    } else if (imageWidth > previewSize && imageHeight <= previewSize) {
                        tmpImage = image.getScaledInstance(previewSize, -1, Image.SCALE_FAST);
                    } else if (imageWidth <= previewSize && imageHeight > previewSize) {
                        tmpImage = image.getScaledInstance(-1, previewSize, Image.SCALE_FAST);
                    } else {
                        tmpImage = image.getScaledInstance(-1, -1, Image.SCALE_FAST);
                    }

                    image = generateImageToBuffered(tmpImage);
                }

                setPreview(image);
            }
        }
    }

    private BufferedImage generateImageToBuffered(Image image)
    {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();

        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bufferedImage;
    }

    public CrawlerFile getFile()
    {
        return file;
    }

    public String getFileType()
    {
        return fileType;
    }

    public Server getServer()
    {
        return server;
    }

    public FTPClient getFtpClient()
    {
        return ftpClient;
    }

    public synchronized void setPreview(BufferedImage preview)
    {
        this.preview = preview;
    }

    public synchronized BufferedImage getPreview()
    {
        return preview;
    }

    public int getPreviewSize()
    {
        return previewSize;
    }

    public synchronized void setPreviewPercentage(int percentage)
    {
        previewPercentage = Math.min(Math.max(percentage, 0), 100);
    }

    public synchronized int getPreviewPercentage()
    {
        return previewPercentage;
    }

    public synchronized int getImageWidth()
    {
        return imageWidth;
    }

    public synchronized int getImageHeight()
    {
        return imageHeight;
    }

}