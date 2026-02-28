package com.obntech.tenanthub.repository;

import com.obntech.tenanthub.entity.UserEntity;
import com.obntech.tenanthub.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Page<UserEntity> findAllByStatus(Status status, Pageable pageable);

    @Query("SELECT u FROM UserEntity u " +
        "LEFT JOIN FETCH u.userRoles ur " +
        "LEFT JOIN FETCH ur.role r " +
        "LEFT JOIN FETCH r.rolePermissions rp " +
        "LEFT JOIN FETCH rp.permission " +
        "WHERE u.username = :username")
    Optional<UserEntity> findByUsernameWithPermissions(@Param("username") String username);
}
