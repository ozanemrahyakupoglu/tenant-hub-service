package com.obntech.tenanthub.repository;

import com.obntech.tenanthub.entity.PaymentEntity;
import com.obntech.tenanthub.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Page<PaymentEntity> findAllByStatus(Status status, Pageable pageable);

    Page<PaymentEntity> findAllByRentId(Long rentId, Pageable pageable);
}
