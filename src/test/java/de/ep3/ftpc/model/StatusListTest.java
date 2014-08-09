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

import org.junit.Assert;
import org.junit.Test;

public class StatusListTest
{

    @Test(expected = IllegalArgumentException.class)
    public void invalidConstructor()
    {
        StatusList testList = new StatusList(0);
    }

    @Test
    public void getLastEmpty()
    {
        StatusList testList = new StatusList(1);

        Assert.assertNull(testList.getLast());
    }

    @Test
    public void getLastSimple()
    {
        StatusList testList = new StatusList(1);

        testList.add("Value 1");
        testList.add("Value 2");

        Assert.assertEquals("Value 2", testList.getLast());
    }

}