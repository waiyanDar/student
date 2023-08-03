package com.example.student.register.controller;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.student.register.service.QuartzService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@Data
public class ErrorController {

    @Autowired
    private QuartzService quartzService;

    private static final Logger logger = Logger.getLogger(ErrorController.class.getName());

    @ExceptionHandler(Throwable.class)
    public String exception(Throwable throwable, Model model) {
    	
    	throwable.printStackTrace();

        String msg = throwable != null ? throwable.getMessage() : "Unknown Error";
        model.addAttribute("msg", msg);
        logger.log(Level.SEVERE, msg);
        return "error";
    }

    @ModelAttribute("loginDate")
    public String loginDate() {
        return LocalDate.now().toString();
    }

    @ModelAttribute("quartzDate")
    public String quartzDate(){
        return quartzService.getStrDate();
    }

    @ModelAttribute("quartzDay")
    public String quartzDay(){
        return quartzService.getStrDays();
    }

    @ModelAttribute("autoReport")
    public boolean isAutoReport(){
        return quartzService.isBoolAutoReport();
    }

    @ModelAttribute("quartzTime")
    public String quartzTime(){
        return quartzService.getStrTime();
    }
}
