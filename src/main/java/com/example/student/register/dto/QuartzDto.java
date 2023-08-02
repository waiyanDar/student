package com.example.student.register.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Data
public class QuartzDto {

	private Optional<Boolean> autoReport;
	private Optional<String> date;
	private Optional<String> days;
	private Optional<String> time;

}
