package com.vynqtalk.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;

@SpringBootApplication
@EnableScheduling
public class ServerApplication {

	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		SpringApplication.run(ServerApplication.class, args);
	}

}
