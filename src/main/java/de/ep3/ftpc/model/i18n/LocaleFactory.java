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

package de.ep3.ftpc.model.i18n;

import de.ep3.ftpc.model.Configuration;

import java.util.Locale;

public class LocaleFactory
{

    public static Locale createInstance(Configuration config)
    {
        String[] localeParts = config.need("app.locale").split("-");

        if (localeParts.length != 2) {
            throw new IllegalStateException("Invalid locale configuration");
        }

        Locale.Builder localeBuilder = new Locale.Builder();

        localeBuilder.setLanguage(localeParts[0]);
        localeBuilder.setRegion(localeParts[1]);

        Locale locale = localeBuilder.build();

        Locale.setDefault(locale);

        return locale;
    }

}