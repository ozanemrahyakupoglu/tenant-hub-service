package com.obntech.tenanthub.repository;

import com.obntech.tenanthub.entity.RealEstateEntity;
import com.obntech.tenanthub.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealEstateRepository extends JpaRepository<RealEstateEntity, Long> {

    boolean existsByName(String name);

    Page<RealEstateEntity> findAllByStatus(Status status, Pageable pageable);
}
