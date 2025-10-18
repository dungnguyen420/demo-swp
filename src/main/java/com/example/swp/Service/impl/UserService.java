package com.example.swp.Service.impl;

import com.example.swp.DTO.RegisterDTO;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.Status;
import com.example.swp.Enums.UserRole;
import com.example.swp.Repository.IUserRepository;
import com.example.swp.Service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity registerUser(RegisterDTO dto) {
        UserEntity checkUserName = userRepository.findByUserName(dto.getUserName()).orElse(null);
        Optional<UserEntity> checkEmail = userRepository.findByEmail(dto.getEmail());
        if(checkUserName == null && checkEmail.isEmpty()){
            UserEntity newUser = new UserEntity();
            newUser.setUserName(dto.getUserName());
            newUser.setEmail(dto.getEmail());
            newUser.setFirstName(dto.getFirstName());
            newUser.setLastName(dto.getLastName());
            newUser.setStatus(Status.ACTIVE);
            newUser.setRole(UserRole.MEMBER);
            newUser.setPassword(passwordEncoder.encode(dto.getPassWord()));
            return userRepository.save(newUser);
        }
        return null;
    }

    @Override
    public UserEntity findByUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    @Override
    public boolean existedByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserEntity creatOrUpdateUser(UserEntity model) {
        return userRepository.save(model);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public UserEntity findByUserNameOrEmail(String input) {
        return userRepository.findByUserName(input)
                .or(() -> userRepository.findByEmail(input))
                .orElse(null);
    }

    @Override
    public UserEntity loginUser(String usernameOrEmail, String rawPassword){
        UserEntity user = findByUserNameOrEmail(usernameOrEmail);

        if (user == null) {
            return null;
        }

        if (!user.isActive()) {
            return null;
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            return null;
        }

        return user;
    }

    @Override
    public boolean authenticateUser(String usernameOrEmail, String password, HttpSession session) {
        UserEntity user = loginUser(usernameOrEmail, password);
        if (user == null || !user.isActive()) {
            return false;
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return true;
    }

    @Override
    public List<UserEntity> findByRole(UserRole member) {
        List<UserEntity> users = userRepository.findByRole(UserRole.MEMBER);
        return users;
    }

    @Override
    public void deleteUser(Long Id) {
        userRepository.deleteById(Id);
    }

    @Override
    public UserEntity updateUser(Long id, RegisterDTO dto) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với id: " + id));
        existingUser.setFirstName(dto.getFirstName());
        existingUser.setLastName(dto.getLastName());
        existingUser.setEmail(dto.getEmail());
        return userRepository.save(existingUser);
    }

    @Override
    public UserEntity findById(Long Id) {
        return userRepository.findById(Id).orElse(null);
    }

    @Override
    public Page<UserEntity> findByRole(UserRole role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
    }
    @Override
    public Page<UserEntity> searchUsers(String keyword, Pageable pageable) {
        return userRepository.searchByKeyword(keyword, pageable);
    }


}
