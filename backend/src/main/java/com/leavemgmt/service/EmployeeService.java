package com.leavemgmt.service;

import com.leavemgmt.dto.EmployeeDTO;
import com.leavemgmt.dto.EmployeeRegistrationDTO;
import com.leavemgmt.entity.Employee;
import com.leavemgmt.entity.UserRole;
import com.leavemgmt.exception.BusinessLogicException;
import com.leavemgmt.exception.ResourceNotFoundException;
import com.leavemgmt.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * EmployeeService - Service for handling employee-related business logic
 */
@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Register a new employee
     */
    public EmployeeDTO registerEmployee(EmployeeRegistrationDTO registrationDTO) {
        // Check if employee ID already exists
        if (employeeRepository.findByEmployeeId(registrationDTO.getEmployeeId()).isPresent()) {
            throw new BusinessLogicException("Employee ID already exists: " + registrationDTO.getEmployeeId());
        }

        // Check if email already exists
        if (employeeRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new BusinessLogicException("Email already registered: " + registrationDTO.getEmail());
        }

        Employee employee = new Employee();
        employee.setEmployeeId(registrationDTO.getEmployeeId());
        employee.setName(registrationDTO.getName());
        employee.setEmail(registrationDTO.getEmail());
        employee.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        employee.setDepartment(registrationDTO.getDepartment());
        employee.setRole(UserRole.EMPLOYEE);
        employee.setTotalLeaves(20);
        employee.setRemainingLeaves(20);
        employee.setUsedLeaves(0);
        employee.setIsActive(true);

        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeDTO.class);
    }

    /**
     * Get employee by ID
     */
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    /**
     * Get employee by employee ID
     */
    public EmployeeDTO getEmployeeByEmployeeId(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    /**
     * Get all active employees
     */
    public List<EmployeeDTO> getAllActiveEmployees() {
        return employeeRepository.findByIsActive(true).stream()
                .map(emp -> modelMapper.map(emp, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Search employees by name, employee ID, or email
     */
    public List<EmployeeDTO> searchEmployees(String searchTerm) {
        return employeeRepository.searchEmployees(searchTerm).stream()
                .map(emp -> modelMapper.map(emp, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Update employee leave balance
     */
    public void updateLeaveBalance(Long employeeId, Integer daysUsed) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        if (employee.getRemainingLeaves() < daysUsed) {
            throw new BusinessLogicException("Insufficient leave balance. Available: " + employee.getRemainingLeaves());
        }

        employee.setRemainingLeaves(employee.getRemainingLeaves() - daysUsed);
        employee.setUsedLeaves(employee.getUsedLeaves() + daysUsed);
        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employee);
    }

    /**
     * Restore leave balance (when leave is rejected or cancelled)
     */
    public void restoreLeaveBalance(Long employeeId, Integer daysToRestore) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        employee.setRemainingLeaves(employee.getRemainingLeaves() + daysToRestore);
        employee.setUsedLeaves(Math.max(0, employee.getUsedLeaves() - daysToRestore));
        employee.setUpdatedAt(LocalDateTime.now());
        employeeRepository.save(employee);
    }

    /**
     * Get total active employees count
     */
    public long getTotalEmployeesCount() {
        return employeeRepository.countByIsActive(true);
    }

    /**
     * Get employees by department
     */
    public List<EmployeeDTO> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department).stream()
                .map(emp -> modelMapper.map(emp, EmployeeDTO.class))
                .collect(Collectors.toList());
    }
}
