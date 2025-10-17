package com.example.swp.Repository;

import com.example.swp.Entity.PackageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPackageRepository extends JpaRepository<PackageEntity,Long> {
}
