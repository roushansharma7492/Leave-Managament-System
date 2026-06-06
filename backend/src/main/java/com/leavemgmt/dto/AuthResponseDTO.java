package com.leavemgmt.dto;

/**
 * AuthResponseDTO - Data Transfer Object for Authentication Response
 */
public class AuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String employeeId;
    private String name;
    private String email;
    private String role;
    private Integer remainingLeaves;

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(String token, String type, Long id, String employeeId, String name, String email, String role, Integer remainingLeaves) {
        this.token = token;
        this.type = type;
        this.id = id;
        this.employeeId = employeeId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.remainingLeaves = remainingLeaves;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getRemainingLeaves() {
        return remainingLeaves;
    }

    public void setRemainingLeaves(Integer remainingLeaves) {
        this.remainingLeaves = remainingLeaves;
    }
}
