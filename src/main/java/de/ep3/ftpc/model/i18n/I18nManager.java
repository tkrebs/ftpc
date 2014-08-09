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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * The internationalization manager is used throughout the application and
 * encapsulates the access on all i18n-related methods.
 */
public class I18nManager
{

    private Translator translator;
    private DateFormat dateFormatter;

    public I18nManager(Translator translator, DateFormat dateFormatter)
    {
        this.translator = translator;
        this.dateFormatter = dateFormatter;
    }

    public String translate(String key)
    {
        return translator.translate(key);
    }

    public String translate(String key, Object... args)
    {
        return String.format(translate(key), args);
    }

    public Date parseDate(String text) throws ParseException
    {
        return dateFormatter.parse(text);
    }

    public boolean isDate(String text)
    {
        try {
            parseDate(text);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public String formatDate(Date date)
    {
        return dateFormatter.format(date);
    }

}