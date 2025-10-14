package com.example.swp.Repository;

import com.example.swp.Entity.PhysicalEvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhysicalEvaluationRepository extends JpaRepository<PhysicalEvaluationEntity, Long> {
    // Lấy danh sách đánh giá thể chất của 1 member cụ thể
    List<PhysicalEvaluationEntity> findByMemberId(Long memberId);
}
