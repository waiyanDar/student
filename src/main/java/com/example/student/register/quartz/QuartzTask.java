package com.example.student.register.quartz;

import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.example.student.register.service.SmbService;

import lombok.Data;

@Component
@Data
public class QuartzTask {
	
	@Autowired
	private SmbService smbService;
	
	static String cronExpression = "0 0/5 * * * ?";
	
	private final TaskScheduler taskScheduler;
	private ScheduledFuture<?> scheduledFuture;
	
	public QuartzTask(TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
		rescheduleTask();
	}
	
	public void changeCron(String newCron) {
		cronExpression = newCron;
		rescheduleTask();
	}
	
	public void rescheduleTask() {
		if (scheduledFuture != null) {
			scheduledFuture.cancel(true);
		}
		TriggerTask triggerTask = new TriggerTask(this::executeTask, new CronTrigger(cronExpression));
		scheduledFuture = taskScheduler.schedule(triggerTask.getRunnable(), triggerTask.getTrigger());
	}

	public void stopTask(){
		if (scheduledFuture != null ) scheduledFuture.cancel(true);
	}
	
	public void executeTask() {
		smbService.writePdf();
	}

}
