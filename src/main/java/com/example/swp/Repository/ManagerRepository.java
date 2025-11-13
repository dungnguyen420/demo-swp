package com.example.swp.Repository;

import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserGender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByUserName(String userName);

    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM Users u " +
            "WHERE u.role = com.example.swp.Enums.UserRole.MANAGER " +
            "AND (:name IS NULL OR (LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%')))) " + // Tìm theo first name hoặc last name
            "AND (:gender IS NULL OR u.gender = :gender) ")
    Page<UserEntity> searchManagers(
            @Param("name") String name,
            @Param("gender") UserGender gender,
            Pageable pageable
    );

}
