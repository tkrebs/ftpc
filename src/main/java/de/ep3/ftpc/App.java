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

import de.ep3.ftpc.controller.Controller;
import de.ep3.ftpc.model.CrawlerManager;
import de.ep3.ftpc.model.ServerListManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;

/**
 * This is where the journey begins.
 *
 * This class creates and provides the Spring Framework DI container alias context
 * (which is heavily used across the application).
 */
public final class App
{

    private static ApplicationContext context;

    private static String id = "FTPC";
    private static String version = "1.12";

    public static ApplicationContext getContext()
    {
        if (context == null) {
            context = new ClassPathXmlApplicationContext("beans.xml");
        }

        return context;
    }

    public static String getID()
    {
        return id;
    }

    public static String getVersion()
    {
        return version;
    }

    public static void main(String[] args)
    {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }

        Controller e = getContext().getBean("exceptionController", Controller.class);

        e.dispatch();

        Controller c = getContext().getBean("portalFrameController", Controller.class);

        c.dispatch();
    }

    public static void exit()
    {
        exit(0);
    }

    public static void exit(int status)
    {
        /* Save server list to file */

        try {
            getContext().getBean("serverListManager", ServerListManager.class).saveServerList();
        } catch (Exception e) { }

        /* Shut down crawler thread if running */

        try {
            CrawlerManager crawlerManager = getContext().getBean("crawlerManager", CrawlerManager.class);

            crawlerManager.getCrawlerThread().interrupt();
            crawlerManager.getCrawlerThread().join(10_000);
        } catch (Exception e) { }

        /* Bye! */

        System.exit(status);
    }

}