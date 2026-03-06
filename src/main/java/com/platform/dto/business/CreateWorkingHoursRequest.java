package com.platform.dto.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class    CreateWorkingHoursRequest {

    private DayOfWeek dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;

    // getters & setters
}
