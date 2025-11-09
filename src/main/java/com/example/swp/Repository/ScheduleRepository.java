package com.example.swp.Repository;

import com.example.swp.Entity.ScheduleEntity;
import com.example.swp.Enums.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

    // Mỗi trainer chỉ 1 lịch trong cùng slot
    boolean existsByTrainer_IdAndSlot_Id(Long trainerId, Long slotId);

    List<ScheduleEntity> findAllByTrainer_Id(Long trainerId);

    boolean existsByMember_IdAndSlot_Id(Long memberId, Long slotId);

    @Query("SELECT s FROM ScheduleEntity s JOIN FETCH s.slot sl JOIN FETCH s.trainer " +
            "WHERE s.member.id = :memberId " +
            "AND (LOWER(:mode) = 'all' OR sl.slotDate >= CURRENT_DATE) " +
            "ORDER BY sl.slotDate DESC")
    List<ScheduleEntity> findAllPersonalSchedulesByMemberId(@Param("memberId") Long memberId, @Param("mode") String mode);

    /** 2. Lấy Lịch Lớp học (cho Member) - ĐÃ SỬA LẠI */
    @Query("SELECT s FROM ClassMember cm " +
            "JOIN cm.classEntity c " +
            "JOIN c.schedules s " +
            "JOIN FETCH s.slot sl " +
            "JOIN FETCH s.trainer " +
            "WHERE cm.id.userId = :userId " +
            "AND (LOWER(:mode) = 'all' OR sl.slotDate >= CURRENT_DATE) " +
            "ORDER BY sl.slotDate ASC")
    List<ScheduleEntity> findAllClassSchedulesByMemberId(@Param("userId") Long userId, @Param("mode") String mode);

    @Query("SELECT s FROM ScheduleEntity s JOIN FETCH s.slot sl " +
            "WHERE s.trainer.id = :trainerId " +
            "AND (LOWER(:mode) = 'all' OR sl.slotDate >= CURRENT_DATE) " +
            "ORDER BY sl.slotDate DESC")
    List<ScheduleEntity> findAllTeachingSchedulesByTrainerId(@Param("trainerId") Long trainerId, @Param("mode") String mode);
}
