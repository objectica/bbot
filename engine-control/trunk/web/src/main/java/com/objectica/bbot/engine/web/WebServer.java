package com.objectica.bbot.engine.web;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class WebServer {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        Context root = new Context(server, "/", Context.SESSIONS);

        ServletContainer jersey = new ServletContainer();

        ServletHolder servletHolder = new ServletHolder(jersey);
        servletHolder.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
        servletHolder.setInitParameter("com.sun.jersey.config.property.packages", "com.objectica.bbot.engine.web");

        root.addServlet(servletHolder, "/*");
        server.start();
    }
}
