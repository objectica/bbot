package com.objectica.bbot.server.system;

import com.objectica.bbot.server.dashboard.DashboardPage;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * @author Serge Podgurskiy
 */
public class WicketApplication extends WebApplication
{

    @Override
    protected void init()
    {
        super.init();
        mountPage("/dashboard", DashboardPage.class);
    }

    @Override
    public Class<? extends Page> getHomePage()
    {
        return DashboardPage.class;
    }
}
