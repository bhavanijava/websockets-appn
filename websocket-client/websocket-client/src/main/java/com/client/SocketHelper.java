package com.client;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.server.Message;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class SocketHelper {

    private StompSession stompSession;

    public SocketHelper() throws IOException, InterruptedException, ExecutionException, StreamReadException, DatabindException {
        super();
    }

    public void openConnection() throws InterruptedException, ExecutionException {
        if (stompSession == null || !stompSession.isConnected()) {
            WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());

            String serverUri = "ws://localhost:7778/gs-guide-websocket";

            StompSessionHandler sessionHandler = new MyStompSessionHandler();
            try {
                stompSession = stompClient.connect(serverUri, sessionHandler).get();
                log.info("WebSocket STOMP connection established.");
            } catch (Exception e) {
                log.error("Error establishing WebSocket STOMP connection.", e);
            }
        }
    }

    public void sendMessage(Message message) throws InterruptedException, ExecutionException {
        openConnection();
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.send("/app/chat", message);
        } else {
            log.error("WebSocket STOMP connection is not established. Message not sent.");
        }
    }

    public class MyStompSessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            log.info("Server Connected ...");

            session.subscribe("/topic/messages", new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return Message.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    Message msg = (Message) payload;
                    //log.info("Received message: " +msg.getId() +" "+msg.getMessage());
                    System.out.println("id:"+msg.getId()+" : message:"+msg.getMessage());
                }
            });
        }
    }
}
