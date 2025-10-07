package com.example.swp.Config;

import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.Status;
import com.example.swp.Enums.UserGender;
import com.example.swp.Enums.UserRole;
import com.example.swp.Service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createDefaultAdmin();
    }

    private void createDefaultAdmin() {
        String adminEmail = "admin@gmail.com";
        Optional<UserEntity> admin = userService.findByEmail(adminEmail);
        if (admin.isEmpty()) {
            admin = Optional.of(new UserEntity());
            admin.get().setUserName("admin");
            admin.get().setFirstName("Admin");
            admin.get().setLastName("Admin");
            admin.get().setPassword(passwordEncoder.encode("admin"));
            admin.get().setEmail(adminEmail);
            admin.get().setRole(UserRole.ADMIN);
            admin.get().setGender(UserGender.FEMALE);
            admin.get().setActive(true);
            admin.get().setStatus(Status.ACTIVE);
            admin.get().setBirthDate(LocalDateTime.of(2004, 7, 23, 0, 0));
            userService.creatOrUpdateUser(admin.orElse(null));
            System.out.println("Admin create succesfully");
        } else {
            admin.get().setGender(UserGender.MALE);
            admin.get().setPhone("0981272666");
            userService.creatOrUpdateUser(admin.orElse(null));
            System.out.println("Admin already exists");
        }
    }

}



