package com.example.student.register.security;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

public class GlobalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

	private RoleHierarchy roleHierarchy;
	
	public GlobalMethodSecurityConfig(RoleHierarchy roleHierarchy) {
		this.roleHierarchy = roleHierarchy;
	}
}
