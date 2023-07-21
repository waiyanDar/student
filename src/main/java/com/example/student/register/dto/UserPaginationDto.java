package com.example.student.register.dto;

import java.util.List;
import com.example.student.register.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPaginationDto {

	public int draw;
	public int recordTotal;
	public int recordsFiltered;
	
	public List<User> data;
}
