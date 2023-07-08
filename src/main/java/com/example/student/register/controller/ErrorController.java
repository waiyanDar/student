package com.example.student.register.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.student.register.util.UserNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ErrorController {

	@ExceptionHandler(Throwable.class)
	public String exception(Throwable throwable, HttpServletResponse response, RedirectAttributes attributes, Model model) {
		
		String msg = throwable != null ? throwable.getMessage() : "Unknown Error";
		int status = 0;
		if (msg.equals("Access is denied")) {
			status = 403;
		} else {
			status = 500;
		}

		
		model.addAttribute("statusCode", status);
		model.addAttribute("msg", msg);

		return "error";
	}

}
