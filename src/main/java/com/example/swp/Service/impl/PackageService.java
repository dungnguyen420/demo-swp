package com.example.swp.Service.impl;

import com.example.swp.DTO.PackageDTO;
import com.example.swp.Entity.PackageEntity;
import com.example.swp.Repository.IPackageRepository;
import com.example.swp.Service.IPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PackageService implements IPackageService {
    @Autowired
    private IPackageRepository packageRepository;

    @Override
    public Page<PackageEntity> findAll(Pageable pageable) {
        return packageRepository.findAll(pageable);
    }

    @Override
    public PackageEntity createPackage(PackageDTO dto) {
        PackageEntity newPackage = new PackageEntity();
        newPackage.setName(dto.getName());
        newPackage.setDescription(dto.getDescription());
        newPackage.setPrice(dto.getPrice());
        newPackage.setDurationMonth(dto.getDurationMonth());
        newPackage.setIsActive(true);
        newPackage.setPtSessionsCount(0);
        newPackage.setAccessLevel(PackageEntity.AccessLevel.BASIC); // Mặc định
        newPackage.setCreatedAt(LocalDateTime.now());
        newPackage.setUpdatedAt(LocalDateTime.now());
        return packageRepository.save(newPackage);
    }

    @Override
    public PackageEntity updatePackage(PackageDTO dto, Long id) {
        PackageEntity existingPackage = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found with id: " + id));
        existingPackage.setName(dto.getName());
        existingPackage.setDescription(dto.getDescription());
        existingPackage.setPrice(dto.getPrice());
        existingPackage.setDurationMonth(dto.getDurationMonth());
        existingPackage.setUpdatedAt(LocalDateTime.now());

        return packageRepository.save(existingPackage);
    }

    @Override
    public PackageDTO findPackageById(Long id) {
        PackageEntity packageEntity = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found with id: " + id));

        PackageDTO packageDTO = new PackageDTO();

        packageDTO.setName(packageEntity.getName());
        packageDTO.setDescription(packageEntity.getDescription());
        packageDTO.setPrice(packageEntity.getPrice());
        packageDTO.setDurationMonth(packageEntity.getDurationMonth());

        return packageDTO;
    }

    @Override
    public void deletePackage(Long Id) {
        packageRepository.deleteById(Id);
    }
}


