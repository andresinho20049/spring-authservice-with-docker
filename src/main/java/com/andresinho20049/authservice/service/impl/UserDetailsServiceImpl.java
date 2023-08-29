package com.andresinho20049.authservice.service.impl;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.andresinho20049.authservice.models.User;
import com.andresinho20049.authservice.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author André Carlos <https://github.com/andresinho20049>
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	Optional<User> optUser = Optional.empty();
    	try {
    		optUser = userRepository.findByEmailAndActive(username, true);			
		} catch (Exception e) {
			log.error(e.getMessage());
		}
    	
    	User user = optUser
    			.orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found!", username)));

        return new UserRepositoryUserDetails(user);
    }

    private final static class UserRepositoryUserDetails extends User implements UserDetails {

        private static final long serialVersionUID = 1L;

        private UserRepositoryUserDetails(User user) {
            super(user);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return getRoles();
        }

        @Override
        public String getUsername() {
            return this.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return this.isActive();
        }

        @Override
        public String getPassword() {
            return super.getPassword();
        }

    }

}
