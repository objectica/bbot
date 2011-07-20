package com.objectica.bbot.triggers;

import com.objectica.bbot.behavior.Behavior;
import com.objectica.bbot.behavior.BehaviorContext;
import com.objectica.bbot.exceptions.TriggerInitializationException;

import java.util.List;

/**
 * @author curly
 */
public abstract class Trigger implements Runnable{

    private Behavior behavior;
    private BehaviorContext context = new BehaviorContext();

    public void initialize() throws TriggerInitializationException
    {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public abstract void run();

    public void fireTriggerBehavior()
    {
        if(behavior!=null)
        {
            behavior.triggerFired(context);
        }
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }

    public void setContext(BehaviorContext context) {
        this.context = context;
    }

    public BehaviorContext getContext() {
        return context;
    }
}
