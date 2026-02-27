package com.obntech.tenanthub.repository;

import com.obntech.tenanthub.entity.RoleEntity;
import com.obntech.tenanthub.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(String name);

    boolean existsByName(String name);

    Page<RoleEntity> findAllByStatus(Status status, Pageable pageable);
}
