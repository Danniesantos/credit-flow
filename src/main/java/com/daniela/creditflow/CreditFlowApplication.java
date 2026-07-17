package com.daniela.creditflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class CreditFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditFlowApplication.class, args);
	}

}
