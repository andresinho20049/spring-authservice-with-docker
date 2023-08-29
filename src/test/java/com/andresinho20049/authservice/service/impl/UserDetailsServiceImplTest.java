package com.andresinho20049.authservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.andresinho20049.authservice.models.User;
import com.andresinho20049.authservice.repository.UserRepository;

class UserDetailsServiceImplTest {
	
	private UserDetailsService userDetailsService;
	
	@Mock
	private UserRepository userRepositoryMock;
	
	@Captor
	private ArgumentCaptor<User> captorUser;
	
	@BeforeEach
	private void beforeEach() {
		MockitoAnnotations.openMocks(this);
		this.userDetailsService = new UserDetailsServiceImpl(userRepositoryMock);
	}

	@Test
	void ifRepositoryThrowException() {
		
		Mockito.when(userRepositoryMock.findByEmailAndActive(anyString(), anyBoolean()))
			.thenThrow(RuntimeException.class);
		
		try {
			User user = (User) userDetailsService.loadUserByUsername(anyString());
			assertEquals(null, user);
		} catch (Exception e) {
			assertInstanceOf(UsernameNotFoundException.class, e);
		}
		
	}

	@Test
	void ifRepositoryReturnNull() {
		
		Mockito.when(userRepositoryMock.findByEmailAndActive(anyString(), anyBoolean()))
			.thenReturn(null);
		
		try {
			User user = (User) userDetailsService.loadUserByUsername(anyString());
			assertEquals(null, user);
		} catch (Exception e) {
			assertInstanceOf(UsernameNotFoundException.class, e);
		}
		
	}

}
