package com.utar.uhauction.component;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint(value="/imserver/{username}")
@Component
public class WebSocketServer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    //record amount of current connection
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    //method for connect successfully
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessionMap.put(username, session);
        log.info("New user joined, username: {}, current online users: {}", username, sessionMap.size());
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        result.set("users", array);
        for (Object key : sessionMap.keySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("username", key);
            array.add(jsonObject);
        }
        // Result: [{"username": "zhang"}, {"username": "admin"}]
        sendAllMessage(JSONUtil.toJsonStr(result)); // Send the result to all clients
    }

    /**
     * Method to be called when a connection is closed
     */
    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessionMap.remove(username);
        log.info("A connection is closed, removed session of username: {}, current online users: {}", username, sessionMap.size());
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("username") String username) {
        log.info("Server received message from user {}: {}", username, message);
        JSONObject obj = JSONUtil.parseObj(message);
        String toUsername = obj.getStr("to"); // 'to' indicates the user to whom the message is sent, e.g., admin
        String text = obj.getStr("text"); // 'text' is the content of the message, e.g., hello
        Session toSession = sessionMap.get(toUsername); // Get the session of the user based on the username
        if (toSession != null) {
            // If the recipient exists, organize the message content and send it as a text
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("from", username); // From zhang
            jsonObject.set("text", text); // Text content is hello
            this.sendMessage(jsonObject.toString(), toSession);
            log.info("Message sent to user {}: {}", toUsername, jsonObject.toString());
        } else {
            log.info("Failed to send message, session not found for user {}", toUsername);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("An error occurred");
        error.printStackTrace();
    }

    /**
     * Send a message from the server to a specific client
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("Server sends message [{}] to client {}", message, toSession.getId());
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("Failed to send message to client", e);
        }
    }

    /**
     * Send a message from the server to all clients
     */
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("Server sends message [{}] to client {}", message, session.getId());
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("Failed to send message to all clients", e);
        }
    }

}
