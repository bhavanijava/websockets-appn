package com.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WebsocketServerApplication {
    
    @Autowired
    private RequestResponseStats requestResponseStats;

    public static void main(String[] args) {
        SpringApplication.run(WebsocketServerApplication.class, args);
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message handleMessage(Message message) {
        requestResponseStats.incrementTotalRequests();
        Message response = createResponse(message);
        requestResponseStats.incrementTotalResponses();
        return response;
    }

    private Message createResponse(Message originalMessage) {
        Message response = new Message();
        response.setId(originalMessage.getId());
        response.setMessage(originalMessage.getMessage());
        return response;
    }
}
