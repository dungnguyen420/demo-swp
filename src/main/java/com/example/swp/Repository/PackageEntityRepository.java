package com.example.swp.Repository;

import com.example.swp.Entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

interface PackageRepository extends JpaRepository<PackageEntity, Long> {

}
