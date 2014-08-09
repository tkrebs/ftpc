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

public class ServerListTest
{

    @Test
    public void activeServer()
    {
        ServerList testList = new ServerList();

        Server server1 = new Server();
        Server server2 = new Server();

        testList.add(server1);
        testList.add(server2);

        testList.setActiveServer(server1);

        Assert.assertEquals("true", server1.getTemporary("active"));
        Assert.assertEquals("false", server2.getTemporary("active"));

        Assert.assertEquals(server1, testList.getActiveServer());

        testList.setActiveServer(server2);

        Assert.assertEquals("false", server1.getTemporary("active"));
        Assert.assertEquals("true", server2.getTemporary("active"));

        Assert.assertEquals(server2, testList.getActiveServer());

        testList.setActiveServer(null);

        Assert.assertEquals("false", server1.getTemporary("active"));
        Assert.assertEquals("false", server2.getTemporary("active"));

        Assert.assertNull(testList.getActiveServer());
    }

}