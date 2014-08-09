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

public class StatusList
{

    private String[] list;
    private int listCursor;

    public StatusList(int capacity)
    {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        list = new String[capacity];
        listCursor = -1;
    }

    public void add(String status)
    {
        listCursor = (listCursor + 1) % list.length;
        list[listCursor] = status;
    }

    public String getLast()
    {
        if (listCursor == -1) {
            return null;
        }

        return list[listCursor];
    }

}