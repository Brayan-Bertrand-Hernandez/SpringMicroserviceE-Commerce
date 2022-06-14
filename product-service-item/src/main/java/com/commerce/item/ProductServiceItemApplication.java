package com.commerce.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EntityScan({"import com.commerce.commons.model"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class ProductServiceItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceItemApplication.class, args);
	}

}
