package com.example.student.register.security;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.example.student.register.entity.Role;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.student.register.entity.User;
import com.example.student.register.service.UserService;

import static com.example.student.register.security.roleHierarchy.RolesForSecurity.*;

@Component
public class CustomAuthProvider implements AuthenticationProvider{

		
	private UserService userService;
	
	private PasswordEncoder passwordEncoder;
	
	public CustomAuthProvider(@Lazy UserService userService,@Lazy PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String inComeUserId = authentication.getName();
		String inComePassword= String.valueOf(authentication.getCredentials());
		String userId = "";
		String password = "";
		List<Role> roles = new ArrayList<>();

		try {
//			User user = userDao.findUserByUserId(inComeUserId).get();
			User user = userService.findUserByUserId(inComeUserId);
			userId = user.getUserId();
			password = user.getPassword();
			roles = user.getRoles();
		}catch (NoSuchElementException e){
			throw new BadCredentialsException("User id is wrong", e);
		}

		List<GrantedAuthority> grantedAuthority = roles.stream()
				.map(r -> new SimpleGrantedAuthority(ROLES_PREFIX+r.getName())).collect(Collectors.toList());
		
		if( userId.equals(inComeUserId) && passwordEncoder.matches(inComePassword, password)) {
			return new UsernamePasswordAuthenticationToken(inComeUserId, inComePassword, grantedAuthority);
		}else {
			throw new BadCredentialsException("Something's wrong");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
