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

import de.ep3.ftpc.model.Configuration;
import de.ep3.ftpc.view.designer.UIDesigner;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class PortalFrame extends JFrame
{

    private PortalMenu portalMenu;
    private PortalPanel portalPanel;

    public PortalFrame(PortalMenu portalMenu, PortalPanel portalPanel, PortalStatusBar portalStatusBar,
        Configuration config, UIDesigner uiDesigner)
    {
        setTitle(config.need("app.name") + " " + config.need("app.version"));
        setIconImages(uiDesigner.getDefaultAppIcons());

        setJMenuBar(portalMenu);

        setLayout(new MigLayout("fill, flowy", "", "[grow]unrelated[min!]"));

        add(portalPanel, "grow");
        add(portalStatusBar, "grow");

        this.portalMenu = portalMenu;
        this.portalPanel = portalPanel;
    }

    public PortalMenu getPortalMenu()
    {
        return portalMenu;
    }

    public PortalPanel getPortalPanel()
    {
        return portalPanel;
    }

}