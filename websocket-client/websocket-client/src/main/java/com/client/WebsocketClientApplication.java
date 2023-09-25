package com.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebsocketClientApplication implements CommandLineRunner{

	@Autowired
	private SocketHelper socketHelper;
	
	public static void main(String[] args) {
		SpringApplication.run(WebsocketClientApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		socketHelper.openConnection();
		
	}

}
