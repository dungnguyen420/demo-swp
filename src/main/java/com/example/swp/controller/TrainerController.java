package com.example.swp.controller;

import com.example.swp.DTO.TrainerDTO;
import com.example.swp.DTO.response.TFUResponse;
import com.example.swp.Entity.UserEntity;
import com.example.swp.Enums.UserRole;
import com.example.swp.Repository.TrainerRepository;
import com.example.swp.Service.IUserService;

import com.example.swp.Service.impl.TrainerService;
import com.example.swp.base.BaseAPIController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainer")
public class TrainerController extends BaseAPIController {

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainerRepository trainerRepository;

    @GetMapping("get-list-trainer")
    public ResponseEntity<TFUResponse<List<UserEntity>>> getAllTrainer(){
        List<UserEntity> getTrainer = trainerService.getAllTrainer(UserRole.TRAINER);
        return success(getTrainer);
    }

    @PostMapping("create-trainer")
    public ResponseEntity<TFUResponse<UserEntity>> createTrainer(@RequestBody TrainerDTO dto){
        UserEntity createTrainer = trainerService.createTrainer(dto);
        if(createTrainer == null){
            return  badRequest("không tạo được trainer");
        }
        return success(createTrainer);
    }

    @PutMapping("update-trainer")
    public ResponseEntity<TFUResponse<UserEntity>> updateTrainer(@RequestBody TrainerDTO dto){
        UserEntity updateTrainer = trainerService.updateTrainer(dto);
        if (updateTrainer == null){
            return badRequest("không tìm thấy trainer");
        }
        return success(updateTrainer);
    }

    @DeleteMapping("delete-trainer")
    public ResponseEntity<TFUResponse<UserEntity>> deleteTrainer(@RequestBody Long id){
    UserEntity deleteTrainer = trainerService.deleteTrainer(id);
    return  success(deleteTrainer);
    }


}
