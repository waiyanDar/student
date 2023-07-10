package com.example.student.register.controller;

import java.time.LocalDate;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {

	@ExceptionHandler(Throwable.class)
	public String exception(Throwable throwable, Model model) {
		
		String msg = throwable != null ? throwable.getMessage() : "Unknown Error";
//		int status = 0;
//		if (msg.equals("Access is denied")) {
//			status = 403;
//		} else {
//			status = 500;
//		}

		
//		model.addAttribute("statusCode", status);
		model.addAttribute("msg", msg);
		model.addAttribute("loginDate", LocalDate.now().toString());

		return "error";
	}

}
