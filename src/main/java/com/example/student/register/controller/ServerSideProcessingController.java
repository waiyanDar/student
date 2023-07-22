package com.example.student.register.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.example.student.register.dto.UserUpdateDto;
import com.example.student.register.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.student.register.dto.UserPaginationDto;
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
                                                  @RequestParam("length") int size,
                                                  @RequestParam("draw") int draw,
                                                  @RequestParam("order[0][column]") int sortColIndex,
                                                  @RequestParam("order[0][dir]") String order,
                                                  @RequestParam("search[value]") String search) {

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
            listUser = userService.searchUserAscSorting(search, page, size, column)
                    .stream().map(UserUpdateDto::form).collect(Collectors.toList());

        } else {
            listUser = userService.searchUserDescSorting(search, page, size, column)
                    .stream().map(UserUpdateDto::form).collect(Collectors.toList());
        }

        UserPaginationDto userPaginationDto = new UserPaginationDto();
        userPaginationDto.setData(listUser);
        userPaginationDto.setRecordsFiltered(totalUsers);
        userPaginationDto.setRecordTotal(totalUsers);
        userPaginationDto.setDraw(draw);

        return userPaginationDto;
    }

}
