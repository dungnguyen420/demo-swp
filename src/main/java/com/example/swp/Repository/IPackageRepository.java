package com.example.swp.Repository;

import com.example.swp.Entity.PackageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPackageRepository extends JpaRepository<PackageEntity,Long>, JpaSpecificationExecutor<PackageEntity> {

    Page<PackageEntity> findByNameContaining(String trim, Pageable pageable);


}