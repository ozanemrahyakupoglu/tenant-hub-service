package com.obntech.tenanthub.entity;

import com.obntech.tenanthub.entity.base.BaseEntity;
import com.obntech.tenanthub.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "real_estate")
@Getter
@Setter
public class RealEstateEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "real_estate_seq")
    @SequenceGenerator(name = "real_estate_seq", sequenceName = "SEQ_REAL_ESTATE", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "province", nullable = false, length = 100)
    private String province;

    @Column(name = "district", nullable = false, length = 100)
    private String district;

    @Column(name = "neighborhood", nullable = false, length = 100)
    private String neighborhood;

    @Column(name = "address", nullable = false, length = 500)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status = Status.ACTIVE;
}
