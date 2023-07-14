package com.example.student.register.controller;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class ErrorController {

	private static final Logger logger = Logger.getLogger(ErrorController.class.getName());

	@ExceptionHandler(ExpiredJwtException.class)
	public String handleDateExpiry(ExpiredJwtException dt, Model model) {
		logger.log(Level.SEVERE, dt.getMessage());
		System.out.println("expire");
		model.addAttribute("expiry", true);

		return "error";
	}

	@ExceptionHandler(AccessDeniedException.class)
	public String handleAccessDeniedException(AccessDeniedException ex, Model model) {

		logger.log(Level.SEVERE, ex.getMessage());

		System.out.println("access denied");
		
		model.addAttribute("msg", "Access Denied: " + ex.getMessage());

		return "error";
	}

//	@ExceptionHandler(NullPointerException.class)
//	public String handleNullPointerException(HttpServletRequest request, AccessDeniedException ex, Model model) {
//
//		logger.log(Level.SEVERE, ex.getMessage());
//		
//		System.out.println("null pointer");
//
//		model.addAttribute("msg", "No value present: " + ex.getMessage());
//
//		return "error";
//	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public String handleNoSuchElementException(NoSuchElementException ex, Model model) {
	    logger.log(Level.SEVERE, ex.getMessage());
	    System.out.println("no such element");

	    model.addAttribute("msg", "No Such Element Exception : " + ex.getMessage());

	    return "error";
	}


	@ExceptionHandler(Throwable.class)
	public String exception(Throwable throwable, Model model) {

		String msg = throwable != null ? throwable.getMessage() : "Unknown Error";
		model.addAttribute("msg", msg);
		
		System.out.println("throwable");
//		model.addAttribute("loginDate", LocalDate.now().toString());
		logger.log(Level.SEVERE, msg);
		return "error";
	}

	@ModelAttribute("loginDate")
	public String loginDate() {
		return LocalDate.now().toString();
	}

}
