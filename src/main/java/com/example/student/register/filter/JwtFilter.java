
package com.example.student.register.filter;

import static com.example.student.register.security.roleHierarchy.RolesForSecurity.ROLES_PREFIX;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.student.register.entity.User;
import com.example.student.register.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService;
	@Value("${jwt.signing.key}")
	private String signinKey;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		Cookie[] cookies = request.getCookies();

		String jwt = "";
		try {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("jwt")) {
					jwt = cookie.getValue();
				}
			}
		} catch (Exception e) {
			
		}
		SecretKey key = Keys.hmacShaKeyFor(signinKey.getBytes(StandardCharsets.UTF_8));

		if (jwt != null && !jwt.isEmpty()) {
			
			Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

			String userId = String.valueOf(claims.get("userId"));
			
			User user = userService.findUserByUserId(userId);
			
			List<GrantedAuthority> grantedAuthority = user.getRoles().stream()
					.map(r -> new SimpleGrantedAuthority(ROLES_PREFIX + r.getName())).collect(Collectors.toList());

			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null,grantedAuthority);
			
			SecurityContextHolder.getContext().setAuthentication(auth);
			
		} else {
			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getServletPath().equals("/login");
	}

}