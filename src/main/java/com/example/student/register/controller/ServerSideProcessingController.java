package com.example.student.register.controller;

import java.util.List;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.student.register.dto.UserPaginationDto;
import com.example.student.register.entity.User;
import com.example.student.register.security.annotation.UserRead;
import com.example.student.register.service.UserService;


@RestController
public class ServerSideProcessingController {
	
    private final UserService userService;

    public ServerSideProcessingController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/serverSideProcessing")
    @UserRead
    public UserPaginationDto serverSideProcessing(@RequestParam("start") int start,
				                                 @RequestParam("length")int size,
				                                 @RequestParam("draw") int draw ,
				                                 @RequestParam("order[0][column]") int sortColIndex,
				                                 @RequestParam("order[0][dir]") String order,Model data ) {
    	
    		int page=0;
          
          int totalUsers = userService.findAllUser().size();
          
          if(start >1) {
        	  if(totalUsers % start == 0) {
            	  page = totalUsers / start;
              }else {
    			page = (totalUsers / start) ;
    		}
          }
          
          List<User> listUser  = userService.paginationUser(page, size);
                
          
          System.out.println(listUser.size());
          UserPaginationDto userPaginationDto = new UserPaginationDto();
          userPaginationDto.setData(listUser);
          userPaginationDto.setRecordsFiltered(totalUsers);
          userPaginationDto.setRecordTotal(totalUsers);
          userPaginationDto.setDraw(draw);
          
        return  userPaginationDto;
    }

}
