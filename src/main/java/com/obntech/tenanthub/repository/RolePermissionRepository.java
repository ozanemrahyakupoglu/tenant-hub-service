package com.obntech.tenanthub.repository;

import com.obntech.tenanthub.entity.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, Long> {

    List<RolePermissionEntity> findAllByRoleId(Long roleId);

    List<RolePermissionEntity> findAllByPermissionId(Long permissionId);

    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);
}
