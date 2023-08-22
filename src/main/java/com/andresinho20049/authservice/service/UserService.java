package com.andresinho20049.authservice.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.andresinho20049.authservice.models.User;

public interface UserService {
	
	void save(User user);	//C
	
	List<User> findAll();	//R
	
	void update(User user);	//U
	
	void delete(Long id);	//D
	
	void disabled(Long id);
	
	User findById(Long id);
	
	User findByEmail(String email, Boolean active);
	
	Page<User> findByPage(Boolean active, Integer page, Integer size, String order, String direction);
	
}
