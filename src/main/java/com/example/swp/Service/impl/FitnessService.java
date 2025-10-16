package com.example.swp.Service.impl;

import com.example.swp.Entity.*;
import com.example.swp.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FitnessService {

    @Autowired private ProgressRepository progressRepo;
    @Autowired private PhysicalEvaluationRepository evalRepo;
    @Autowired private NutritionPlanRepository planRepo;
    @Autowired private NotificationRepository notifRepo;
    @Autowired private EquipmentRepository equipRepo;

    // --- Progress ---
    public ProgressEntity saveProgress(ProgressEntity p) { return progressRepo.save(p); }
    public List<ProgressEntity> getProgressByMember(Long memberId) {
        return progressRepo.findAll().stream()
                .filter(p -> p.getMemberId().equals(memberId)).toList();
    }

    // --- Evaluation ---
    public PhysicalEvaluationEntity saveEvaluation(PhysicalEvaluationEntity e) { return evalRepo.save(e); }
    public List<PhysicalEvaluationEntity> getEvalByMember(Long memberId) {
        return evalRepo.findAll().stream()
                .filter(e -> e.getMemberId().equals(memberId)).toList();
    }

    // --- Nutrition ---
    public NutritionPlanEntity savePlan(NutritionPlanEntity plan) { return planRepo.save(plan); }
    public List<NutritionPlanEntity> getPlanByMember(Long memberId) {
        return planRepo.findAll().stream()
                .filter(p -> p.getMemberId().equals(memberId)).toList();
    }

    // --- Notifications ---
    public NotificationEntity sendNotification(NotificationEntity n) { return notifRepo.save(n); }
    public List<NotificationEntity> getReceived(Long userId) {
        return notifRepo.findAll().stream()
                .filter(n -> n.getUser().getId().equals(userId)).toList();
    }

    // --- Equipment ---
    public EquipmentEntity saveEquipment(EquipmentEntity e) { return equipRepo.save(e); }
    public List<EquipmentEntity> getAllEquipment() { return equipRepo.findAll(); }
}
