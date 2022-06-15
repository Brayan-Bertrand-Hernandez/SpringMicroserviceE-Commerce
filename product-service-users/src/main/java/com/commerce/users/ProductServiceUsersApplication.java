package com.commerce.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EntityScan({"com.commerce.commons.users.model", "import com.commerce.commons.email.model", "import com.commerce.commons.email.factory"})
public class ProductServiceUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceUsersApplication.class, args);
	}

}
