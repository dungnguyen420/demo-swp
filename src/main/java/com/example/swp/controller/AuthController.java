package com.example.swp.controller;

import com.example.swp.Config.JwtUtil;
import com.example.swp.DTO.AuthRequest;
import com.example.swp.DTO.AuthResponse;
import com.example.swp.DTO.RegisterDTO;
import com.example.swp.DTO.response.TFUResponse;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Service.IUserService;
import com.example.swp.Service.impl.CustomUserDetails;
import com.example.swp.base.BaseAPIController;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
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
            return "redirect:/home";
        } else {
            return "redirect:/home";
        }
    }




    //    @PostMapping("register")
//    public ResponseEntity<TFUResponse<UserEntity>> register(@RequestBody RegisterDTO dto) {
//        UserEntity user = userService.registerUser(dto);
//        if (user == null) {
//
//            return badRequest("User register fail");
//        }
//
//        return success(user);
//    }
}
    
