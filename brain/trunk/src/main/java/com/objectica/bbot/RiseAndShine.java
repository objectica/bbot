package com.objectica.bbot;

import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author curly
 */
public class RiseAndShine {

    public static void main(String[] args) {
        ApplicationContext context =  new ClassPathXmlApplicationContext("spring/sp-brain.xml");

        BotBrain bot = (BotBrain) context.getBean("brain");
        bot.wakeUp();
    }


}
