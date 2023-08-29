package com.andresinho20049.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile({ "prod" })
@ComponentScan({ "com.andresinho20049.authservice.controller" })
public class ProdConfig {

	@Bean
	void startDatabase() {
		log.debug("################ Start with Profile Prod ################");
	}
}
