export interface AuthResponse {
  token: string;
  type: string;
  id: number;
  employeeId: string;
  name: string;
  email: string;
  role: string;
  remainingLeaves?: number;
}

export interface EmployeeSummary {
  id: number;
  employeeId: string;
  name: string;
  email: string;
  department: string;
  role: string;
  totalLeaves: number;
  remainingLeaves: number;
  usedLeaves: number;
}

export interface ManagerSummary {
  totalEmployees: number;
  pendingRequests: number;
  approvedRequests: number;
  rejectedRequests: number;
  totalLeaveRequests: number;
}

export interface LeaveRequestForm {
  leaveType: string;
  startDate: string;
  endDate: string;
  reason: string;
}

export interface LeaveHistoryItem {
  id: number;
  leaveType: string;
  startDate: string;
  endDate: string;
  numberOfDays: number;
  reason: string;
  status: string;
  managerComment?: string;
  employeeName?: string;
  appliedDate: string;
  approvedRejectedDate?: string;
  department?: string;
}

export interface LeaveApproval {
  leaveRequestId: number;
  status: 'APPROVED' | 'REJECTED';
  managerComment?: string;
}

export interface EmployeeSearchResult {
  id: number;
  employeeId: string;
  name: string;
  email: string;
  department: string;
  role: string;
  remainingLeaves: number;
}

export type PendingLeaveItem = LeaveHistoryItem;
