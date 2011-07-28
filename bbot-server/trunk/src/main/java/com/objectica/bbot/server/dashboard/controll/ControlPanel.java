package com.objectica.bbot.server.dashboard.controll;

import com.objectica.bbot.server.service.EngineControlService;
import com.objectica.bbot.server.service.impl.EngineControlServiceImpl;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Serge Podgurskiy
 */
public class ControlPanel extends Panel
{
    private static final Logger logger = LoggerFactory.getLogger(ControlPanel.class);
    private FeedbackPanel feedback;

    public ControlPanel(String id)
    {
        super(id);
        feedback = new FeedbackPanel("feedback");
        add(feedback.setOutputMarkupId(true));

        add(new EngineArrow("up")
        {
            @Override
            public void doAction() throws Exception
            {
                EngineControlService engineControlService = getEngineService();
                engineControlService.sendCommand(0, 1, (byte) 10);
                engineControlService.sendCommand(1, 1, (byte) 10);
            }
        });
        add(new EngineArrow("down")
        {
            @Override
            public void doAction() throws Exception
            {
                EngineControlService engineControlService = getEngineService();
                engineControlService.sendCommand(0, 0, (byte) 10);
                engineControlService.sendCommand(1, 0, (byte) 10);
            }
        });
        add(new EngineArrow("left")
        {
            @Override
            public void doAction() throws Exception
            {
                EngineControlService engineControlService = getEngineService();
                engineControlService.sendCommand(0, 1, (byte) 10);
                engineControlService.sendCommand(1, 0, (byte) 10);
            }
        });
        add(new EngineArrow("right")
        {
            @Override
            public void doAction() throws Exception
            {
                EngineControlService engineControlService = getEngineService();
                engineControlService.sendCommand(0, 0, (byte) 10);
                engineControlService.sendCommand(1, 1, (byte) 10);
            }
        });
    }

    protected EngineControlService getEngineService()
    {
        String serverName = ((javax.servlet.http.HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest()).getServerName();
        String fullPath = "http://" + serverName + "/engine";
        return new EngineControlServiceImpl(fullPath);
    }

    public abstract class EngineArrow extends AjaxLink
    {

        public EngineArrow(String id)
        {
            super(id);
        }

        @Override
        public void onClick(AjaxRequestTarget target)
        {
            try
            {
                doAction();
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            Thread.sleep(100);
                            EngineControlService engineControlService = getEngineService();
                            engineControlService.sendCommand(0, 2, (byte) 0);
                            engineControlService.sendCommand(0, 2, (byte) 0);
                        } catch (Exception e)
                        {
                            logger.error("Exception in engine stop command", e);
                        }
                    }
                }).start();
            } catch (Exception e)
            {
                logger.error("Exception in execute engine command", e);
                feedback.error(e.getMessage());
                target.add(feedback);
            }
        }

        public abstract void doAction() throws Exception;
    }

}
