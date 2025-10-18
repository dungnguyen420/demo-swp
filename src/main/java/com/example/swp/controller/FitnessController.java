//package com.example.swp.controller;
//
//import com.example.swp.Entity.*;
//import com.example.swp.Service.impl.FitnessService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api")
//public class FitnessController {
//
//    @Autowired private FitnessService service;
//
//    // --- Progress ---
//    @PostMapping("/progress")
//    public ProgressEntity addProgress(@RequestBody ProgressEntity p) { return service.saveProgress(p); }
//
//    @GetMapping("/progress/{memberId}")
//    public List<ProgressEntity> getProgress(@PathVariable Long memberId) {
//        return service.getProgressByMember(memberId);
//    }
//
//    // --- Evaluation ---
//    @PostMapping("/evaluation")
//    public PhysicalEvaluationEntity addEvaluation(@RequestBody PhysicalEvaluationEntity e) { return service.saveEvaluation(e); }
//
//    @GetMapping("/evaluation/{memberId}")
//    public List<PhysicalEvaluationEntity> getEvaluation(@PathVariable Long memberId) {
//        return service.getEvalByMember(memberId);
//    }
//
//    // --- Nutrition ---
//    @PostMapping("/nutrition")
//    public NutritionPlanEntity addPlan(@RequestBody NutritionPlanEntity plan) { return service.savePlan(plan); }
//
//    @GetMapping("/nutrition/{memberId}")
//    public List<NutritionPlanEntity> getPlans(@PathVariable Long memberId) {
//        return service.getPlanByMember(memberId);
//    }
//
//    // --- Notification ---
//    @PostMapping("/notification")
//    public NotificationEntity sendNotification(@RequestBody NotificationEntity n) { return service.sendNotification(n); }
//
//    @GetMapping("/notification/{userId}")
//    public List<NotificationEntity> getReceived(@PathVariable Long userId) {
//        return service.getReceived(userId);
//    }
//
//    // --- Equipment ---
//    @PostMapping("/equipment")
//    public EquipmentEntity addEquipment(@RequestBody EquipmentEntity e) { return service.saveEquipment(e); }
//
//    @GetMapping("/equipment")
//    public List<EquipmentEntity> getAllEquip() { return service.getAllEquipment(); }
//}
