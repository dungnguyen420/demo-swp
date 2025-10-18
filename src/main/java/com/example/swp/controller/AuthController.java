package com.example.swp.Controller;


import com.example.swp.DTO.RegisterDTO;
import com.example.swp.DTO.response.TFUResponse;
import com.example.swp.Entity.PackageEntity;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserRole;
import com.example.swp.Service.IUserService;
import com.example.swp.base.BaseAPIController;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@Controller
@RequestMapping("/auth")
public class AuthController extends BaseAPIController {

    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(){
        return "auth/register";
    }


    @PostMapping("/login")
    public String loginUser(
            @RequestParam("usernameOrEmail") String usernameOrEmail,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        boolean isAuthenticated = userService.authenticateUser(usernameOrEmail, password, session);

        if (!isAuthenticated) {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không chính xác, hoặc tài khoản bị khóa!");
            return "auth/login";
        }

        UserEntity user = userService.findByUserNameOrEmail(usernameOrEmail);

        if (user.getRole().name().equalsIgnoreCase("ADMIN")) {
            return "redirect:/auth/dashBoard";
        } else {
            return "redirect:/home";
        }
    }
    // 🟨 Xử lý đăng ký
    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisterDTO dto, Model model) {
        UserEntity newUser = userService.registerUser(dto);

        if (newUser == null) {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc email đã tồn tại!");
            return "auth/register";
        }

        model.addAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/auth/login";
    }

}

    
