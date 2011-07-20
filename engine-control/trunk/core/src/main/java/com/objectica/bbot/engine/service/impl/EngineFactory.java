package com.objectica.bbot.engine.service.impl;

import com.objectica.bbot.engine.service.EngineController;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sergey Podgurskiy
 */
public class EngineFactory
{
    private static Logger logger = LoggerFactory.getLogger(EngineFactory.class);

    public static EngineController createEngineController()
    {
        try
        {
            EngineController controller;
            PropertiesConfiguration configuration = new PropertiesConfiguration(EngineFactory.class.getClassLoader().getResource("engine.properties"));
            controller = new RxTxEngineController(configuration.getString("engine.port"), configuration.getInt("com.connectTimeout", 5000), configuration.getBoolean("test.mode", false));
            return controller;
        } catch (Exception e)
        {
            logger.error("Error in loading engine.properties", e);
            throw new RuntimeException(e);
        }
    }
}
