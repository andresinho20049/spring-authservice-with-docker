package com.andresinho20049.authservice.service;

import java.util.List;

import com.andresinho20049.authservice.models.Roles;

public interface RolesService {
	
	void save(Roles roles); 	//C
	
	List<Roles> findAll();		//R
	
	void update(Roles roles);	//U
	
	void delete(Roles roles);	//D
	
	Roles findById(Long id);
	
	Roles findByName(String nameRole);

}
