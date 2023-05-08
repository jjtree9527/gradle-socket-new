package top.inson.boot.server;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jingjitree
 * @description
 * @date 2023/4/20 10:42
 */
@Slf4j
@ServerEndpoint(value = "/websocket/server")
@Component
public class WebSocketServer {
    /**
     * 存储每一个客户端会话信息的线程安全的集合
     */
    private static final CopyOnWriteArraySet<Session> SESSIONS = new CopyOnWriteArraySet<>();

    /**
     * 使用线程安全的计数器，记录在线数
     */
    private static final AtomicInteger ONLINE_COUNTER = new AtomicInteger(0);


    /**
     * 连接成功时使用的方法
     * @param session
     */
    @OnOpen
    public void onOpen(Session session){
        //存储会话信息
        SESSIONS.add(session);
        //在线数加1
        int cnt = ONLINE_COUNTER.incrementAndGet();

        log.info("有新连接加入,当前连接数cnt：{}, ", cnt);
    }

    @OnClose
    public void onClose(Session session){
        //在线数减1
        int cnt = ONLINE_COUNTER.decrementAndGet();
        log.info("有一连接关闭,当前连接数cnt：{}", cnt);
        //移除会话信息
        SESSIONS.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session){
        Map<String, List<String>> parameterMap = session.getRequestParameterMap();
        String queryStr = session.getQueryString();
        log.info("参数内容：parameterMap:{}, queryStr: {}", parameterMap, queryStr);
        log.info("收到客户端消息：{},", message);
        this.sendMessage(session, "我已收到消息了, 你看到了吗");
    }

    @OnError
    public void onError(Session session, Throwable error){

        log.warn("sessionId: {},发生错误,{}", session.getId(), error.getMessage());
    }

    /**
     * 发送消息
     */
    @SneakyThrows
    public void sendMessage(Session session, String message){

        session.getBasicRemote().sendText(message);
    }

    /**
     * 群发消息
     */
    public void sendMessage(String message){

        SESSIONS.forEach(session -> {
            if (session.isOpen()) {
                this.sendMessage(session, message);
            }
        });
    }

    /**
     * 给指定客户端发消息
     */
    public void sendMessage(String sessionId, String message){
        Session session = SESSIONS.stream().filter(s -> sessionId.equals(s.getId())).findFirst().orElseGet(null);
        if (session != null) {
            this.sendMessage(session, message);
        }
    }

}
