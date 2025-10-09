package com.example.swp.controller;

import com.example.swp.Entity.UserEntity;
import com.example.swp.Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("auth")
public class DashBoardController {

    @Autowired
    private IUserService userService;

    @GetMapping("/dashBoard")
    public String showDashBoard(Model model){
        List<UserEntity> users = userService.getAllUsers();
        model.addAllAttributes("users",users);
        return "auth/dashBoard";
    }

}
