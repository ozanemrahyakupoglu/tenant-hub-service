package com.obntech.tenanthub.entity;

import com.obntech.tenanthub.entity.base.BaseEntity;
import com.obntech.tenanthub.enums.Status;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter
@Setter
public class RoleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "SEQ_ROLE", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    private List<RolePermissionEntity> rolePermissions = new ArrayList<>();
}
