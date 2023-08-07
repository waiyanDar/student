package com.example.student.register.quartz;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class CronExpressionBuilder {

    @Autowired
    private QuartzTask quartzTask;

    private String strDate;
    private String strDays;
    private String strTime;
    private boolean boolAutoReport;
    
    public String changeCronExpression(Optional<String> date, Optional<String> days, Optional<String> time, Optional<Boolean> autoReport) {

        String cronExpression = "0 ";

        if (autoReport.isPresent()) {
            boolAutoReport = autoReport.get();

            if (autoReport.get()) {
                if (time.isPresent()) {
                    strTime = time.get();

                    String[] arrTime = strTime.split(":");

                    for (int i = arrTime.length - 1; i >= 0; i--) {
                        cronExpression = cronExpression + arrTime[i] + " ";
                    }
                }
                if (date.isPresent()) {

                    strDate = date.get();

                    if (strDate.equals("custom")) {
                        if (days.isPresent() && days.get().split(",").length == 7) {
                            strDate = "daily";
                        }
                    }
                    
                    if(strDate.equals("achoc")) {
                    	
                    	cronExpression = cronExpression + "* * *";
                    	
                    }else if (strDate.equals("daily")) {
                    	
                        cronExpression = cronExpression + "* * ?";
                        
                    } else if (days.isPresent()) {
                    	
                        strDays = days.get();
                        cronExpression = cronExpression + "? * " + days.get();
                        
                    }

                } else {
                	boolAutoReport = false;
                }
            }

        }else {
        	boolAutoReport = false;

        }
        System.out.println(cronExpression);
        return cronExpression;
    }
    
}
