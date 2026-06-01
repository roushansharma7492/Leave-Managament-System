package com.leavemgmt.controller;

import com.leavemgmt.dto.LeaveRequestDTO;
import com.leavemgmt.security.CustomUserPrincipal;
import com.leavemgmt.service.LeaveService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

/**
 * LeaveController - Handles leave request endpoints
 */
@RestController
@RequestMapping("/api/leaves")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    /**
     * Apply for leave
     */
    @PostMapping("/apply")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> applyForLeave(
            @Valid @RequestBody LeaveRequestDTO leaveRequestDTO,
            Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        LeaveRequestDTO response = leaveService.applyForLeave(principal.getId(), leaveRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get leave history for employee
     */
    @GetMapping("/history")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> getLeaveHistory(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "appliedDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<LeaveRequestDTO> history = leaveService.getLeaveHistory(principal.getId(), pageable);
        return ResponseEntity.ok(history);
    }

    /**
     * Get leave request by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> getLeaveRequest(@PathVariable Long id) {
        LeaveRequestDTO leave = leaveService.getLeaveRequestById(id);
        return ResponseEntity.ok(leave);
    }

    /**
     * Update leave request (only if PENDING)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> updateLeaveRequest(
            @PathVariable Long id,
            @Valid @RequestBody LeaveRequestDTO leaveRequestDTO) {
        LeaveRequestDTO updated = leaveService.updateLeaveRequest(id, leaveRequestDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Cancel leave request (only if PENDING)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> cancelLeaveRequest(@PathVariable Long id) {
        LeaveRequestDTO cancelled = leaveService.cancelLeaveRequest(id);
        return ResponseEntity.ok(cancelled);
    }

    /**
     * Get all pending leaves (for managers)
     */
    @GetMapping("/pending/all")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> getPendingLeaves(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "appliedDate"));
        Page<LeaveRequestDTO> pending = leaveService.getPendingLeaveRequests(pageable);
        return ResponseEntity.ok(pending);
    }

    /**
     * Get leaves with filters
     */
    @GetMapping("/filter")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> filterLeaves(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "appliedDate"));
        Page<LeaveRequestDTO> filtered = leaveService.getLeaveRequestsWithFilters(
                employeeId, status, start, end, pageable
        );
        return ResponseEntity.ok(filtered);
    }
}
