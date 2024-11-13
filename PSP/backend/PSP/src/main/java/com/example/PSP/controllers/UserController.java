package com.example.PSP.controllers;

import com.example.PSP.models.User;
import com.example.PSP.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public List<User> getAll(){return userService.getAllUsers();}

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){return userService.getUserById(id);}

}
