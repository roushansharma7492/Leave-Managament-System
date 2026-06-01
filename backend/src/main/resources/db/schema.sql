-- MySQL Database Schema for Employee Leave Management System

-- Create Database
CREATE DATABASE IF NOT EXISTS employee_leave_db;
USE employee_leave_db;

-- Drop existing tables if they exist
DROP TABLE IF EXISTS leave_requests;
DROP TABLE IF EXISTS managers;
DROP TABLE IF EXISTS employees;

-- Create Employees Table
CREATE TABLE employees (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    department VARCHAR(50) NOT NULL,
    role ENUM('EMPLOYEE', 'MANAGER') NOT NULL,
    total_leaves INT NOT NULL DEFAULT 20,
    remaining_leaves INT NOT NULL DEFAULT 20,
    used_leaves INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_employee_id (employee_id),
    INDEX idx_email (email),
    INDEX idx_department (department),
    INDEX idx_is_active (is_active)
);

-- Create Managers Table
CREATE TABLE managers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    department VARCHAR(50) NOT NULL,
    role ENUM('EMPLOYEE', 'MANAGER') NOT NULL DEFAULT 'MANAGER',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_employee_id (employee_id),
    INDEX idx_email (email)
);

-- Create Leave Requests Table
CREATE TABLE leave_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    leave_type ENUM('CASUAL', 'SICK', 'EARNED') NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    number_of_days INT NOT NULL,
    reason VARCHAR(1000),
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    manager_comment VARCHAR(500),
    manager_id BIGINT,
    approved_rejected_date TIMESTAMP NULL,
    applied_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    INDEX idx_employee_id (employee_id),
    INDEX idx_status (status),
    INDEX idx_applied_date (applied_date),
    INDEX idx_start_date (start_date),
    INDEX idx_end_date (end_date)
);

-- Sample Data - Employees
INSERT INTO employees (employee_id, name, email, password, department, role, total_leaves, remaining_leaves, used_leaves) VALUES
('EMP001', 'John Doe', 'john.doe@company.com', '$2a$10$7E/6.1E0B5.8.2.6.9.8uKb2V7M9k7m9k7m9k7m9k7m9k7m9k7m9', 'IT', 'EMPLOYEE', 20, 20, 0),
('EMP002', 'Jane Smith', 'jane.smith@company.com', '$2a$10$7E/6.1E0B5.8.2.6.9.8uKb2V7M9k7m9k7m9k7m9k7m9k7m9k7m9', 'HR', 'EMPLOYEE', 20, 18, 2),
('EMP003', 'Michael Johnson', 'michael.j@company.com', '$2a$10$7E/6.1E0B5.8.2.6.9.8uKb2V7M9k7m9k7m9k7m9k7m9k7m9k7m9', 'IT', 'EMPLOYEE', 20, 15, 5),
('EMP004', 'Sarah Wilson', 'sarah.w@company.com', '$2a$10$7E/6.1E0B5.8.2.6.9.8uKb2V7M9k7m9k7m9k7m9k7m9k7m9k7m9', 'Finance', 'EMPLOYEE', 20, 20, 0),
('EMP005', 'Robert Brown', 'robert.b@company.com', '$2a$10$7E/6.1E0B5.8.2.6.9.8uKb2V7M9k7m9k7m9k7m9k7m9k7m9k7m9', 'IT', 'EMPLOYEE', 20, 12, 8);

-- Sample Data - Managers
INSERT INTO managers (employee_id, name, email, password, department, role) VALUES
('MGR001', 'Manager Alice', 'alice.manager@company.com', '$2a$10$7E/6.1E0B5.8.2.6.9.8uKb2V7M9k7m9k7m9k7m9k7m9k7m9k7m9', 'IT', 'MANAGER'),
('MGR002', 'Manager Bob', 'bob.manager@company.com', '$2a$10$7E/6.1E0B5.8.2.6.9.8uKb2V7M9k7m9k7m9k7m9k7m9k7m9k7m9', 'HR', 'MANAGER');

-- Sample Data - Leave Requests
INSERT INTO leave_requests (employee_id, leave_type, start_date, end_date, number_of_days, reason, status, applied_date) VALUES
(1, 'CASUAL', '2024-07-01', '2024-07-05', 5, 'Family vacation', 'PENDING', NOW()),
(2, 'SICK', '2024-06-15', '2024-06-16', 2, 'Medical appointment', 'APPROVED', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(3, 'EARNED', '2024-07-10', '2024-07-14', 5, 'Personal work', 'PENDING', NOW()),
(4, 'CASUAL', '2024-06-20', '2024-06-22', 3, 'Vacation', 'REJECTED', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(5, 'SICK', '2024-07-08', '2024-07-08', 1, 'Dental checkup', 'APPROVED', DATE_SUB(NOW(), INTERVAL 2 DAY));
