package com.leavemgmt.service;

import com.leavemgmt.dto.AuthResponseDTO;
import com.leavemgmt.dto.LoginDTO;
import com.leavemgmt.entity.Employee;
import com.leavemgmt.entity.Manager;
import com.leavemgmt.exception.BusinessLogicException;
import com.leavemgmt.repository.EmployeeRepository;
import com.leavemgmt.repository.ManagerRepository;
import com.leavemgmt.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * AuthenticationService - Service for handling authentication and login
 */
@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Authenticate user and generate JWT token
     */
    public AuthResponseDTO login(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = tokenProvider.generateToken(userDetails.getUsername());

            // Fetch user details based on username (could be employee or manager)
            Optional<Employee> employee = employeeRepository.findByEmployeeIdOrEmail(
                    loginDTO.getUsername(), loginDTO.getUsername()
            );

            if (employee.isPresent()) {
                Employee emp = employee.get();
                AuthResponseDTO response = new AuthResponseDTO();
                response.setToken(token);
                response.setId(emp.getId());
                response.setEmployeeId(emp.getEmployeeId());
                response.setName(emp.getName());
                response.setEmail(emp.getEmail());
                response.setRole(emp.getRole().name());
                response.setRemainingLeaves(emp.getRemainingLeaves());
                return response;
            }

            Optional<Manager> manager = managerRepository.findByEmployeeIdOrEmail(
                    loginDTO.getUsername(), loginDTO.getUsername()
            );

            if (manager.isPresent()) {
                Manager mgr = manager.get();
                AuthResponseDTO response = new AuthResponseDTO();
                response.setToken(token);
                response.setId(mgr.getId());
                response.setEmployeeId(mgr.getEmployeeId());
                response.setName(mgr.getName());
                response.setEmail(mgr.getEmail());
                response.setRole(mgr.getRole().name());
                return response;
            }

            throw new BusinessLogicException("User not found after successful authentication");

        } catch (Exception e) {
            throw new BusinessLogicException("Invalid username or password");
        }
    }

    /**
     * Logout user
     */
    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
