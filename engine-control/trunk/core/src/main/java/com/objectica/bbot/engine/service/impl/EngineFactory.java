package com.objectica.bbot.engine.service.impl;

import com.objectica.bbot.engine.service.EngineController;
import org.apache.commons.configuration.ConfigurationException;
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
            PropertiesConfiguration configuration = new PropertiesConfiguration(EngineFactory.class.getClassLoader().getResource("engine.properties"));
            return createEngineController(configuration.getString("engine.port"), configuration.getInt("com.connectTimeout", 5000), configuration.getBoolean("test.mode", false));
        } catch (ConfigurationException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static EngineController createEngineController(String portName, int connectionTimeout, boolean testModel)
    {
        try
        {
            return new RxTxEngineController(portName, connectionTimeout, testModel);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
