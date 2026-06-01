package com.leavemgmt.entity;

/**
 * LeaveStatus Enum - Defines statuses for leave requests
 */
public enum LeaveStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    CANCELLED("Cancelled");

    private final String displayName;

    LeaveStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
