package com.leavemgmt.controller;

import com.leavemgmt.dto.DashboardStatsDTO;
import com.leavemgmt.dto.EmployeeDTO;
import com.leavemgmt.dto.LeaveApprovalDTO;
import com.leavemgmt.dto.LeaveRequestDTO;
import com.leavemgmt.service.EmployeeService;
import com.leavemgmt.service.LeaveService;
import com.leavemgmt.service.ManagerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/**
 * ManagerController - Handles manager-related endpoints
 */
@RestController
@RequestMapping("/api/managers")
@CrossOrigin(origins = "*", maxAge = 3600)
@PreAuthorize("hasRole('MANAGER')")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * Get manager dashboard statistics
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDTO> getManagerDashboard() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalEmployees(employeeService.getTotalEmployeesCount());
        stats.setPendingRequests(leaveService.getPendingRequestsCount());
        stats.setApprovedRequests(leaveService.getApprovedRequestsCount());
        stats.setRejectedRequests(leaveService.getRejectedRequestsCount());
        stats.setTotalLeaveRequests(stats.getPendingRequests() + stats.getApprovedRequests() + stats.getRejectedRequests());
        return ResponseEntity.ok(stats);
    }

    /**
     * Get all leave requests with pagination and sorting
     */
    @GetMapping("/leaves")
    public ResponseEntity<Page<LeaveRequestDTO>> getAllLeaveRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appliedDate") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<LeaveRequestDTO> leaves = leaveService.getPendingLeaveRequests(pageable);
        return ResponseEntity.ok(leaves);
    }

    /**
     * Filter leave requests by status and date range
     */
    @GetMapping("/leaves/filter")
    public ResponseEntity<Page<LeaveRequestDTO>> filterLeaveRequests(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appliedDate") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<LeaveRequestDTO> leaves = leaveService.getLeaveRequestsWithFilters(
                null, status, startDate, endDate, pageable
        );
        return ResponseEntity.ok(leaves);
    }

    /**
     * Approve leave request
     */
    @PostMapping("/leaves/approve")
    public ResponseEntity<LeaveRequestDTO> approveLeaveRequest(
            @Valid @RequestBody LeaveApprovalDTO approvalDTO) {
        LeaveRequestDTO approvedLeave = managerService.approveLeaveRequest(approvalDTO);
        return ResponseEntity.ok(approvedLeave);
    }

    /**
     * Reject leave request
     */
    @PostMapping("/leaves/reject")
    public ResponseEntity<LeaveRequestDTO> rejectLeaveRequest(
            @Valid @RequestBody LeaveApprovalDTO approvalDTO) {
        LeaveRequestDTO rejectedLeave = managerService.rejectLeaveRequest(approvalDTO);
        return ResponseEntity.ok(rejectedLeave);
    }

    /**
     * Search employees
     */
    @GetMapping("/employees/search")
    public ResponseEntity<List<EmployeeDTO>> searchEmployees(
            @RequestParam(required = false) String searchTerm) {
        List<EmployeeDTO> employees;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            employees = employeeService.searchEmployees(searchTerm);
        } else {
            employees = employeeService.getAllActiveEmployees();
        }
        return ResponseEntity.ok(employees);
    }

    /**
     * Get employee by ID
     */
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable Long employeeId) {
        EmployeeDTO employee = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(employee);
    }

    /**
     * Get leave history for specific employee
     */
    @GetMapping("/employees/{employeeId}/leaves")
    public ResponseEntity<Page<LeaveRequestDTO>> getEmployeeLeaveHistory(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appliedDate") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<LeaveRequestDTO> leaves = leaveService.getLeaveHistory(employeeId, pageable);
        return ResponseEntity.ok(leaves);
    }

    /**
     * Get manager statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<DashboardStatsDTO> getStatistics() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTotalEmployees(employeeService.getTotalEmployeesCount());
        stats.setPendingRequests(leaveService.getPendingRequestsCount());
        stats.setApprovedRequests(leaveService.getApprovedRequestsCount());
        stats.setRejectedRequests(leaveService.getRejectedRequestsCount());
        return ResponseEntity.ok(stats);
    }
}
