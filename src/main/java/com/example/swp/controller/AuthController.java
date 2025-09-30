package com.example.swp.controller;

import com.example.swp.Config.JwtUtil;
import com.example.swp.DTO.RegisterDTO;
import com.example.swp.DTO.response.TFUResponse;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("register")
    public ResponseEntity<TFUResponse<UserEntity>> register(@RequestBody RegisterDTO dto){
        UserEntity user = userService.registerUser(dto);
        if(user == null){
            TFUResponse<UserEntity> response = TFUResponse.<UserEntity>builder()
                    .success(false)
                    .message("Không tạo được user")
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        TFUResponse<UserEntity> response = TFUResponse.<UserEntity>builder()
                .success(true)
                .message("Tạo user thành công")
                .statusCode(HttpStatus.OK.value())
                .data(user)
                .build();

        return ResponseEntity.ok(response);
    }
}
