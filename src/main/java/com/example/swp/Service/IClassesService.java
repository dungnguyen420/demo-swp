package com.example.swp.Service;

import com.example.swp.DTO.ClassesDTO;
import com.example.swp.Entity.ClassesEntity;

public interface IClassesService {
    ClassesEntity createClasses(ClassesDTO dto);

}
