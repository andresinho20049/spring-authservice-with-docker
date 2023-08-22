package com.andresinho20049.authservice.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.andresinho20049.authservice.dto.StandardResponse;
import com.andresinho20049.authservice.models.User;
import com.andresinho20049.authservice.utils.Constants;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/oauth")
@Api(value = "Security", description = "Controlled session", tags = {
		"security" }, produces = MediaType.APPLICATION_JSON_VALUE)
public class SecurityController {

	@Autowired
	private ConsumerTokenServices consumerTokenServices;

	@PostMapping(value = "/revoke-token")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null) {
			String tokenValue = authHeader.replace("Bearer", "").trim();
			Boolean isRevoke = consumerTokenServices.revokeToken(tokenValue);

			return ResponseEntity.ok(new StandardResponse(HttpStatus.OK.hashCode(),
					String.format("Revoke token %s", isRevoke), System.currentTimeMillis()));
		}

		return ResponseEntity.accepted()
				.body(new StandardResponse(HttpStatus.OK.hashCode(), "revoked token", System.currentTimeMillis()));
	}

	@ResponseBody
	@Secured({ Constants.ROLE_ADMIN })
	@GetMapping("/user-auth")
	public User getUserLogged() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
