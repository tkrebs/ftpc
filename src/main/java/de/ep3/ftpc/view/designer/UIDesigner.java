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

package de.ep3.ftpc.view.designer;

import de.ep3.ftpc.App;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Provides the UI's default primary color and some convenience methods for icon and image loading.
 */
public class UIDesigner
{

    private Border defaultBorder;
    private Color defaultBorderColor;

    public UIDesigner()
    {
        defaultBorderColor = Color.GRAY;
        defaultBorder = BorderFactory.createLineBorder(defaultBorderColor);
    }

    public Border getDefaultBorder()
    {
        return defaultBorder;
    }

    public Color getDefaultBorderColor()
    {
        return defaultBorderColor;
    }

    public Icon getDefaultIcon(String fileName)
    {
        return getDefaultIcon("icon", fileName);
    }

    /**
     * Loads an icon image from the resources directory provided.
     *
     * The icon will be recolored according to the UI default color
     * (with respect to its alpha values).
     *
     * @param dirName The directory name under the resource/drawable directory.
     * @param fileName The file name under the directory name provided before.
     * @return The icom ready to be displayed within the GUI.
     */
    public Icon getDefaultIcon(String dirName, String fileName)
    {
        BufferedImage image = getDefaultImage(dirName, fileName);

        /* Change icon color according to UI color */

        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();

        int defaultRGBA = getDefaultBorderColor().getRGB();
        int defaultRGB = defaultRGBA & 0x00FFFFFF;

        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int imageRGBA = image.getRGB(x, y);
                int imageA = imageRGBA & 0xFF000000;

                int newRGBA = defaultRGB | imageA; // I'm quite proud of this little bitwise color calculation ^-^

                image.setRGB(x, y, newRGBA);
            }
        }

        return new ImageIcon(image);
    }

    /**
     * Loads a (buffered) image from the resources directory provided.
     *
     * @param dirName The directory name under the resource/drawable directory.
     * @param fileName The file name under the directory name provided before.
     * @return A hopefully nice image.
     */
    public BufferedImage getDefaultImage(String dirName, String fileName)
    {
        String filePath = "drawable" + File.separator + dirName + File.separator + fileName;

        Resource res = App.getContext().getResource(filePath);

        BufferedImage image;

        try {
            image = ImageIO.read(res.getInputStream());
        } catch (IOException e) {
            // Don't blame this checked-to-unchecked exception conversion. This is by design (fail fast and early).
            throw new IllegalStateException("Unable to load image file " + fileName + " (" + e.getMessage() + ")");
        }

        return image;
    }

    /**
     * Loads a list of (buffered) images from the resource directory provided.
     *
     * @param dirName The directory name under the resource/drawable directory.
     * @param fileNames The files name under the directory name provided before.
     * @return A list of hopefully nice images.
     */
    public List<? extends BufferedImage> getDefaultImageList(String dirName, String... fileNames)
    {
        List<BufferedImage> list = new Vector<>();

        for (String fileName : fileNames) {
            list.add(getDefaultImage(dirName, fileName));
        }

        return list;
    }

    /**
     * This method provides the default application icons for windows, task bars, etc.
     *
     * @return The list, obviously.
     */
    public List<? extends BufferedImage> getDefaultAppIcons()
    {
        return getDefaultImageList("logo", "32.png", "64.png", "128.png", "256.png", "512.png");
    }

}