package com.andresinho20049.authservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andresinho20049.authservice.service.RolesService;
import com.andresinho20049.authservice.utils.Constants;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/roles")
@Api(value = "Roles", description = "Controller de Regras", tags= {"roles"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class RolesController {
	
	@Autowired
	private RolesService roleService;
	
    @Secured({Constants.ROLE_ADMIN})
    @GetMapping
    public ResponseEntity<?> findAll(){

		return ResponseEntity.ok(roleService.findAll());
    }

    @Secured({Constants.ROLE_ADMIN})
    @GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {

		return ResponseEntity.ok(roleService.findById(id));
	}

    @Secured({Constants.ROLE_ADMIN})
    @GetMapping("find-name/{name}")
	public ResponseEntity<?> findByName(@PathVariable String name) {

		return ResponseEntity.ok(roleService.findByName(name));
	}

}
