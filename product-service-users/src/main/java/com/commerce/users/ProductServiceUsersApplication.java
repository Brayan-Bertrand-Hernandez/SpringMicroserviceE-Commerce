package com.commerce.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@EntityScan({"import com.commerce.commons.email.model", "import com.commerce.commons.email.factory"})
public class ProductServiceUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceUsersApplication.class, args);
	}

}
