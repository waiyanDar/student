package com.example.student.register.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.student.register.quartz.CronExpressionBuilder;
import com.example.student.register.quartz.QuartzTask;

import lombok.Data;

@Service
@Data
public class QuartzService {

    @Autowired
    private QuartzTask quartzTask;

    @Autowired
    private CronExpressionBuilder expressionBuilder;
    
    private String strDate;
    private String strDays;
    private String strTime;
    private boolean boolAutoReport;
    
    public void executeQuartz(Optional<String> date, Optional<String> days,
    						  Optional<String> time, Optional<Boolean> autoReport) {
    	
    	String cronExpression = expressionBuilder.changeCronExpression(date, days, time, autoReport);
    	
    	setDatas();
    	
    	if (boolAutoReport) {
    		quartzTask.changeCron(cronExpression);
		}else {
			quartzTask.stopTask();
		}
    	
    }
    
    public void setDatas() {
    	
    strDate = expressionBuilder.getStrDate();
    strDays = expressionBuilder.getStrDays();
    strTime = expressionBuilder.getStrTime();
    boolAutoReport = expressionBuilder.isBoolAutoReport();
    
    }
    
}
