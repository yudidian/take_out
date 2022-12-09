package com.dhy.websocket;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

//注册成组件
@Component
//定义websocket服务器端，它的功能主要是将目前的类定义成一个websocket服务器端。注解的值将被用于监听用户连接的终端访问URL地址
@ServerEndpoint("/websocket")
//如果不想每次都写private  final Logger logger = LoggerFactory.getLogger(当前类名.class); 可以用注解@Slf4j;可以直接调用log.info
@Slf4j
public class WebSocket {
    //实例一个session，这个session是websocket的session
    private Session session;

    private int count = 0;
    private String userId;
    private static final CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    //前端请求时一个websocket时
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        Map<String, String> map = new HashMap<>();
        String queryString = session.getQueryString();
        String[] query = queryString.split("&");
        for (String s : query) {
            String[] keyAndValue = s.split("=");
            map.put(keyAndValue[0], keyAndValue[1]);
        }
        String userId = map.get("userId");
        this.userId = userId;
        webSocketSet.add(this);
        try {
            webSocketSet.forEach(item -> {
                if (Objects.equals(item.userId, userId)) {
                    item.count++;
                }
                log.info("用户ID: {}， count: {}, userId: {}", item.userId, item.count, userId);
                if (item.count >= 2) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("flag", false);
                    hashMap.put("msg", "用户重复登录");
                    String jsonString = JSONObject.toJSONString(hashMap);
                    item.sendMessage(jsonString);
                    item.count = 1;
                    throw new RuntimeException();
                }
            });
        } catch (RuntimeException ignored) {
            this.onClose();
        }
    }

    //前端关闭时一个websocket时
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
    }

    //前端向后端发送消息
    @OnMessage
    public void onMessage(String message) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String userId = jsonObject.getString("userId");
    }

    //新增一个方法用于主动向客户端发送消息
    public void sendAllMessage(String message) {
        for (WebSocket webSocket : webSocketSet) {
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //新增一个方法用于主动向客户端发送消息
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
