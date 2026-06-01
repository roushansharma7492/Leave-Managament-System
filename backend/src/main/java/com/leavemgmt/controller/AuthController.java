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
 * AuthController - Handles authentication and authorization endpoints
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

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
     * Register employee endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerEmployee(@Valid @RequestBody EmployeeRegistrationDTO registrationDTO) {
        var response = employeeService.registerEmployee(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Logout endpoint
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authenticationService.logout();
        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Service is running");
    }
}
