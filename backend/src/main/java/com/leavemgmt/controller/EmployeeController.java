package com.leavemgmt.controller;

import com.leavemgmt.dto.EmployeeDTO;
import com.leavemgmt.dto.LeaveRequestDTO;
import com.leavemgmt.security.CustomUserPrincipal;
import com.leavemgmt.service.EmployeeService;
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

import java.util.HashMap;
import java.util.Map;

/**
 * EmployeeController - Handles employee endpoints
 */
@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private LeaveService leaveService;

    /**
     * Get current employee dashboard
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<EmployeeDTO> getDashboard(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        EmployeeDTO employee = employeeService.getEmployeeByEmployeeId(principal.getUsername());
        return ResponseEntity.ok(employee);
    }

    /**
     * Get current employee details
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<EmployeeDTO> getCurrentEmployee(Authentication authentication) {
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        EmployeeDTO employee = employeeService.getEmployeeByEmployeeId(principal.getUsername());
        return ResponseEntity.ok(employee);
    }

    /**
     * Get employee by ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    /**
     * Search employees
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> searchEmployees(@RequestParam(required = false) String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Search term is required")
            );
        }
        return ResponseEntity.ok(employeeService.searchEmployees(searchTerm));
    }

    /**
     * Get employees by department
     */
    @GetMapping("/department/{department}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> getEmployeesByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(department));
    }

    /**
     * Get all active employees
     */
    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<?> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllActiveEmployees());
    }
}
