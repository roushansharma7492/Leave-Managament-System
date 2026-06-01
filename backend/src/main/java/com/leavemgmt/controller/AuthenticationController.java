package com.leavemgmt.controller;

import com.leavemgmt.dto.AuthResponseDTO;
import com.leavemgmt.dto.EmployeeRegistrationDTO;
import com.leavemgmt.dto.LoginDTO;
import com.leavemgmt.service.AuthenticationService;
import com.leavemgmt.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthenticationController - Handles authentication endpoints
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * Login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        AuthResponseDTO response = authenticationService.login(loginDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Employee registration endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerEmployee(@Valid @RequestBody EmployeeRegistrationDTO registrationDTO) {
        employeeService.registerEmployee(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Employee registered successfully");
    }

    /**
     * Logout endpoint
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        authenticationService.logout();
        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("API is running");
    }
}
