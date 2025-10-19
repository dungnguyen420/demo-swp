package com.example.swp.Repository;

import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    List<UserEntity> findAllByRole(UserRole role);

    Page<UserEntity> findByRole(UserRole role, Pageable pageable);
    @Query("SELECT u FROM Users u WHERE " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<UserEntity> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);


}
