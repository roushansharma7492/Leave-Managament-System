package com.leavemgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DashboardStatsDTO - Data Transfer Object for Dashboard Statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private Long totalEmployees;
    private Long pendingRequests;
    private Long approvedRequests;
    private Long rejectedRequests;
    private Long totalLeaveRequests;
}
