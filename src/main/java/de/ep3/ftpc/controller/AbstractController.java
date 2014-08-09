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

package de.ep3.ftpc.controller;

import de.ep3.ftpc.model.StatusManager;
import de.ep3.ftpc.model.i18n.I18nManager;

public abstract class AbstractController implements Controller
{

    protected I18nManager i18n;
    protected StatusManager status;

    @Override
    public void setI18nManager(I18nManager i18n)
    {
        this.i18n = i18n;
    }

    public I18nManager getI18nManager()
    {
        return i18n;
    }

    @Override
    public void setStatusManager(StatusManager status)
    {
        this.status = status;
    }

    public StatusManager getStatusManager()
    {
        return status;
    }

}