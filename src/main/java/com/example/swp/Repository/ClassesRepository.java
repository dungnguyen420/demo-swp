package com.example.swp.Repository;

import com.example.swp.Entity.ClassesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassesRepository extends JpaRepository<ClassesEntity, Long> {

    @EntityGraph(attributePaths = {"trainer", "schedules", "schedules.slot"})
    Page<ClassesEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"trainer", "schedules", "schedules.slot"})
    Optional<ClassesEntity> findWithAllById(Long id);
}
