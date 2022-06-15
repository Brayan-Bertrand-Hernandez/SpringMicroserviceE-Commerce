package com.commerce.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@EntityScan({"import com.commerce.commons.email.model", "import com.commerce.commons.email.factory"})
public class ProductServiceEmailServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceEmailServerApplication.class, args);
	}

}
