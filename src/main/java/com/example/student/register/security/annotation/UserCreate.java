package com.example.student.register.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.annotation.Secured;

import static com.example.student.register.security.roleHierarchy.RolesForSecurity.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Secured(ROLES_PREFIX+USER_CREATE)
public @interface UserCreate {
}
