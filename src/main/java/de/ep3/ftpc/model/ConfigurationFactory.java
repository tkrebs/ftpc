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

import de.ep3.ftpc.App;
import de.ep3.ftpc.AppDirectory;

import java.io.IOException;
import java.util.Locale;

public class ConfigurationFactory
{

    public static Configuration createInstance(AppDirectory appDirectory)
    {
        Configuration config;

        /* Load or create config */

        if (appDirectory.exists("config")) {
            try {
                config = (Configuration) appDirectory.load("config");
            } catch (ClassNotFoundException | IOException e) {
                appDirectory.rename("config", "config.corrupt");

                throw new RuntimeException(e.getMessage());
            }
        } else {
            config = new Configuration();
            config.put("app.locale", Locale.getDefault().toLanguageTag());
            config.put("app.view.scroll", "true");
        }

        /* Override default values */

        config.put("app.name", "FTP Crawler");
        config.put("app.version", App.getVersion());
        config.put("app.version.year", "2014");
        config.put("app.license", "GNU GPL v3.0");
        config.put("app.author.name", "Tobias Krebs");
        config.put("app.author.homepage", "http://www.ep-3.de/projekte/ftpc");
        config.put("app.source.homepage", "https://github.com/tkrebs/ftpc");

        return config;
    }

}