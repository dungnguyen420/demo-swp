package com.example.swp.controller;

import com.example.swp.Config.JwtUtil;
import com.example.swp.DTO.AuthRequest;
import com.example.swp.DTO.AuthResponse;
import com.example.swp.DTO.RegisterDTO;
import com.example.swp.DTO.response.TFUResponse;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Service.IUserService;
import com.example.swp.base.BaseAPIController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController extends BaseAPIController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("register")
    public ResponseEntity<TFUResponse<UserEntity>> register(@RequestBody RegisterDTO dto) {
        UserEntity user = userService.registerUser(dto);
        if (user == null) {
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
    @PostMapping("login")
    public ResponseEntity<TFUResponse<AuthResponse>> login (@RequestBody AuthRequest dto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUserName(),dto.getPassWord()));

        UserEntity user = userService.findByUserName(dto.getUserName());

        if (user == null){
            return badRequest("User not find");
        }
        String jwt = jwtUtil.generateToken(user);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(jwt);
        authResponse.setUserName(user.getUserName());
        return success(authResponse);
    }
    
}