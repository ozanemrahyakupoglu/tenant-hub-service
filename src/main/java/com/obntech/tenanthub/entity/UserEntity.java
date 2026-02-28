package com.obntech.tenanthub.entity;

import com.obntech.tenanthub.entity.base.BaseEntity;
import com.obntech.tenanthub.enums.Status;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "USERS", schema = "devuser")
@Getter
@Setter
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "SEQ_USERS", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone", length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status = Status.ACTIVE;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    @Column(name = "error_login_count", nullable = false)
    private Integer errorLoginCount = 0;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserRoleEntity> userRoles = new ArrayList<>();
}
