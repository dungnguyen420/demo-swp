package com.example.swp.Repository;

import com.example.swp.Entity.NutritionPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NutritionPlanRepository extends JpaRepository<NutritionPlanEntity, Long> {
    // Lấy danh sách chế độ dinh dưỡng theo member
    List<NutritionPlanEntity> findByMemberId(Long memberId);
}
