package com.leavemgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

/**
 * EmployeeRegistrationDTO - Data Transfer Object for Employee Registration
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRegistrationDTO {

    @NotBlank(message = "Employee ID cannot be blank")
    private String employeeId;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 characters")
    private String password;

    @NotBlank(message = "Department cannot be blank")
    private String department;
}
