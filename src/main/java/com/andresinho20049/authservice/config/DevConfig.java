package com.andresinho20049.authservice.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.andresinho20049.authservice.enums.RolesEnum;
import com.andresinho20049.authservice.exceptions.ProjectException;
import com.andresinho20049.authservice.models.Roles;
import com.andresinho20049.authservice.models.User;
import com.andresinho20049.authservice.service.RolesService;
import com.andresinho20049.authservice.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Profile({ "dev" })
@ComponentScan({ "com.andresinho20049.authservice.controller" })
public class DevConfig {

	@Autowired
	private UserService userService;

	@Autowired
	private RolesService rolesService;

	@Bean
	void startDatabase() {
		log.debug("################ Start with Profile Dev ################");

		Roles roles = null;
		for (RolesEnum role : RolesEnum.values()) {
			String roleName = role.name();
			try {
				rolesService.findByName(roleName);
			} catch (ProjectException e) {
				roles = new Roles(roleName);
				rolesService.save(roles);
			}
		}

		String supportMail = "admin@email.com";
		try {
			userService.findByEmail(supportMail, true);
		} catch (ProjectException e) {
			roles = rolesService.findByName(RolesEnum.ROLE_ADMIN.name());
			User user = new User("Admin", supportMail, "strongPassword@1234", Arrays.asList(roles));
			userService.save(user);
		}
	}
}
