package com.leavemgmt.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * LeaveCalculationUtil - Utility class for leave calculations
 */
public class LeaveCalculationUtil {

    /**
     * Calculate number of days between two dates (excluding weekends)
     */
    public static Integer calculateLeaveDays(LocalDate startDate, LocalDate endDate) {
        int days = 0;
        LocalDate currentDate = startDate;
        
        while (!currentDate.isAfter(endDate)) {
            // Exclude Saturdays (6) and Sundays (7)
            if (currentDate.getDayOfWeek().getValue() < 6) {
                days++;
            }
            currentDate = currentDate.plusDays(1);
        }
        
        return days;
    }

    /**
     * Calculate total days between two dates (including weekends)
     */
    public static Integer calculateTotalDays(LocalDate startDate, LocalDate endDate) {
        return Math.toIntExact(ChronoUnit.DAYS.between(startDate, endDate)) + 1;
    }

    /**
     * Validate if dates are valid for leave request
     */
    public static boolean isValidLeaveDate(LocalDate startDate, LocalDate endDate) {
        return !startDate.isAfter(endDate) && !startDate.isBefore(LocalDate.now());
    }
}
