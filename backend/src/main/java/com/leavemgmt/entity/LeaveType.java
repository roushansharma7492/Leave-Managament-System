package com.leavemgmt.entity;

/**
 * LeaveType Enum - Defines types of leaves
 */
public enum LeaveType {
    CASUAL("Casual Leave", 12),
    SICK("Sick Leave", 6),
    EARNED("Earned Leave", 20);

    private final String displayName;
    private final Integer defaultDays;

    LeaveType(String displayName, Integer defaultDays) {
        this.displayName = displayName;
        this.defaultDays = defaultDays;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Integer getDefaultDays() {
        return defaultDays;
    }
}
