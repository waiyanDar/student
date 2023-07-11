package com.example.student.register.controller;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {

	private static final Logger logger = Logger.getLogger(ErrorController.class.getName());

	@ExceptionHandler(Throwable.class)
	public String exception(Throwable throwable, Model model) {
		
		String msg = throwable != null ? throwable.getMessage() : "Unknown Error";
		model.addAttribute("msg", msg);
		model.addAttribute("loginDate", LocalDate.now().toString());
		logger.log(Level.SEVERE, msg);
		return "error";
	}

}
