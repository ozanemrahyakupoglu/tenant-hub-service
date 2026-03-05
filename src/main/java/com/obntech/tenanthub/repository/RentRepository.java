package com.obntech.tenanthub.repository;

import com.obntech.tenanthub.entity.RentEntity;
import com.obntech.tenanthub.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentRepository extends JpaRepository<RentEntity, Long> {

    Page<RentEntity> findAllByStatus(Status status, Pageable pageable);

    List<RentEntity> findAllByRealEstateId(Long realEstateId);

    Page<RentEntity> findAllByRealEstateId(Long realEstateId, Pageable pageable);
}
