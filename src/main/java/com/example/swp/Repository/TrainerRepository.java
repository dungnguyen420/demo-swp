package com.example.swp.Repository;

import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserGender;
import com.example.swp.Enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    @Query("""
         select u
         from Users u
         left join fetch u.trainerProfile tp
         where u.role = :role
         """)
    List<UserEntity> findAllTrainerWithProfile(@Param("role") UserRole role);


    Optional<UserEntity> findByEmail(String email);

    @EntityGraph(attributePaths = "trainerProfile")
    Optional<UserEntity> findById(Long id);

    @Query("SELECT u FROM Users u JOIN u.trainerProfile tp " +
            "WHERE (:name IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:gender IS NULL OR u.gender = :gender) " +
            "AND (:specialization IS NULL OR LOWER(tp.specialization) LIKE LOWER(CONCAT('%', :specialization, '%')))")
    Page<UserEntity> searchTrainer(
            @Param("name") String name,
            @Param("gender") UserGender gender,
            @Param("specialization") String specialization,
            Pageable pageable
    );
    Optional<UserEntity> findByUserName(String userName);


    Long id(Long id);
}
