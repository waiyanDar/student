package com.example.student.register.config;

import com.example.student.register.filter.JwtFilter;
import com.example.student.register.security.CustomAuthProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig {

	@Autowired
	@Lazy
	private CustomAuthProvider customAuthProvider;
	
	@Autowired
	@Lazy
	private  RoleHierarchy roleHierarchy;
	
	@Autowired
	@Lazy
	private JwtFilter jwtfilter;

	@Bean
	public DefaultWebSecurityExpressionHandler expressionHandler() {
		DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
		handler.setRoleHierarchy(roleHierarchy);
		return handler;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.authenticationProvider(customAuthProvider);
		httpSecurity.addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class);
		httpSecurity.authorizeHttpRequests()
					.mvcMatchers("/bootstrap-5.0.2/**", "/test.css")
					.permitAll()
					.anyRequest()
					.authenticated()
					.and()
					.formLogin()
					.loginPage("/login")
					.failureUrl("/login-error")
					.permitAll().and()
					.logout()
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login")
					.deleteCookies("jwt")
					.permitAll();

		httpSecurity.csrf().disable();
		return httpSecurity.build();
	}



	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
