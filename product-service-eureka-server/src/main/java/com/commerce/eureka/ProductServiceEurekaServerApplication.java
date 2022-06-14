package com.commerce.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ProductServiceEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceEurekaServerApplication.class, args);
	}

}
