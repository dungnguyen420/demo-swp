package com.example.swp.Repository;

import com.example.swp.Entity.ClassesEntity;
import com.example.swp.Enums.UserGender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClassesRepository extends JpaRepository<ClassesEntity, Long> {

    @EntityGraph(attributePaths = {"trainer", "schedules", "schedules.slot"})
    Page<ClassesEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"trainer", "schedules", "schedules.slot"})
    Optional<ClassesEntity> findWithAllById(Long id);

    @EntityGraph(attributePaths = {"trainer"}) // list chỉ cần trainer, tránh N+1
    Page<ClassesEntity> findDistinctBySchedules_Slot_SlotDateGreaterThanEqual(
            LocalDate date, Pageable pageable);

    @EntityGraph(attributePaths = {"trainer"})
    @Query("""
  select distinct c from ClassesEntity c
  left join c.trainer t
  where (:className is null or lower(c.name) like lower(concat('%', :className, '%')))
    and (:trainerLast is null or lower(t.lastName) like lower(concat('%', :trainerLast, '%')))
    and (:gender is null or t.gender = :gender)
    and (
         :mode = 'all'
         or (:mode = 'upcoming' and exists (
               select 1 from ScheduleEntity sc2 join sc2.slot sl2
               where c member of sc2.classes and sl2.slotDate >= CURRENT_DATE
             ))
         or (:mode = 'finished' and not exists (
               select 1 from ScheduleEntity sc3 join sc3.slot sl3
               where c member of sc3.classes and sl3.slotDate >= CURRENT_DATE
             ))
        )
""")
    Page<ClassesEntity> search(@Param("className") String className,
                               @Param("trainerLast") String trainerLast,
                               @Param("gender") UserGender gender,
                               @Param("mode") String mode,
                               Pageable pageable);

}
