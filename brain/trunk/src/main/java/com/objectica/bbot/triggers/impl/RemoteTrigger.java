package com.objectica.bbot.triggers.impl;

import com.objectica.bbot.behavior.Behavior;
import com.objectica.bbot.behavior.BehaviorContext;
import com.objectica.bbot.triggers.Trigger;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ServerSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * @author curly
 */
public class RemoteTrigger extends Trigger {

    Logger logger = LoggerFactory.getLogger(RemoteTrigger.class);
    public static final int SERVER_SOCKET_PORT = 8080;
    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public void run() {
        while (true) {
            try {
                ServerSocket server = ServerSocketFactory.getDefault().createServerSocket(SERVER_SOCKET_PORT);
                Socket client = server.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String someJson = in.readLine();
                if(someJson!=null)
                {
                    RemoteCommand command = mapper.readValue(someJson,RemoteCommand.class);
                    getContext().getDataMap().put("command",command);
                    // TODO need to think abt some better way to pass context
                    fireTriggerBehavior();
                }
            } catch (IOException e) {
                logger.error("Exception listening server socket on port "+SERVER_SOCKET_PORT,e);
                break;
            }
        }
    }

}
