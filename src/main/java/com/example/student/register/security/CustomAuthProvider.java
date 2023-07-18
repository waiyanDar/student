package com.example.student.register.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.*;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.example.student.register.entity.Role;

import com.example.student.register.holder.SecretKeyHolder;
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
import com.example.student.register.generator.PublicPrivateKeyGenerator;
import com.example.student.register.service.UserService;

import static com.example.student.register.security.roleHierarchy.RolesForSecurity.*;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private SecretKeyHolder keyHolder;

	@Autowired
	private UserService userService;

	@Autowired
	private PublicPrivateKeyGenerator publicPrivateKeyGenerator;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static String jwt;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		try {
			decrypt(authentication.getCredentials().toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		String inComeUserId = authentication.getName();
		String inComePassword = String.valueOf(authentication.getCredentials());
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
//			keyHolder.setSecretKey(encodedKey);

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

	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	private static final String INIT_VECTOR = "YourInitializationVector";

	public static String decryptPassword(String encryptedPassword, String decryptionKey) {
		try {
			IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
			SecretKeySpec secretKeySpec = new SecretKeySpec(decryptionKey.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);

			byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
			return new String(decryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * public String decryptedPassword(String encryptedPsw) {
	 * 
	 * String privateKey = publicPrivateKeyGenerator.getPrivateKey();
	 * 
	 * byte[] decodedKey = Base64.getDecoder().decode(privateKey);
	 * 
	 * byte[] encryptedBytes = Base64.getDecoder().decode(encryptedPsw);
	 * System.out.println(encryptedBytes.toString());
	 * 
	 * SecretKeySpec secretKeySpec = new SecretKeySpec(decodedKey, "AES");
	 * 
	 * Cipher cipher = null; try { cipher =
	 * Cipher.getInstance("AES/ECB/PKCS5Padding");
	 * 
	 * cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
	 * 
	 * byte[] decryptedData = cipher.doFinal(encryptedBytes);
	 * 
	 * String decryptedPassword = new String(decryptedData, StandardCharsets.UTF_8);
	 * 
	 * System.out.println(decryptedPassword);
	 * 
	 * return decryptedPassword; } catch (Exception e) { e.printStackTrace(); }
	 * return null; }
	 */
	
	 private static final String ENCRYPTION_ALGORITHM = "AES";
	 
	 public String decrypt(String encryptPassword) throws Exception {
		 String encryptedPassword = encryptPassword;

	        byte[] encryptedPasswordBytes = Base64.getDecoder().decode(encryptedPassword);

	        System.out.println("1");
	        byte[] privateKeyBytes = publicPrivateKeyGenerator.getPrivateKey().getBytes(StandardCharsets.UTF_8); // Replace with your private key
	        SecretKey privateKey = new SecretKeySpec(privateKeyBytes, ENCRYPTION_ALGORITHM);
	        System.out.println("2");
	        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
	        cipher.init(Cipher.DECRYPT_MODE, privateKey);
	        System.out.println("3");
	        byte[] decryptedPasswordBytes = cipher.doFinal(encryptedPasswordBytes);
	        System.out.println("4");
	        String decryptedPassword = new String(decryptedPasswordBytes, StandardCharsets.UTF_8);
	        System.out.println("5");
	        System.out.println("Decrypted password: " + decryptedPassword);

	        return decryptedPassword;
	    }

}
