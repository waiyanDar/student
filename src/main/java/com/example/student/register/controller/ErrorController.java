package com.example.student.register.controller;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {

	private static final Logger logger = Logger.getLogger(ErrorController.class.getName());
	
	@ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(HttpServletRequest request, AccessDeniedException ex, Model model) {

	 model.addAttribute("msg", "Access Denied: " + ex.getMessage());
	 
	 return "error";
    }
	
	@ExceptionHandler(NullPointerException.class)
    public String handleNullPointerException(HttpServletRequest request, AccessDeniedException ex, Model model) {

	 model.addAttribute("msg", "No value present: " + ex.getMessage());
	 
	 return "error";
    }

	@ExceptionHandler(Throwable.class)
	public String exception(Throwable throwable, Model model) {
		
		String msg = throwable != null ? throwable.getMessage() : "Unknown Error";
		model.addAttribute("msg", msg);
		model.addAttribute("loginDate", LocalDate.now().toString());
		logger.log(Level.SEVERE, msg);
		return "error";
	}
	 

}
