package com.example.swp.Repository;

import com.example.swp.Entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> , JpaSpecificationExecutor<PaymentEntity> {

}
