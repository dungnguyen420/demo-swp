package com.example.swp.controller;


import com.example.swp.DTO.RegisterDTO;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("")
public class AuthController {

    @Autowired
    private IUserService userService;

    @GetMapping("/index")
    public String showLoginForm() {
        return "index";
    }

    @GetMapping("/auth/register")
    public String showRegisterForm(){
        return "auth/register";
    }


//    @PostMapping("/index")
//    public String loginUser(
//            @RequestParam("usernameOrEmail") String usernameOrEmail,
//            @RequestParam("password") String password,
//            HttpSession session,
//            Model model) {
//
//        boolean isAuthenticated = userService.authenticateUser(usernameOrEmail, password, session);
//
//        if (!isAuthenticated) {
//            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không chính xác, hoặc tài khoản bị khóa!");
//            return "index";
//        }
//
//        UserEntity user = userService.findByUserNameOrEmail(usernameOrEmail);
//
//        session.setAttribute("loggedInUser", user);
//
//        // ✅ In ra role để kiểm tra
//        System.out.println(">>> ROLE LOGIN: " + user.getRole());
//
//        String role = user.getRole().toString();
//
//        if (role.equalsIgnoreCase("ADMIN")) {
//            return "redirect:/auth/dashboard";
//        } else if (role.equalsIgnoreCase("MANAGER")) {
//            return "redirect:/products/list";
//        } else if (role.equalsIgnoreCase("TRAINER")) {
//            return "redirect:/home";
//        } else if (role.equalsIgnoreCase("MEMBER")) {
//            return "redirect:/home";
//        } else {
//            return "redirect:/home";
//        }
//        }

    @PostMapping("/auth/register")
    public String registerUser(@ModelAttribute RegisterDTO dto, Model model) {
        UserEntity newUser = userService.registerUser(dto);

        if (newUser == null) {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc email đã tồn tại!");
            return "auth/register";
        }

        model.addAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
        return "redirect:/login";
    }

}
    
