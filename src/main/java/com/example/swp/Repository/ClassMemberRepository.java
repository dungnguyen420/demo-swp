package com.example.swp.Repository;

import com.example.swp.Entity.ClassMember;
import com.example.swp.Entity.ClassMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassMemberRepository extends JpaRepository<ClassMember, ClassMemberId> {

    boolean existsById_ClassIdAndId_UserId(Long classId, Long userId);

    long countByClassEntity_Id(Long classId);
    boolean existsById_UserIdAndClassEntity_Schedules_Slot_IdIn(Long userId, List<Long> slotIds);


}
