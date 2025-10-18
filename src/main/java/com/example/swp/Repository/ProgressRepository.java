package com.example.swp.Repository;

import com.example.swp.Entity.ProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressRepository extends JpaRepository<ProgressEntity, Long> {
    // Tìm danh sách tiến độ theo memberId
    List<ProgressEntity> findByMemberId(Long memberId);
}
