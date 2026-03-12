package com.platform.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Entity
@Table(
    name = "business_working_hours",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_business_day_time",
            columnNames = {"business_id", "day_of_week", "open_time", "close_time"}
        )
    }
)
public class BusinessWorkingHours {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 20)
    private DayOfWeek dayOfWeek;

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;

    public BusinessWorkingHours() {}

    public BusinessWorkingHours(Business business,
                                DayOfWeek dayOfWeek,
                                LocalTime openTime,
                                LocalTime closeTime) {
        this.business = business;
        this.dayOfWeek = dayOfWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

}
