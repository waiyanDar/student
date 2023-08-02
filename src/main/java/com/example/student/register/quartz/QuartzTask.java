package com.example.student.register.quartz;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
public class QuartzTask {
	
	String cronExpression = "0 0/1 * * * ?";
	
	private final TaskScheduler taskScheduler;
	private ScheduledFuture<?> scheduledFuture;
	
	public QuartzTask(TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
		rescheduleTask();
	}
	
	public void changeCron(String newCron) {
		this.cronExpression = newCron;
		System.out.println(cronExpression);
		rescheduleTask();
	}
	
	public void rescheduleTask() {
		if (scheduledFuture != null) {
			scheduledFuture.cancel(true);
		}
		TriggerTask triggerTask = new TriggerTask(this::everyOneMinute, new CronTrigger(cronExpression));
		scheduledFuture = taskScheduler.schedule(triggerTask.getRunnable(), triggerTask.getTrigger());
	}

	public void stopTask(){
		if (scheduledFuture != null ) scheduledFuture.cancel(true);
	}
	
	public void everyOneMinute() {
		System.out.println("testing : " + new Date());
	}

}
