/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gbc.mc.controller;

import com.gbc.mc.common.ClientSessionInfo;
import com.gbc.mc.common.DefinedName;
import com.gbc.mc.common.JsonParserUtil;
import com.gbc.mc.common.MessageType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 *
 * @author tamvh
 */
@WebSocket
public class NotifyController {
    
    private static final Logger logger = Logger.getLogger(NotifyController.class);
    private static final Gson gson = new Gson();
    private static final Map<String, ClientSessionInfo> _clientSessionMap = Collections.synchronizedMap(new LinkedHashMap<String, ClientSessionInfo>());
    
    @OnWebSocketConnect
    public void onConnect(org.eclipse.jetty.websocket.api.Session session) {
        
        
        Map<String, List<String>> params = session.getUpgradeRequest().getParameterMap();
        
        String sessionId = "";
        List<String> sessionIdList = params.get(DefinedName.SESSION_ID);
        if (sessionIdList == null || sessionIdList.isEmpty()) {
            session.close();
            return;
        } else {
            sessionId = sessionIdList.get(0);
        }
        
        List<HttpCookie> listCookie = new ArrayList<>();
        listCookie.add(new HttpCookie(DefinedName.SESSION_ID, sessionId));
        session.getUpgradeRequest().setCookies(listCookie);
        
        logger.info("NotifyController.onConnect: client sessionId = " + sessionId);
        
        ClientSessionInfo oldClient = _clientSessionMap.get(sessionId);
        System.out.println("Session id: " + sessionId);
        if (oldClient != null) {
            logger.info("NotifyController.onConnect: close old client sessionId = " + sessionId);
            oldClient.getSession().close();
        }
        
        _clientSessionMap.put(sessionId, new ClientSessionInfo(session, sessionId));
        session.setIdleTimeout(10*60*1000);
        
        return;        
    }
    
    @OnWebSocketClose
    public void onClose(org.eclipse.jetty.websocket.api.Session session, int status, String reason) {
        session.close();
        removeSession(session);
    }

    @OnWebSocketMessage
    public void onText(org.eclipse.jetty.websocket.api.Session session, String message) {
        logger.info("Received message:" + message);
        JsonObject jsonReq = JsonParserUtil.parseJsonObject(message);
        if(jsonReq != null && jsonReq.has("msg_type")) {
            if (jsonReq.get("msg_type").getAsInt() == MessageType.MSG_PING) {
               JsonObject jsonResp = new JsonObject();
               
               jsonResp.addProperty("msg_type", MessageType.MSG_PONG);
               jsonResp.addProperty("dt", "Hi! This is pong message response from server");
               String sendMsgPong = gson.toJson(jsonResp);               
               sendMessageToClient(session, sendMsgPong);               
            }            
        }
    }
    
    public void removeSession(org.eclipse.jetty.websocket.api.Session session) {
        List<HttpCookie> listCookie = session.getUpgradeRequest().getCookies();
        for (HttpCookie cookie : listCookie) {
            if (cookie.getName().compareToIgnoreCase(DefinedName.SESSION_ID) == 0) {
                _clientSessionMap.remove(cookie.getValue());
                break;
            }
        }
    }
    
    public static boolean sendMessageToClient(String sessionId, String data) {
        if (_clientSessionMap.containsKey(sessionId)) {
            ClientSessionInfo clientSession = _clientSessionMap.get(sessionId);
            if (clientSession != null) {
                try {
                    logger.info("NotifyController.sendMessageToClient: response to client sessionId = " + sessionId + ", data = " + data);
                    clientSession.getSession().getRemote().sendString(data);
                    return true;
                } catch (IOException ex) {
                    logger.error("NotifyController.sendMessageToClient: " + ex.getMessage(), ex);
                }
            }
        }        
        return false;
    }
    
    public static boolean sendMessageToClient(String data) {
        _clientSessionMap.forEach( (sessionId, sessionInfo) -> {
            try {
                sessionInfo.getSession().getRemote().sendString(data);
            } catch (IOException ex) {
                logger.error("NotifyController.sendMessageToClient: " + ex.getMessage(), ex);
            }
        });
        
        return true;
    }
    public static boolean sendMessageToClient(org.eclipse.jetty.websocket.api.Session session, String data) {
        try {
            session.getRemote().sendString(data);
        } catch (IOException ex) {
            logger.error("NotifyController.sendMessageToClientBySession: " + ex.getMessage(), ex);
        }
        return true;
    }
}
