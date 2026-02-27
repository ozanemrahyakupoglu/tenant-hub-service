package com.obntech.tenanthub.repository;

import com.obntech.tenanthub.entity.PermissionEntity;
import com.obntech.tenanthub.enums.PermissionAction;
import com.obntech.tenanthub.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    Optional<PermissionEntity> findByName(String name);

    boolean existsByName(String name);

    boolean existsByModuleAndAction(String module, PermissionAction action);

    List<PermissionEntity> findAllByModule(String module);

    Page<PermissionEntity> findAllByStatus(Status status, Pageable pageable);
}
