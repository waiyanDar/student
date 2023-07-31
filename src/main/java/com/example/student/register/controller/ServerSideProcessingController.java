package com.example.student.register.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.example.student.register.dto.UserUpdateDto;
import com.example.student.register.entity.Student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.student.register.dto.UserPaginationDto;
import com.example.student.register.dto.StudentPaginationDto;
import com.example.student.register.security.annotation.Admin;
import com.example.student.register.security.annotation.UserRead;
import com.example.student.register.service.StudentService;
import com.example.student.register.service.UserService;

@RestController
//@Aspect
public class ServerSideProcessingController {

	private final UserService userService;
	private final StudentService studentService;

	public ServerSideProcessingController(UserService userService, StudentService studentService) {
		this.studentService = studentService;
		this.userService = userService;

	}

	@GetMapping("/serverSideProcessingForUser")
	@UserRead
	public UserPaginationDto serverSideProcessingForUser(@RequestParam("start") int start, @RequestParam("length") int size,
			@RequestParam("draw") int draw, @RequestParam("order[0][column]") int sortColIndex,
			@RequestParam("order[0][dir]") String order, @RequestParam("search[value]") String search) {

		int totalUsers = userService.findAllUser().size();

		int page = start / size;

		List<UserUpdateDto> listUser = null;
		String column = "";
		if (sortColIndex == 0) {
			column = "userId";
		} else {
			column = "username";
		}

		if (order.equals("asc")) {
			listUser = userService.searchUserAscSorting(search, page, size, column).stream().map(UserUpdateDto::form)
					.collect(Collectors.toList());

		} else {
			listUser = userService.searchUserDescSorting(search, page, size, column).stream().map(UserUpdateDto::form)
					.collect(Collectors.toList());
		}

		UserPaginationDto userPaginationDto = new UserPaginationDto();
		userPaginationDto.setData(listUser);
		userPaginationDto.setRecordsFiltered(totalUsers);
		userPaginationDto.setRecordTotal(totalUsers);
		userPaginationDto.setDraw(draw);

		return userPaginationDto;
	}

	@GetMapping("/serverSideProcessingForStudent")
	@Admin
	public StudentPaginationDto serverSideProcessingForStudent(@RequestParam("start") int start,
																@RequestParam("length") int length,
																@RequestParam("draw") int draw,
																@RequestParam("order[0][column]") int sortColIndex,
																@RequestParam("order[0][dir]") String order,
																@RequestParam("search[value]") String search) {

		int totalStudents = studentService.findAllStudent().size();

		int page = start / length;

		List<Student> listStudent = null;
		String column = "";
		if (sortColIndex == 0) {
			column = "studentId";
		} else if (sortColIndex == 1) {
			column = "name";
		} else {
			column = "studentId";
		}
		

		if (order.equals("asc")) {
			listStudent = studentService.searchStudentWithAsc(page, length, search, column);
		} else {
			listStudent = studentService.searchStudentWithDesc(page, length, search, column);
		}

		StudentPaginationDto studentPaginationDto = new StudentPaginationDto();
		studentPaginationDto.setDraw(draw);
		studentPaginationDto.setData(listStudent);
		studentPaginationDto.setRecordTotal(totalStudents);
		studentPaginationDto.setRecordsFiltered(totalStudents);
		
		return studentPaginationDto;

	}

}
