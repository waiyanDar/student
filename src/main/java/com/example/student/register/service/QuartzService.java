package com.example.student.register.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.student.register.quartz.QuartzTask;

import lombok.Data;

@Service
@Data
public class QuartzService {

	@Autowired
	private QuartzTask quartzTask;

	public void changeQuartzTime(String date, Optional<String> days, String time, boolean autoReport) {
		String conExpression = "0 ";
		String [] arrTime = time.split(":");
		
		for (int i = arrTime.length-1; i >= 0; i--) {
			conExpression = conExpression + arrTime[i] + " ";
		}
		
		if(date.equals("custom")) {
			if (days.get().split(",").length == 7) {
				date = "daily";
			}
		}
		
		if (date.equals("daily")) {
			conExpression = conExpression + "* * ?";
		}else {
			conExpression = conExpression + "? * " +days.get();
		}
		
		System.out.println(conExpression);
		
	}
}
