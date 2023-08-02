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

    private String strDate;
    private String strDays;
    private String strTime;
    private boolean boolAutoReport;
    public void changeQuartzTime(Optional<String> date, Optional<String> days, Optional<String> time, Optional<Boolean> autoReport) {

        String conExpression = "0 ";

        if (autoReport.isPresent()) {
            boolAutoReport = autoReport.get();

            if (autoReport.get()) {
                if (time.isPresent()) {
                    strTime = time.get();

                    String[] arrTime = strTime.split(":");

                    for (int i = arrTime.length - 1; i >= 0; i--) {
                        conExpression = conExpression + arrTime[i] + " ";
                    }
                }
                if (date.isPresent()) {

                    strDate = date.get();

                    if (strDate.equals("custom")) {
                        if (days.isPresent() && days.get().split(",").length == 7) {
                            strDate = "daily";
                        }
                    }
                    if (strDate.equals("daily")) {
                        conExpression = conExpression + "* * ?";
                    } else if (days.isPresent()) {
                        strDays = days.get();
                        conExpression = conExpression + "? * " + days.get();
                    }
                    quartzTask.changeCron(conExpression);

                } else {
                    quartzTask.stopTask();
                    System.out.println("stopped task");
                }
            }

        }else {
            quartzTask.stopTask();
            System.out.println("stopped task 2 ");

        }
    }
}
