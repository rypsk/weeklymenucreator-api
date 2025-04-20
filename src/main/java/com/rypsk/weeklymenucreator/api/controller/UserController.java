package com.rypsk.weeklymenucreator.api.controller;

import com.rypsk.weeklymenucreator.api.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "User management APIs")
public class UserController {

    private static final String ID = "id";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }




}
