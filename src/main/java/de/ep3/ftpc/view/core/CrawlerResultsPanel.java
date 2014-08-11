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

package de.ep3.ftpc.view.core;

import de.ep3.ftpc.model.Crawler;
import de.ep3.ftpc.model.i18n.I18nManager;
import de.ep3.ftpc.view.designer.UIDesigner;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CrawlerResultsPanel extends JPanel implements ActionListener
{

    private I18nManager i18n;
    private UIDesigner uiDesigner;

    private JScrollPane scrollPane;
    private JPanel resultsPanel;

    private int animationDelay = 50;
    private Timer animationTimer;
    private Point animationObjectPosition;
    private Point animationObjectOffset;
    private int animationObjectDirection;
    private int animationObjectLength;
    private int animationObjectDepth;
    private int animationObjectSteps;

    private Crawler.Status crawlerStatus;

    public CrawlerResultsPanel(I18nManager i18n, UIDesigner uiDesigner)
    {
        setBorder(uiDesigner.getDefaultBorder());

        this.i18n = i18n;
        this.uiDesigner = uiDesigner;

        initialize();
        initializeAnimationObjects();
    }

    public void initialize()
    {
        removeAll();

        setLayout(new MigLayout("fill"));

        JLabel noResultsLabel = new JLabel(i18n.translate("portalNoCrawlerResults"));

        add(noResultsLabel, "align center");

        revalidate();
        repaint();
    }

    public JPanel initializeForResults()
    {
        removeAll();

        setLayout(new MigLayout("fill"));

        scrollPane = new JScrollPane();

        add(scrollPane, "grow");

        /* Setup scroll pane panel */

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new MigLayout("fillx, wrap 2, gap unrelated", "[sizegroup s][sizegroup s]"));

        /* Setup scroll pane */

        scrollPane.setViewportView(resultsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        revalidate();
        repaint();

        return resultsPanel;
    }

    public JScrollPane getScrollPane()
    {
        return scrollPane;
    }

    public JPanel getResultsPanel()
    {
        return resultsPanel;
    }

    private synchronized void initializeAnimationObjects()
    {
        if (animationTimer == null) {
            animationTimer = new Timer(animationDelay, this);
        }

        animationObjectLength = 48;
        animationObjectDepth = 4;
        animationObjectSteps = 8;

        animationObjectPosition = new Point(0, 0);
        animationObjectOffset = new Point(-animationObjectLength, 0);
        animationObjectDirection = 0;
    }

    public synchronized void setStatus(Crawler.Status status)
    {
        crawlerStatus = status;

        switch (crawlerStatus) {
            case IDLE:
                if (animationTimer.isRunning()) {
                    animationTimer.stop();

                    initializeAnimationObjects();
                }
                break;
            case RUNNING:
            case PAUSED:
                if (! animationTimer.isRunning()) {
                    animationTimer.start();
                }
                break;
        }

        repaint();
    }

    @Override
    protected void paintBorder(Graphics g)
    {
        super.paintBorder(g);

        if (crawlerStatus != null) {
            switch (crawlerStatus) {
                case RUNNING:
                case PAUSED:
                    g.setColor(uiDesigner.getDefaultBorderColor());

                    int x = animationObjectPosition.x - animationObjectLength + (animationObjectOffset.x * 2);
                    int y = animationObjectPosition.y - animationObjectDepth + (animationObjectOffset.y * 2);
                    int width = animationObjectLength * 2;
                    int height = animationObjectDepth * 2;

                    /* Render primary object */

                    g.fillRect(x, y, width, height);

                    /* Render mirrored object for free :D */

                    g.fillRect(
                        -x + getWidth() - animationObjectLength * 2,
                        -y + getHeight() - animationObjectDepth * 2,
                        width, height);

                    break;
            }
        }
    }

    @Override
    public synchronized void actionPerformed(ActionEvent e)
    {
        repaint();

        if (crawlerStatus != null) {
            switch (crawlerStatus) {
                case RUNNING:
                    int x = animationObjectPosition.x;
                    int y = animationObjectPosition.y;
                    int length = animationObjectLength;
                    int depth = animationObjectDepth;

                    int panelWidth = getWidth();
                    int panelHeight = getHeight();

                    float panelHalfWidth = panelWidth / 2.0F;
                    float panelHalfHeight = panelHeight / 2.0F;

                    /* Calculate speed */

                    float relativePositionX = (x - panelHalfWidth) / panelHalfWidth;
                    float relativePositionY = (y - panelHalfHeight) / panelHalfHeight;

                    float speedFactor = Math.abs(Math.abs(relativePositionX) - 1) + Math.abs(Math.abs(relativePositionY) - 1);

                    if (speedFactor <= 0.05F) {
                        speedFactor = 0.05F;
                    }

                    int speed = animationObjectSteps;

                    if (animationObjectDirection == 0) {
                        speed = Math.round(panelWidth / speed);
                    } else {
                        speed = Math.round(panelHeight / speed);
                    }

                    speed = Math.round(speed * speedFactor);

                    /* Move object ... */

                    if (x < panelWidth && y == 0) {
                        x = Math.min(x + speed, panelWidth);
                    } else if (x > 0 && y == panelHeight) {
                        x = Math.max(x - speed, 0);
                    } else if (x == panelWidth && y < panelHeight) {
                        y = Math.min(y + speed, panelHeight);
                    } else if (x == 0 && y > 0) {
                        y = Math.max(y - speed, 0);
                    } else {
                        initializeAnimationObjects();
                        break;
                    }

                    animationObjectPosition.x = x;
                    animationObjectPosition.y = y;

                    if (animationObjectDirection == 0) {
                        animationObjectOffset.x = Math.round(animationObjectLength * relativePositionX);
                        animationObjectOffset.y = 0;
                    } else {
                        animationObjectOffset.x = 0;
                        animationObjectOffset.y = Math.round(animationObjectDepth * relativePositionY);
                    }

                    if ((x == panelWidth && y == 0) ||
                        (x == panelWidth && y == panelHeight) ||
                        (x == 0 && y == panelHeight) ||
                        (x == 0 && y == 0)) {

                        animationObjectLength = depth;
                        animationObjectDepth = length;

                        animationObjectDirection = Math.abs(animationObjectDirection - 1);
                    }

                    break;
            }
        }
    }

}