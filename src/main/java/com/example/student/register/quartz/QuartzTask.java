package com.example.student.register.quartz;

import java.util.Date;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
public class QuartzTask {
	
	String cronExpression = "0 0/1 * * * ?";
	
	private final TaskScheduler taskScheduler;
	
	public QuartzTask(TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
	}
	
	public void changeCron(String newCron) {
		this.cronExpression = newCron;
	}
	
	public void rescheduleTask() {
		TriggerTask triggerTask = new TriggerTask(this::everyOneMinute, new CronTrigger(cronExpression));
		taskScheduler.schedule(triggerTask.getRunnable(), triggerTask.getTrigger());
	}
	
//	@Scheduled(cron = cronExpression)
	public void everyOneMinute() {
		System.out.println("testing : " + new Date());
	}

}
