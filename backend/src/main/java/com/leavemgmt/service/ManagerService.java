package com.leavemgmt.service;

import com.leavemgmt.dto.LeaveApprovalDTO;
import com.leavemgmt.dto.LeaveRequestDTO;
import com.leavemgmt.dto.ManagerDTO;
import com.leavemgmt.dto.ManagerRegistrationDTO;
import com.leavemgmt.entity.LeaveRequest;
import com.leavemgmt.entity.LeaveStatus;
import com.leavemgmt.entity.Manager;
import com.leavemgmt.entity.UserRole;
import com.leavemgmt.exception.BusinessLogicException;
import com.leavemgmt.exception.ResourceNotFoundException;
import com.leavemgmt.repository.LeaveRequestRepository;
import com.leavemgmt.repository.ManagerRepository;
import com.leavemgmt.service.EmailNotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * ManagerService - Service for handling manager-related business logic
 */
@Service
@Transactional
public class ManagerService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Approve leave request
     */
    public LeaveRequestDTO approveLeaveRequest(LeaveApprovalDTO approvalDTO) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(approvalDTO.getLeaveRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new BusinessLogicException("Only pending leave requests can be approved");
        }

        leaveRequest.setStatus(LeaveStatus.APPROVED);
        leaveRequest.setManagerComment(approvalDTO.getManagerComment());
        leaveRequest.setApprovedRejectedDate(LocalDateTime.now());

        // Deduct leave balance from employee when manager approves
        employeeService.updateLeaveBalance(
                leaveRequest.getEmployee().getId(),
                leaveRequest.getNumberOfDays()
        );

        LeaveRequest updatedLeave = leaveRequestRepository.save(leaveRequest);
        emailNotificationService.sendLeaveDecisionEmail(
                leaveRequest.getEmployee().getEmail(),
                leaveRequest.getEmployee().getName(),
                leaveRequest.getStatus().name(),
                leaveRequest.getManagerComment()
        );

        return convertToDTO(updatedLeave);
    }

    /**
     * Reject leave request
     */
    public LeaveRequestDTO rejectLeaveRequest(LeaveApprovalDTO approvalDTO) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(approvalDTO.getLeaveRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new BusinessLogicException("Only pending leave requests can be rejected");
        }

        leaveRequest.setStatus(LeaveStatus.REJECTED);
        leaveRequest.setManagerComment(approvalDTO.getManagerComment());
        leaveRequest.setApprovedRejectedDate(LocalDateTime.now());

        LeaveRequest updatedLeave = leaveRequestRepository.save(leaveRequest);
        emailNotificationService.sendLeaveDecisionEmail(
                leaveRequest.getEmployee().getEmail(),
                leaveRequest.getEmployee().getName(),
                leaveRequest.getStatus().name(),
                leaveRequest.getManagerComment()
        );

        return convertToDTO(updatedLeave);
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
     * Register a new manager
     */
    public ManagerDTO registerManager(ManagerRegistrationDTO registrationDTO) {
        if (managerRepository.findByEmployeeId(registrationDTO.getEmployeeId()).isPresent()) {
            throw new BusinessLogicException("Manager ID already exists: " + registrationDTO.getEmployeeId());
        }

        if (managerRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new BusinessLogicException("Email already registered: " + registrationDTO.getEmail());
        }

        Manager manager = new Manager();
        manager.setEmployeeId(registrationDTO.getEmployeeId());
        manager.setName(registrationDTO.getName());
        manager.setEmail(registrationDTO.getEmail());
        manager.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        manager.setDepartment(registrationDTO.getDepartment());
        manager.setRole(UserRole.MANAGER);
        manager.setIsActive(true);

        Manager savedManager = managerRepository.save(manager);
        return modelMapper.map(savedManager, ManagerDTO.class);
    }
}
