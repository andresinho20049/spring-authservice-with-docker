package com.andresinho20049.authservice.service.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.andresinho20049.authservice.exceptions.ProjectException;
import com.andresinho20049.authservice.models.User;
import com.andresinho20049.authservice.repository.UserRepository;
import com.andresinho20049.authservice.service.UserService;
import com.andresinho20049.authservice.utils.Pagination;
import com.andresinho20049.authservice.utils.RegexValidateService;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	private PasswordEncoder passwordEncoder;
	
	private RegexValidateService regexValidate;
	
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.regexValidate = new RegexValidateService();
	}

	@Override
	public void save(User user) {

		if (user == null)
			throw new ProjectException("User is null");

		if (user.getName() == null || user.getName().isEmpty())
			throw new ProjectException("Name is required");

		if (user.getEmail() == null || user.getEmail().isEmpty())
			throw new ProjectException("E-mail is required");
		
		// check if email is valid 
		String regexEmail = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
		if (!regexValidate.matcher(user.getEmail(), regexEmail))
			throw new ProjectException("Email is not valid");
		

		if (userRepository.existsByEmail(user.getEmail()))
			throw new ProjectException("Email is already registered");

		if (user.getPassword() == null || user.getPassword().isEmpty())
			throw new ProjectException("Password is required");
		
		// Must be at least one numeric character
		// Must be at least one lowercase character
		// Must have at least one uppercase character
		// Must have at least one special symbol between !@#$%^&*
		// Password length must be between 8 and 20
		String regexPassword = "^(?=.*\\d)(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
		if (!regexValidate.matcher(user.getPassword(), regexPassword))
			throw new ProjectException(
					"Weak password! Must contain uppercase, lowercase, numerical value, "
					+ "special characters and at least 8 characters");

		String password = passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
		
		user.setActive(true);
		user.setUpdatePassword(true);

		userRepository.save(user);

	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public void update(User user) {
		User updated = this.findById(user.getId());

		if (user.getName() != null && !user.getName().isEmpty())
			updated.setName(user.getName());

		if (user.getPassword() != null && !user.getPassword().isEmpty())
			updated.setPassword(passwordEncoder.encode(user.getPassword()));

		if (user.getRoles() != null && !user.getRoles().isEmpty())
			updated.setRoles(user.getRoles());

		userRepository.save(updated);

	}

	@Override
	public void delete(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public void disabled(Long id) {
		User user = this.findById(id);
		
		user.setActive(false);
		userRepository.save(user);
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ProjectException(String.format("User %s not found", id)));
	}

	@Override
	public User findByEmail(String email, Boolean active) {

		if (active != null)
			return userRepository.findByEmailAndActive(email, active)
					.orElseThrow(() -> new ProjectException(String.format("Email %s not found on active users", email)));

		return userRepository.findByEmail(email)
				.orElseThrow(() -> new ProjectException(String.format("E-mail %s not found", email)));
	}

	@Override
	public Page<User> findByPage(Boolean active, Integer page, Integer size, String order, String direction) {
		PageRequest pageRequest = Pagination.getPageRequest(page, size, order, direction);

		if (active != null)
			return userRepository.findByActive(active, pageRequest);

		return userRepository.findAll(pageRequest);
	}

}
