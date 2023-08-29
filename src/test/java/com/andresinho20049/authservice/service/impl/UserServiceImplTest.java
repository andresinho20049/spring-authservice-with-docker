package com.andresinho20049.authservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.andresinho20049.authservice.exceptions.ProjectException;
import com.andresinho20049.authservice.models.Roles;
import com.andresinho20049.authservice.models.User;
import com.andresinho20049.authservice.repository.UserRepository;
import com.andresinho20049.authservice.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class UserServiceImplTest {
	
	private UserService userService;
	
	@Mock
	private UserRepository userRepositoryMock;
	
	@Captor
	private ArgumentCaptor<User> captorUser;
	
	@BeforeEach
	private void beforeEach() {
		MockitoAnnotations.openMocks(this);
		this.userService = new UserServiceImpl(userRepositoryMock, new BCryptPasswordEncoder());
	}
	
	@Test
	void saveSuccess() {

		String name = "Fake";
		String email = "andre.andresinho2009@hotmail.com";
		String password = "strongPass@1234";
		Roles roles = new Roles("Test");
		
		User user = new User(name, email, password, Arrays.asList(roles));
		userService.save(user);
		
		Mockito.verify(userRepositoryMock).save(captorUser.capture());
		User userCaptured = captorUser.getValue();
		
		assertTrue(userCaptured.isActive());
		assertTrue(userCaptured.isUpdatePassword());
		assertNotEquals(password, userCaptured.getPassword());
		
	}
	
	@Test
	void saveExpectingError() {
		
		String emailExist = "alreadyExist@email.com";
		Mockito.when(userRepositoryMock.existsByEmail(emailExist)).thenReturn(true);
		
		List<User> listUsers = new ArrayList<>();
		listUsers.add(null);
		listUsers.add(new User(null, null, null, null));
		listUsers.add(new User("Contain only name", null, null, null));
		listUsers.add(new User("Name", "email not valid", "12345678", null));
		listUsers.add(new User("Email is empty", "", "12345678", null));
		listUsers.add(new User("Name", emailExist, "12345678", null));
		listUsers.add(new User("Not Contain Password", "email@email.com", null, null));
		listUsers.add(new User("Weak Password", "email@email.com", "12345678", null));
		
		for (User user : listUsers) {
			try {
				
				userService.save(user);
				Mockito.verifyNoInteractions(userRepositoryMock.save(any()));
				
			} catch (Exception e) {
				assertInstanceOf(ProjectException.class, e);
				log.debug("Error expected: " + e.getMessage());
			}
		}
		
	}
	
	

	@Test
	void findAllTest() {
		
		List<User> listMock = findAllMock();
		
		Mockito.when(userRepositoryMock.findAll()).thenReturn(listMock);
		
		List<User> users = userService.findAll();
		
		assertEquals(listMock, users);
		
	}
	
	private List<User> findAllMock() {
		
		List<User> users = new ArrayList<>();
		
		User user = null;
		Roles roles = new Roles("Test");
		for (int i = 0; i < 10; i++) {
			String name = String.format("User %s", i);
			String email = String.format("user%s@email.com", i);
			String password = UUID.randomUUID().toString();
			
			
			user = new User(name, email, password, Arrays.asList(roles));
			users.add(user);
		}
		
		return users;
	}
	
	@Test
	void findByEmailWhenExist() {

		String name = "Fake";
		String email = "emailfake@email.com";
		String password = UUID.randomUUID().toString();
		Roles roles = new Roles("Test");
		
		User user = new User(name, email, password, Arrays.asList(roles));
		
		Mockito.when(userRepositoryMock.findByEmailAndActive(anyString(), anyBoolean()))
			.thenReturn(Optional.of(user));
		
		User userReturned = userService.findByEmail(email, true);
		
		assertEquals(user, userReturned);
		
	}
	
	@Test
	void findByEmailWhenNotExist() {
		
		String email = "emailfake@email.com";

		try {
			
			userService.findByEmail(email, true);
			
		} catch (Exception e) {
			assertInstanceOf(ProjectException.class, e);
			log.debug("findByEmailWhenNotExist[Error expected]: " + e.getMessage());
		}
		
	}

}
