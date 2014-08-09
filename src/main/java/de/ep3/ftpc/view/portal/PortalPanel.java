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

package de.ep3.ftpc.view.portal;

import de.ep3.ftpc.view.core.ArrowPanel;
import de.ep3.ftpc.view.core.CrawlerResultsPanel;
import de.ep3.ftpc.view.core.CrawlerSettingsPanel;
import de.ep3.ftpc.view.core.ServerListPanel;
import de.ep3.ftpc.view.designer.ArrowDesigner;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class PortalPanel extends JPanel
{

    private ServerListPanel serverListPanel;

    public PortalPanel(ServerListPanel serverListPanel, CrawlerSettingsPanel crawlerSettingsPanel, CrawlerResultsPanel crawlerResultsPanel,
        ArrowDesigner arrowDesigner)
    {
        int arrowSize = arrowDesigner.getDefaultArrowSize();

        setLayout(new MigLayout("fill, insets 0", "[250px!][" + arrowSize + "px!][250px!][" + arrowSize + "px!][750px::]", "[350px::]"));

        add(serverListPanel, "grow");

        add(new ArrowPanel(arrowDesigner), "grow");

        add(crawlerSettingsPanel, "grow");

        add(new ArrowPanel(arrowDesigner), "grow");

        add(crawlerResultsPanel, "grow");

        this.serverListPanel = serverListPanel;
    }

    public ServerListPanel getServerListPanel()
    {
        return serverListPanel;
    }

}