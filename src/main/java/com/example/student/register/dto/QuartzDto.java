package com.example.student.register.dto;

import lombok.Data;

@Data
public class QuartzDto {
	
	private boolean autoReport;
	private boolean editDaily;
	private boolean monday;
	private boolean tuesday;
	private boolean wednesday;
	private boolean thursday;
	private boolean friday;
	private boolean satursday;
	private boolean sunday;
	
	private String time;
}
