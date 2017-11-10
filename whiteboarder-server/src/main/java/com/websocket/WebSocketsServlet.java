package com.websocket;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.glassfish.grizzly.websockets.WebSocketEngine;

public class WebSocketsServlet extends HttpServlet {

    private WhiteboarderApplication app;

    @Override
    public void init(ServletConfig config) throws ServletException {
        app = new WhiteboarderApplication(null);
        WebSocketEngine.getEngine().register(config.getServletContext().getContextPath(), "/session", app);
    }

    @Override
    public void destroy() {
        WebSocketEngine.getEngine().unregister(app);
    }
}
