package com.example.swp.Service.impl;

import com.example.swp.DTO.RegisterDTO;
import com.example.swp.DTO.UserDTO;
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
    public void deleteUser(Long Id) {
        userRepository.deleteById(Id);
    }

    @Override
    public UserEntity updateUser(Long id, UserDTO dto) {


        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với id: " + id));

        Optional<UserEntity> userWithSameEmail = userRepository.findByEmail(dto.getEmail());

        if(userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)) {

            throw new RuntimeException("Email này đã được sử dụng bởi tài khoản khác.");
        }Optional<UserEntity> userWithSameName = userRepository.findByUserName(dto.getUserName());
        if(userWithSameName.isPresent() && !userWithSameName.get().getId().equals(id)) {
            throw new RuntimeException("Tên đăng nhập này đã được sử dụng.");
        }


        existingUser.setFirstName(dto.getFirstName());
        existingUser.setLastName(dto.getLastName());
        existingUser.setEmail(dto.getEmail());
        existingUser.setUserName(dto.getUserName());
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

    @Override
    public UserEntity updateProfile(String userName, UserEntity formUser) {
        UserEntity currentUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        validateUserData(formUser, currentUser);

        if (!formUser.getUserName().equals(currentUser.getUserName())
                && userRepository.findByUserName(formUser.getUserName()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        if (!formUser.getEmail().equals(currentUser.getEmail())
                && userRepository.findByEmail(formUser.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        currentUser.setUserName(formUser.getUserName());
        currentUser.setFirstName(formUser.getFirstName());
        currentUser.setLastName(formUser.getLastName());
        currentUser.setEmail(formUser.getEmail());
        currentUser.setPhone(formUser.getPhone());
        currentUser.setGender(formUser.getGender());
        currentUser.setBirthDate(formUser.getBirthDate());

        if (formUser.getPassword() != null && !formUser.getPassword().isEmpty()) {
            currentUser.setPassword(passwordEncoder.encode(formUser.getPassword()));
        }

        return userRepository.save(currentUser);
    }

    private void validateUserData(UserEntity user, UserEntity currentUser) {
        if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
            throw new RuntimeException("Tên đăng nhập không được để trống!");
        }
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new RuntimeException("Họ không được để trống!");
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new RuntimeException("Tên không được để trống!");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email không được để trống!");
        }
        if (!user.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new RuntimeException("Định dạng email không hợp lệ!");
        }
        if (user.getPhone() == null || user.getPhone().trim().isEmpty()) {
            throw new RuntimeException("Số điện thoại không được để trống!");
        }
        if (!user.getPhone().matches("^[0-9]{9,11}$")) {
            throw new RuntimeException("Số điện thoại chỉ được chứa 9–11 chữ số!");
        }
        if (user.getBirthDate() == null) {
            throw new RuntimeException("Ngày sinh không được để trống!");
        }
        if (user.getBirthDate().isAfter(java.time.LocalDateTime.now())) {
            throw new RuntimeException("Ngày sinh phải là ngày trong quá khứ!");
        }
    }
    public UserEntity findUserByRole(UserRole role) {

        return userRepository.findFirstByRole(role).orElse(null);
    }
    @Override
    public Optional<UserEntity> findByUserNameOptional(String userName) {

        return userRepository.findByUserNameOptional(userName);
    }
}
