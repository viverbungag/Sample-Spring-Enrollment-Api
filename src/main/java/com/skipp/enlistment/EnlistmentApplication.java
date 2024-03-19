package com.skipp.enlistment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
// TODO What annotation enables transaction management?
// TODO What annotation enables security annotations?
public class EnlistmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnlistmentApplication.class, args);
	}

}
