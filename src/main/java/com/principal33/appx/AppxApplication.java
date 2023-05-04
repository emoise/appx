package com.principal33.appx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AppxApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppxApplication.class, args);
	}

}
