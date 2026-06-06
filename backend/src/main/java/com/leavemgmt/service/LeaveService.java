package com.leavemgmt.service;

import com.leavemgmt.dto.LeaveRequestDTO;
import com.leavemgmt.entity.Employee;
import com.leavemgmt.entity.LeaveRequest;
import com.leavemgmt.entity.LeaveStatus;
import com.leavemgmt.entity.LeaveType;
import com.leavemgmt.exception.BusinessLogicException;
import com.leavemgmt.exception.ResourceNotFoundException;
import com.leavemgmt.repository.EmployeeRepository;
import com.leavemgmt.repository.LeaveRequestRepository;
import com.leavemgmt.util.LeaveCalculationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * LeaveService - Service for handling leave request-related business logic
 */
@Service
@Transactional
public class LeaveService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Apply for leave
     */
    public LeaveRequestDTO applyForLeave(Long employeeId, LeaveRequestDTO leaveRequestDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        // Validate dates
        if (!LeaveCalculationUtil.isValidLeaveDate(leaveRequestDTO.getStartDate(), leaveRequestDTO.getEndDate())) {
            throw new BusinessLogicException("Invalid leave dates. Start date must be before end date and in future.");
        }

        // Calculate leave days
        Integer leaveDays = LeaveCalculationUtil.calculateLeaveDays(
                leaveRequestDTO.getStartDate(),
                leaveRequestDTO.getEndDate()
        );

        // Check leave balance
        if (employee.getRemainingLeaves() < leaveDays) {
            throw new BusinessLogicException(
                    "Insufficient leave balance. You have " + employee.getRemainingLeaves() + 
                    " days available but requesting " + leaveDays + " days."
            );
        }

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(employee);
        leaveRequest.setLeaveType(LeaveType.valueOf(leaveRequestDTO.getLeaveType().toUpperCase()));
        leaveRequest.setStartDate(leaveRequestDTO.getStartDate());
        leaveRequest.setEndDate(leaveRequestDTO.getEndDate());
        leaveRequest.setNumberOfDays(leaveDays);
        leaveRequest.setReason(leaveRequestDTO.getReason());
        leaveRequest.setStatus(LeaveStatus.PENDING);

        LeaveRequest savedLeave = leaveRequestRepository.save(leaveRequest);
        return convertToDTO(savedLeave);
    }

    /**
     * Get leave history for employee
     */
    public Page<LeaveRequestDTO> getLeaveHistory(Long employeeId, Pageable pageable) {
        Page<LeaveRequest> leaves = leaveRequestRepository.findByEmployeeId(employeeId, pageable);
        return leaves.map(this::convertToDTO);
    }

    /**
     * Get leave request by ID
     */
    public LeaveRequestDTO getLeaveRequestById(Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));
        return convertToDTO(leaveRequest);
    }

    /**
     * Cancel leave request (only if status is PENDING)
     */
    public LeaveRequestDTO cancelLeaveRequest(Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new BusinessLogicException("Can only cancel leave requests with PENDING status");
        }

        leaveRequest.setStatus(LeaveStatus.CANCELLED);
        leaveRequest.setUpdatedAt(LocalDateTime.now());
        leaveRequest.setApprovedRejectedDate(LocalDateTime.now());

        LeaveRequest updatedLeave = leaveRequestRepository.save(leaveRequest);
        return convertToDTO(updatedLeave);
    }

    /**
     * Update leave request (only if status is PENDING)
     */
    public LeaveRequestDTO updateLeaveRequest(Long leaveRequestId, LeaveRequestDTO leaveRequestDTO) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new BusinessLogicException("Can only update leave requests with PENDING status");
        }

        // Calculate new leave days
        Integer newLeaveDays = LeaveCalculationUtil.calculateLeaveDays(
                leaveRequestDTO.getStartDate(),
                leaveRequestDTO.getEndDate()
        );

        // Check if new balance is sufficient
        Employee employee = leaveRequest.getEmployee();
        if (employee.getRemainingLeaves() < newLeaveDays) {
            throw new BusinessLogicException("Insufficient leave balance for the new dates");
        }

        leaveRequest.setStartDate(leaveRequestDTO.getStartDate());
        leaveRequest.setEndDate(leaveRequestDTO.getEndDate());
        leaveRequest.setNumberOfDays(newLeaveDays);
        leaveRequest.setReason(leaveRequestDTO.getReason());
        leaveRequest.setLeaveType(LeaveType.valueOf(leaveRequestDTO.getLeaveType().toUpperCase()));
        leaveRequest.setUpdatedAt(LocalDateTime.now());

        LeaveRequest updatedLeave = leaveRequestRepository.save(leaveRequest);
        return convertToDTO(updatedLeave);
    }

    /**
     * Get all pending leave requests
     */
    public Page<LeaveRequestDTO> getPendingLeaveRequests(Pageable pageable) {
        Page<LeaveRequest> leaves = leaveRequestRepository.findByStatus(LeaveStatus.PENDING, pageable);
        return leaves.map(this::convertToDTO);
    }

    /**
     * Get all leave requests
     */
    public Page<LeaveRequestDTO> getAllLeaveRequests(Pageable pageable) {
        Page<LeaveRequest> leaves = leaveRequestRepository.findAll(pageable);
        return leaves.map(this::convertToDTO);
    }

    /**
     * Get leave requests with filters
     */
    public Page<LeaveRequestDTO> getLeaveRequestsWithFilters(
            Long employeeId, String status, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LeaveStatus leaveStatus = status != null ? LeaveStatus.valueOf(status.toUpperCase()) : null;
        Page<LeaveRequest> leaves = leaveRequestRepository.findWithFilters(
                employeeId, leaveStatus, startDate, endDate, pageable
        );
        return leaves.map(this::convertToDTO);
    }

    /**
     * Get leave requests by department and optionally by status
     */
    public Page<LeaveRequestDTO> getLeaveRequestsByDepartment(
            String department, String status, Pageable pageable) {
        LeaveStatus leaveStatus = status != null ? LeaveStatus.valueOf(status.toUpperCase()) : null;
        Page<LeaveRequest> leaves = leaveRequestRepository.findByDepartmentAndStatus(
                department, leaveStatus, pageable
        );
        return leaves.map(this::convertToDTO);
    }

    /**
     * Convert LeaveRequest entity to DTO
     */
    private LeaveRequestDTO convertToDTO(LeaveRequest leaveRequest) {
        LeaveRequestDTO dto = modelMapper.map(leaveRequest, LeaveRequestDTO.class);
        dto.setEmployeeId(leaveRequest.getEmployee().getId());
        dto.setEmployeeName(leaveRequest.getEmployee().getName());
        dto.setDepartment(leaveRequest.getEmployee().getDepartment());
        dto.setLeaveType(leaveRequest.getLeaveType().name());
        dto.setStatus(leaveRequest.getStatus().name());
        if (leaveRequest.getAppliedDate() != null) {
            dto.setAppliedDate(leaveRequest.getAppliedDate().toString());
        }
        if (leaveRequest.getApprovedRejectedDate() != null) {
            dto.setApprovedRejectedDate(leaveRequest.getApprovedRejectedDate().toString());
        }
        return dto;
    }

    /**
     * Count pending requests
     */
    public long getPendingRequestsCount() {
        return leaveRequestRepository.countByStatus(LeaveStatus.PENDING);
    }

    /**
     * Count approved requests
     */
    public long getApprovedRequestsCount() {
        return leaveRequestRepository.countByStatus(LeaveStatus.APPROVED);
    }

    /**
     * Count rejected requests
     */
    public long getRejectedRequestsCount() {
        return leaveRequestRepository.countByStatus(LeaveStatus.REJECTED);
    }
}
