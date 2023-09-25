package com.client;


import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDataMessageSender extends Thread {

	@Autowired
    private ObjectMapper objectMapper;
	
    private final String jsonFilePath;
    
    @Autowired
    private SocketHelper socketHelper;
    
    
    public TestDataMessageSender(ObjectMapper objectMapper, SocketHelper socketHelper, String jsonFilePath) {
        this.objectMapper = objectMapper;
        this.socketHelper = socketHelper;
        this.jsonFilePath = jsonFilePath;
    }

    @Override
    public void run() {
        try {
            log.info("Thread started. jsonFilePath: {}", jsonFilePath);

            if (socketHelper == null) {
                log.error("StompSession is null. Aborting thread execution. jsonFilePath: {}", jsonFilePath);
                return;
            }

            File file = new File(jsonFilePath);
            JsonNode jsonNode = objectMapper.readTree(file);

            if (jsonNode.isArray()) {
                List<Message> messageList = objectMapper.readValue(jsonNode.toString(), new TypeReference<List<Message>>() {});

                for (Message message : messageList) {
                    sendingMessage(message);
                }
            } else {
                String id = jsonNode.get("id").asText();
                String text = jsonNode.get("message").asText();

                Message message = new Message();
                message.setId(String.valueOf(id)); // Convert the integer id to a string if necessary
                message.setMessage(text);

                sendingMessage(message);
            }
        } catch (Exception e) {
            log.error("Exception occurred during thread execution. jsonFilePath: {}", jsonFilePath, e);
        } finally {
            log.info("Thread completed. jsonFilePath: {}", jsonFilePath);
        }
    }

    private void sendingMessage(Message message) throws InterruptedException, ExecutionException {
        socketHelper.sendMessage(message);
    }
}