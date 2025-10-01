package com.expenshare.controller;

import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.model.dto.user.UserDto;
import com.expenshare.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.validation.Valid;

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Get("/{userId}")
    UserDto getUser(Long userId){
        return userService.getUserByID(userId);
    }

    @Post
    HttpResponse<UserDto> createUser(@Body @Valid CreateUserRequest userRequest) {
        UserDto newUser = userService.createUser(userRequest);
        return HttpResponse.created(newUser);
    }
}
