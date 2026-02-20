package com.platform.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "business_features")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The business that owns this feature
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Column(nullable = false)
    private String name;
}
