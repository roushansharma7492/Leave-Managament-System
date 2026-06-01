package com.leavemgmt.service;

import com.leavemgmt.dto.LeaveApprovalDTO;
import com.leavemgmt.dto.LeaveRequestDTO;
import com.leavemgmt.entity.LeaveRequest;
import com.leavemgmt.entity.LeaveStatus;
import com.leavemgmt.exception.BusinessLogicException;
import com.leavemgmt.exception.ResourceNotFoundException;
import com.leavemgmt.repository.LeaveRequestRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ModelMapper modelMapper;

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

        // Deduct leave balance from employee
        employeeService.updateLeaveBalance(
                leaveRequest.getEmployee().getId(),
                leaveRequest.getNumberOfDays()
        );

        LeaveRequest updatedLeave = leaveRequestRepository.save(leaveRequest);
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
}
