package com.example.swp.Service;

import com.example.swp.DTO.ClassesDTO;
import com.example.swp.DTO.CreateClassBySlotDTO;
import com.example.swp.DTO.CreateClassDTO;
import com.example.swp.Entity.ClassesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClassesService {

    ClassesEntity createClassBySlots(CreateClassBySlotDTO dto);
    Page<ClassesEntity> listPaged(int page, int size, String sortBy, String dir);
    ClassesEntity getDetail(Long id);
    void register(Long classId, Long userId);
    void unregister(Long classId, Long userId);
    Page<ClassesEntity> listUpcoming(int page, int size, String sortBy, String dir);
    ClassesEntity updateBasicInfo(Long classId, String name, String description, Integer capacity);
    void deleteById(Long classId);
    Page<ClassesEntity> search(String className,
                               String trainerLast,
                               String gender,
                               String mode,
                               Pageable pageable);

}
