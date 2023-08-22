package com.andresinho20049.authservice.controller;


import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andresinho20049.authservice.dto.StandardResponse;
import com.andresinho20049.authservice.models.User;
import com.andresinho20049.authservice.service.UserService;
import com.andresinho20049.authservice.utils.Constants;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/user")
@Api(value = "Usuário", description = "Controller usuários", tags= {"user"}, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Secured({ Constants.ROLE_ADMIN, Constants.ROLE_VIEW_USER })
	@GetMapping
	public ResponseEntity<?> findAll() {
		
		return ResponseEntity.ok(userService.findAll());
	}
	
	@Secured({ Constants.ROLE_ADMIN, Constants.ROLE_VIEW_USER })
	@GetMapping("/{id}")
	public ResponseEntity<?> findUserById(@PathVariable Long id) {
		
		return ResponseEntity.ok(userService.findById(id));
	}
    
	@Secured({ Constants.ROLE_ADMIN, Constants.ROLE_VIEW_USER })
	@GetMapping("/paginated")
	public ResponseEntity<?> findByPage(
			@RequestParam(value = "active", required = false) Boolean active,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size,
			@RequestParam(value = "order", defaultValue = "id") String order,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		
		return ResponseEntity.ok(userService.findByPage(active, page, size, order, direction));
	}
	
    @Secured({Constants.ROLE_ADMIN, Constants.ROLE_CREATE_USER})
    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user){
        userService.save(user);
        
        String msg = String.format("User %s registered successfully!", user.getName());
        
        return ResponseEntity
        		.status(HttpStatus.CREATED)
        		.body(new StandardResponse(HttpServletResponse.SC_OK, msg, System.currentTimeMillis()));
    }
    
    @Secured({Constants.ROLE_ADMIN, Constants.ROLE_UPDATE_USER})
    @PutMapping
    public ResponseEntity<?> update(@RequestBody User user){
        userService.update(user);
        
        String msg = String.format("User %s successfully updated!", user.getName());
        return ResponseEntity.ok(new StandardResponse(HttpServletResponse.SC_OK, msg, System.currentTimeMillis()));
    }
    
    @Secured({Constants.ROLE_ADMIN, Constants.ROLE_DISABLE_USER})
    @PatchMapping("/disabled/{id}")
    public ResponseEntity<?> disabled(@PathVariable Long id){
        userService.disabled(id);
        
        return ResponseEntity.ok(new StandardResponse(HttpServletResponse.SC_OK, "User successfully deactivated!", System.currentTimeMillis()));
    }

	@Secured({ Constants.ROLE_ADMIN, Constants.ROLE_DELETE_USER })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		userService.delete(id);
		return ResponseEntity.ok(new StandardResponse(HttpServletResponse.SC_OK, "User successfully deleted!", System.currentTimeMillis()));
	}
}
