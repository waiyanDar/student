package com.example.student.register.security.roleHierarchy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

import static com.example.student.register.security.roleHierarchy.RolesForSecurity.*;

@Configuration
public class RoleHierarchyConfig {

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchyImpl = new RoleHierarchyImpl();
        roleHierarchyImpl.setHierarchy(new RoleHierarchyBuilder().append(SUPER_ADMIN, ROLES_ADMIN)
        		.append(ROLES_ADMIN, USER_ADMIN)
                .append(USER_ADMIN, USER_CREATE)
                .append(USER_ADMIN, USER_DELETE)
                .append(USER_ADMIN, USER_UPDATE)
                .append(USER_ADMIN, USER_READ)

                .build());

        return roleHierarchyImpl;
    }
}

class RoleHierarchyBuilder {

    private StringBuilder stringBuilder = new StringBuilder();

    public RoleHierarchyBuilder append(String upLineRole, String downLineRole) {
        stringBuilder.append(String.format("ROLE_%s > ROLE_%s\n", upLineRole, downLineRole));
        return this;
    }

    public String build() {
        return this.stringBuilder.toString();
    }
}
