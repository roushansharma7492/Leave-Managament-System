import axios from 'axios';
import { AuthResponse, LeaveRequestForm, LeaveApproval } from './types';

const baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const client = axios.create({
  baseURL,
  headers: { 'Content-Type': 'application/json' },
});

client.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token && config.headers) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const login = async (username: string, password: string): Promise<AuthResponse> => {
  const response = await client.post('/auth/login', { username, password });
  return response.data;
};

export const registerManager = async (payload: {
  employeeId: string;
  name: string;
  email: string;
  password: string;
  department: string;
}) => {
  const response = await client.post('/auth/register-manager', payload);
  return response.data;
};

export const registerEmployee = async (payload: {
  employeeId: string;
  name: string;
  email: string;
  password: string;
  department: string;
}) => {
  const response = await client.post('/auth/register', payload);
  return response.data;
};

export const fetchEmployeeDashboard = async () => {
  const response = await client.get('/employees/dashboard');
  return response.data;
};

export const applyLeave = async (payload: LeaveRequestForm) => {
  const response = await client.post('/leaves/apply', payload);
  return response.data;
};

export const fetchLeaveHistory = async (page = 0, size = 10) => {
  const response = await client.get('/leaves/history', { params: { page, size } });
  return response.data.content as any[];
};

export const cancelLeave = async (id: number) => {
  const response = await client.delete(`/leaves/${id}`);
  return response.data;
};

export const updateLeave = async (id: number, payload: LeaveRequestForm) => {
  const response = await client.put(`/leaves/${id}`, payload);
  return response.data;
};

export const fetchManagerDashboard = async () => {
  const response = await client.get('/managers/dashboard');
  return response.data;
};

export const fetchPendingLeaves = async (page = 0, size = 10) => {
  const response = await client.get('/leaves/pending/all', { params: { page, size } });
  return response.data.content as any[];
};

export const approveLeave = async (payload: LeaveApproval) => {
  const response = await client.post('/managers/leaves/approve', payload);
  return response.data;
};

export const rejectLeave = async (payload: LeaveApproval) => {
  const response = await client.post('/managers/leaves/reject', payload);
  return response.data;
};

export const fetchEmployeesByDepartment = async (department: string) => {
  const response = await client.get(`/employees/department/${encodeURIComponent(department)}`);
  return response.data;
};
