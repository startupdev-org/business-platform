package com.platform.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "business_features",
        uniqueConstraints = @UniqueConstraint(columnNames = {"business_id", "name"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long featureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(nullable = false, unique = true)
    private String name;
}
