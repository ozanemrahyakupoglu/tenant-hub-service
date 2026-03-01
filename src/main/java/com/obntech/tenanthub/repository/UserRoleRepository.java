package com.obntech.tenanthub.repository;

import com.obntech.tenanthub.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    List<UserRoleEntity> findAllByUser_Id(Long userId);

    List<UserRoleEntity> findAllByRoleId(Long roleId);

    boolean existsByUserIdAndRoleId(Long userId, Long roleId);
}
