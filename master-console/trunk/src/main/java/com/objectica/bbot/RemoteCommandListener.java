package com.objectica.bbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.*;

/**
 * @author curly
 */
public class RemoteCommandListener implements KeyListener
{
    private static final Logger logger = LoggerFactory.getLogger(RemoteCommandListener.class);
    private MasterConsole console;

    public RemoteCommandListener(MasterConsole console)
    {
        this.console = console;
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        //stub
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        console.displayInfo("pressed: " + getKeyDesctiption(e));
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        console.displayInfo("released: " + getKeyDesctiption(e));
    }

    public String getKeyDesctiption(KeyEvent e)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("code: ").append(e.getKeyCode());
        builder.append(" char: ").append(e.getKeyChar());
        return builder.toString();
    }

    public void execute(String[] args)
    {
        //ApplicationContext context =  new ClassPathXmlApplicationContext("spring/sp-master-console.xml");
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                MasterConsole console = new MasterConsole();
            }
        });


        int i = 0;
        while (i < 100)
        {
            i++;
            try
            {
                InetAddress addr = InetAddress.getByName("localhost");
                int port = 8080;
                SocketAddress sockaddr = new InetSocketAddress(addr, port);
                Socket sock = new Socket();
                int timeoutMs = 2000;
                sock.connect(sockaddr, timeoutMs);

                BufferedWriter w = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                //RemoteCommand commandRight = new RemoteCommand("command", "goRight");
                w.write("{}");
                w.flush();
                w.close();

                Thread.sleep(10000);

            } catch (UnknownHostException e)
            {
                logger.error("", e);
            } catch (SocketTimeoutException e)
            {
                logger.error("", e);
            } catch (IOException e)
            {
                logger.error("", e);
            } catch (InterruptedException e)
            {

            }

        }
    }


}
