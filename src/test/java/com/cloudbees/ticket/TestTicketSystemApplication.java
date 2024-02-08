package com.cloudbees.ticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestTicketSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(TicketSystemApplication::main).with(TestTicketSystemApplication.class).run(args);
	}

}
