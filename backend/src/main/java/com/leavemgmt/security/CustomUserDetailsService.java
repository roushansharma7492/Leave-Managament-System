package com.leavemgmt.security;

import com.leavemgmt.entity.Employee;
import com.leavemgmt.entity.Manager;
import com.leavemgmt.entity.UserRole;
import com.leavemgmt.repository.EmployeeRepository;
import com.leavemgmt.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * CustomUserDetailsService - Loads user details for authentication
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to find employee first
        Optional<Employee> employee = employeeRepository.findByEmployeeIdOrEmail(username, username);
        if (employee.isPresent()) {
            Employee emp = employee.get();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + emp.getRole().name()));
            
            return new CustomUserPrincipal(
                    emp.getId(),
                    emp.getEmployeeId(),
                    emp.getEmail(),
                    emp.getPassword(),
                    authorities
            );
        }

        // Try to find manager
        Optional<Manager> manager = managerRepository.findByEmployeeIdOrEmail(username, username);
        if (manager.isPresent()) {
            Manager mgr = manager.get();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + mgr.getRole().name()));
            
            return new CustomUserPrincipal(
                    mgr.getId(),
                    mgr.getEmployeeId(),
                    mgr.getEmail(),
                    mgr.getPassword(),
                    authorities
            );
        }

        throw new UsernameNotFoundException("User not found with username or email: " + username);
    }
}
