package com.example.swp.Repository;

import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity>  findByUserName(String userName);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail (String email);
    List<UserEntity> findByRole(UserRole role);
}
