package com.leavemgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

/**
 * LoginDTO - Data Transfer Object for Login
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @NotBlank(message = "Username/Email/EmployeeID cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
