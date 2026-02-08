package com.platform.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotGenerator {

    private static final LocalTime BUSINESS_OPEN = LocalTime.of(9, 0);
    private static final LocalTime BUSINESS_CLOSE = LocalTime.of(19, 0);
    private static final int SLOT_DURATION_MINUTES = 30;

    public static List<LocalDateTime> generateAvailableSlots(LocalDateTime date, int serviceDurationMinutes) {
        List<LocalDateTime> slots = new ArrayList<>();

        LocalDateTime startOfDay = date.withHour(BUSINESS_OPEN.getHour())
                .withMinute(BUSINESS_OPEN.getMinute())
                .withSecond(0);

        LocalDateTime endOfDay = date.withHour(BUSINESS_CLOSE.getHour())
                .withMinute(BUSINESS_CLOSE.getMinute())
                .withSecond(0);

        LocalDateTime currentSlot = startOfDay;

        while (currentSlot.plusMinutes(serviceDurationMinutes).isBefore(endOfDay) ||
               currentSlot.plusMinutes(serviceDurationMinutes).isEqual(endOfDay)) {
            slots.add(currentSlot);
            currentSlot = currentSlot.plusMinutes(SLOT_DURATION_MINUTES);
        }

        return slots;
    }

    public static List<LocalDateTime> generateAvailableSlots(LocalDateTime date, int serviceDurationMinutes,
                                                             LocalTime businessOpen, LocalTime businessClose) {
        List<LocalDateTime> slots = new ArrayList<>();

        LocalDateTime startOfDay = date.withHour(businessOpen.getHour())
                .withMinute(businessOpen.getMinute())
                .withSecond(0);

        LocalDateTime endOfDay = date.withHour(businessClose.getHour())
                .withMinute(businessClose.getMinute())
                .withSecond(0);

        LocalDateTime currentSlot = startOfDay;

        while (currentSlot.plusMinutes(serviceDurationMinutes).isBefore(endOfDay) ||
               currentSlot.plusMinutes(serviceDurationMinutes).isEqual(endOfDay)) {
            slots.add(currentSlot);
            currentSlot = currentSlot.plusMinutes(SLOT_DURATION_MINUTES);
        }

        return slots;
    }
}
