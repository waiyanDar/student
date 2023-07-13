package com.example.student.register.security;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.student.register.entity.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class CustomAuthProvider implements AuthenticationProvider {

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static String jwt;

	/*
	 * public CustomAuthProvider(@Lazy UserService userService, @Lazy
	 * PasswordEncoder passwordEncoder, HttpServletResponse response) {
	 * this.userService = userService; this.passwordEncoder = passwordEncoder;
	 * this.response = response; }
	 */

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String inComeUserId = authentication.getName();
		String inComePassword = String.valueOf(authentication.getCredentials());
		String userId = "";
		String password = "";
		List<Role> roles = new ArrayList<>();

		try {
//			User user = userDao.findUserByUserId(inComeUserId).get();
			User user = userService.findUserByUserId(inComeUserId);
			userId = user.getUserId();
			password = user.getPassword();
			roles = user.getRoles();
		} catch (NoSuchElementException e) {
			throw new BadCredentialsException("User id is wrong", e);
		}

		List<GrantedAuthority> grantedAuthority = roles.stream()
				.map(r -> new SimpleGrantedAuthority(ROLES_PREFIX + r.getName())).collect(Collectors.toList());

		if (userId.equals(inComeUserId) && passwordEncoder.matches(inComePassword, password)) {
			jwt = generateJwtToken(userId);
//			HttpSession session = request.getSession();
//			session.setAttribute("jwt", jwt);
			Cookie jwtCookie = new Cookie("jwt", jwt);
			jwtCookie.setMaxAge(1800);
			jwtCookie.setPath("/");
			response.addCookie(jwtCookie);
			return new UsernamePasswordAuthenticationToken(inComeUserId, inComePassword, grantedAuthority);
		} else {
			throw new BadCredentialsException("Something's wrong");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	@Value("${jwt.signing.key}")
	private String signinKey;

	private String generateJwtToken(String userId) {
		int expirationInSec = 1800;

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + (expirationInSec*1000));

		SecretKey key = Keys.hmacShaKeyFor(signinKey.getBytes(StandardCharsets.UTF_8));

		String jwt = Jwts.builder().setClaims(Collections.singletonMap("userId", userId)).setIssuedAt(now)
				.setExpiration(expiryDate).signWith(key).compact();
		System.out.println("from auth provider : " + jwt);
		return jwt;
	}

}
