package com.example.student.register.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.student.register.dao.UserDao;
import com.example.student.register.entity.User;

@Component
public class CustomAuthProvider implements AuthenticationProvider{

	
	private UserDao userDao;
	
	private PasswordEncoder passwordEncoder;
	
	public CustomAuthProvider(UserDao userDao,@Lazy PasswordEncoder passwordEncoder) {
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String inComeUserId = authentication.getName();
		String inComePassword= String.valueOf(authentication.getCredentials());
		
		User user = userDao.findUserByUserId(inComeUserId).get();
		
		String userId = user.getUserId();
		String password = user.getPassword();
		
		if(user != null && userId.equals(inComeUserId) && passwordEncoder.matches(inComePassword, password)) {
			return new UsernamePasswordAuthenticationToken(inComeUserId, inComePassword, Arrays.asList());
		}else {
			throw new BadCredentialsException("Something's wrong");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	
}
