package com.example.swp.Service;

import com.example.swp.DTO.ClassesDTO;
import com.example.swp.DTO.CreateClassBySlotDTO;
import com.example.swp.DTO.CreateClassDTO;
import com.example.swp.Entity.ClassesEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IClassesService {
    ClassesEntity createClassBySlots(CreateClassBySlotDTO dto);
    Page<ClassesEntity> listPaged(int page, int size, String sortBy, String dir);
    ClassesEntity getDetail(Long id);
}
