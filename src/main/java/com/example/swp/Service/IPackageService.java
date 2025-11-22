package com.example.swp.Service;

import com.example.swp.DTO.PackageDTO;
import com.example.swp.Entity.PackageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPackageService {
    Page<PackageEntity> findAll(Pageable pageable);
    PackageEntity createPackage(PackageDTO dto);
    void deletePackage(Long id);
    PackageEntity updatePackage(PackageDTO dto,Long id);
    PackageDTO findPackageById(Long id);
    List<PackageDTO> getMyPackages(Long userId);
    Page<PackageEntity> searchAndSortPackage(String keyword, Pageable pageable);
}