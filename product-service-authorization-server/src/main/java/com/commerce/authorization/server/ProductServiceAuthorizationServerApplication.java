package com.commerce.authorization.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@EntityScan({ "com.commerce.commons.users.model" })
public class ProductServiceAuthorizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceAuthorizationServerApplication.class, args);
	}

}
