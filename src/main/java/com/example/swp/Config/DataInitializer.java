package com.example.swp.Config;

import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.Status;
import com.example.swp.Enums.UserRole;
import com.example.swp.Service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception{
        createDefaultAdmin();
    }

    private void createDefaultAdmin(){
        String adminEmail = "admin@gmail.com";
        if (!userService.existedByEmail(adminEmail)){
            UserEntity admin = new UserEntity();
            admin.setUserName("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail(adminEmail);
            admin.setRole(UserRole.ADMIN);
            admin.setActive(true);
            admin.setStatus(Status.ACTIVE);
            userService.createUser(admin);
        }else {
            System.out.println("Admin already exists");
        }
    }
}
