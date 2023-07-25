package com.example.student.register.security;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.example.student.register.entity.Role;

import com.example.student.register.holder.Holder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
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
	private Holder keyHolder;

	@Autowired
	private UserService userService;


	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private DecryptPassword decryptPassword;

	public static String jwt;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String test1 = authentication.getCredentials().toString();

		String inComeUserId = authentication.getName();
		String inComePassword =decryptPassword.decryptPassword(test1);;

		System.out.println("decrypt : "+inComePassword);
		
		String userId = "";
		String password = "";
		List<Role> roles = new ArrayList<>();

		try {
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

			Cookie jwtCookie = new Cookie("jwt", jwt);
			jwtCookie.setMaxAge(18000);
			jwtCookie.setPath("/");
			response.addCookie(jwtCookie);

			keyHolder.userSecretKey.put(userId, encodedKey);
//			keyHolder.setPrivateKey(null);

			return new UsernamePasswordAuthenticationToken(inComeUserId, inComePassword, grantedAuthority);

		} else {
			throw new BadCredentialsException("Something's wrong");
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	public static String encodedKey;

	private String generateJwtToken(String userId) {
		try {
			generateSecretKey();
		} catch (NoSuchAlgorithmException e) {

		}
		int expirationInSec = 1800;

		SecretKey key = Keys.hmacShaKeyFor(encodedKey.getBytes(StandardCharsets.UTF_8));

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + (expirationInSec * 1000));

		String jwt = Jwts.builder().setClaims(Collections.singletonMap("userId", userId)).setIssuedAt(now)
				.setExpiration(expiryDate).signWith(key).compact();
		return jwt;
	}

	private void generateSecretKey() throws NoSuchAlgorithmException {

		KeyGenerator keyGenerator = KeyGenerator.getInstance("HMACSHA512");
		SecretKey secretKey = keyGenerator.generateKey();
		encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
	}
}
