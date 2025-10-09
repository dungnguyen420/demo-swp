package com.example.swp.Repository;

import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findByRole(UserRole TRAINER);

    Optional<UserEntity> findByEmail(String email);

    @EntityGraph(attributePaths = "trainerProfile")
    Optional<UserEntity> findById(Long id);


    Long id(Long id);
}
