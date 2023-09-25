package com.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TestDataMessageExecutor {

	@Autowired
	private SocketHelper socketHelper;
	
	@Autowired
	private ObjectMapper objectMapper;

	public TestDataMessageExecutor(ObjectMapper objectMapper, SocketHelper socketHelper, String[] jsonFilePaths) {
		this.objectMapper = objectMapper;
		this.socketHelper = socketHelper;
	}

	
	@PostConstruct
	public void executeTestDataMessages() throws InterruptedException {
		try {
			String[] jsonFilePaths = {
					"src/main/resources/test.json",
					"src/main/resources/test_data_1.json",
					"src/main/resources/test_data_2.json",
					"src/main/resources/test_data_3.json",
					"src/main/resources/test_data_4.json"
			};
			int numThreads = jsonFilePaths.length;
			ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
			for (String jsonFilePath : jsonFilePaths) {
				TestDataMessageSender sender = new TestDataMessageSender(objectMapper, socketHelper, jsonFilePath);
				executorService.submit(sender);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
