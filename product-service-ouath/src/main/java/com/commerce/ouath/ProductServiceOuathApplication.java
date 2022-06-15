package com.commerce.ouath;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EntityScan({"com.commerce.commons.users.model"})
public class ProductServiceOuathApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceOuathApplication.class, args);
	}

}
