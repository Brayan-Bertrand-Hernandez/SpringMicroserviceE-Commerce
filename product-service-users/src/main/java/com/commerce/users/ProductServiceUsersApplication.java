package com.commerce.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
@EntityScan({"import com.commerce.commons.email.factory"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class ProductServiceUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceUsersApplication.class, args);
	}

}
