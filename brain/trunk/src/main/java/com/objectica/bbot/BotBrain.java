package com.objectica.bbot;

import com.objectica.bbot.exceptions.TriggerInitializationException;
import com.objectica.bbot.triggers.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * @author curly
 */
public class BotBrain {

    private Logger logger = LoggerFactory.getLogger(BotBrain.class);
    private List<Trigger> triggers;

    public void wakeUp() {
        logger.info("Initializing triggers...");
        for (Trigger trigger : triggers) {
            try {
                trigger.initialize();
            } catch (TriggerInitializationException e) {
                logger.error("Exception while trying to initialize trigger "+trigger.getClass().getName(),e);
            }
        }
    }

    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }
}
