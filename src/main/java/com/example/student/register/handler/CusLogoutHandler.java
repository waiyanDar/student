//package com.example.student.register.handler;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//import org.springframework.stereotype.Component;
//
//import com.example.student.register.security.CustomAuthProvider;
//import com.example.student.register.service.InvalidJwtService;
//
//@Component
//public class CusLogoutHandler implements LogoutHandler{
//
//	@Autowired
//	private InvalidJwtService jwtService;
//	
//	@Override
//	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//		String token =  CustomAuthProvider.jwt;
//		System.out.println("from logout : " + token);
//		
//		jwtService.saveToken(token);
//	}
//	
//}
