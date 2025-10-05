package com.expenshare.controller;

import com.expenshare.model.dto.group.GroupDto;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/api/users")
@Tag(name = "Users", description = "Operations related to users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Get("/{userId}")
    @Operation(
            summary = "Get an Existing User",
            description = "Get an Existing User using user's ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User is found successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class)
            )
    )
    HttpResponse<UserDto> getUser(Long userId){
        return HttpResponse.ok(userService.getUserByID(userId));
    }

    @Post
    @Operation(
            summary = "Create a new User",
            description = "Creates an User and returns the created User object."
    )
    @ApiResponse(
            responseCode = "201",
            description = "User successfully created",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class)
            )
    )
    HttpResponse<UserDto> createUser(@Body CreateUserRequest userRequest) {
        UserDto newUser = userService.createUser(userRequest);
        return HttpResponse.created(newUser);
    }
}
