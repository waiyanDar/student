package com.example.student.register.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig {

	private CustomAuthProvider customAuthProvider;

	public SecurityConfig(CustomAuthProvider customAuthProvider) {
		this.customAuthProvider = customAuthProvider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.authenticationProvider(customAuthProvider);

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
					.permitAll();

		httpSecurity.csrf().disable();
		return httpSecurity.build();
	}

	/*
	 * @Bean public SecurityFilterChain securityFilterChain(HttpSecurity
	 * httpSecurity) throws Exception {
	 * httpSecurity.authenticationProvider(customAuthProvider);
	 * 
	 * httpSecurity .authorizeRequests() .mvcMatchers("/bootstrap-5.0.2/**",
	 * "/test.css").permitAll() .anyRequest().authenticated() .and() .formLogin()
	 * .loginPage("/login") .failureUrl("/login-error") .permitAll() .and()
	 * .logout() .logoutUrl("/logout") .logoutSuccessUrl("/login") .permitAll();
	 * 
	 * httpSecurity.csrf().disable();
	 * 
	 * return httpSecurity.build(); }
	 */

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
