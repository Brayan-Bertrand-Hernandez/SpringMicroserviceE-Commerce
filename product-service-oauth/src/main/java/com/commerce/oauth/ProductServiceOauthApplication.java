package com.commerce.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EntityScan({ "com.commerce.commons.users.model" })
public class ProductServiceOauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceOauthApplication.class, args);
	}

}
