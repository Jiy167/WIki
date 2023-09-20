package com.jiyuan.wiki.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@Component
@ServerEndpoint("/ws/{token}")
public class WebSocketServer {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * One token per client
     */
    private String token = "";

    private static HashMap<String, Session> map = new HashMap<>();

    /**
     * connect successful
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        map.put(token, session);
        this.token = token;
        LOG.info("There are new connections: token: {}, session id: {}, current number of connections: {}", token, session.getId(), map.size());
    }

    /**
     * connect closed
     */
    @OnClose
    public void onClose(Session session) {
        map.remove(this.token);
        LOG.info("The connection is closed, token: {}, session id: {}, Current number of connections: {}", this.token, session.getId(), map.size());
    }

    /**
     * get message
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        LOG.info("get message: {}，content: {}", token, message);
    }

    /**
     * connection error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        LOG.error("connection error", error);
    }

    /**
     * Group message
     */
    public void sendInfo(String message) {
        for (String token : map.keySet()) {
            Session session = map.get(token);
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                LOG.error("Push message failed: {}, content: {}", token, message);
            }
            LOG.info("Push message: {}, content: {}", token, message);
        }
    }

}
