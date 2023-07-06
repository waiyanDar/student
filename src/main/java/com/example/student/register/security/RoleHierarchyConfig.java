package com.example.student.register.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

import com.example.student.register.util.RoleHierarchyBuilder;
import static com.example.student.register.util.RolesForSecurity.*;

@Configuration
public class RoleHierarchyConfig {

	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchyImpl = new RoleHierarchyImpl();
		roleHierarchyImpl.setHierarchy(new RoleHierarchyBuilder().append(ROLES_ADMIN, USER).build());
		
		return roleHierarchyImpl;
	}
}
