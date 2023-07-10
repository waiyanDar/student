package com.example.student.register.security.roleHierarchy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

import com.example.student.register.security.roleHierarchy.RoleHierarchyBuilder;
import static com.example.student.register.security.roleHierarchy.RolesForSecurity.*;

@Configuration
public class RoleHierarchyConfig {

	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchyImpl = new RoleHierarchyImpl();
		roleHierarchyImpl.setHierarchy(new RoleHierarchyBuilder().append(ROLES_ADMIN, USER_ADMIN)
																 .append(USER_ADMIN, USER_CREATE)
																 .append(USER_ADMIN, USER_DELETE)
																 .append(USER_ADMIN, USER_UPDATE)
																 .append(USER_ADMIN, USER_READ)

																 .build());
		
		return roleHierarchyImpl;
	}
}
