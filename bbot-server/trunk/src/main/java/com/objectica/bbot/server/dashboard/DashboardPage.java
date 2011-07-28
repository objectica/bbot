package com.objectica.bbot.server.dashboard;

import com.objectica.bbot.server.dashboard.controll.ControlPanel;
import org.apache.wicket.markup.html.WebPage;

/**
 * @author Serge Podgurskiy
 */
public class DashboardPage extends WebPage
{

    public DashboardPage()
    {
        add(new ControlPanel("controlPanel"));
    }
}
