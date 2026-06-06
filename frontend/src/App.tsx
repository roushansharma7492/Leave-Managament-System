import { Navigate, Route, Routes } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import EmployeeRegistrationPage from './pages/EmployeeRegistrationPage';
import ManagerRegistrationPage from './pages/ManagerRegistrationPage';
import EmployeeDashboard from './pages/EmployeeDashboard';
import ManagerDashboard from './pages/ManagerDashboard';
import LeaveHistoryPage from './pages/LeaveHistoryPage';
import ApplyLeavePage from './pages/ApplyLeavePage';
import ApproveLeavePage from './pages/ApproveLeavePage';
import EmployeeSearchPage from './pages/EmployeeSearchPage';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout';

function App() {
  return (
    <Routes>
      <Route path="/login/employee" element={<LoginPage role="EMPLOYEE" />} />
      <Route path="/login/manager" element={<LoginPage role="MANAGER" />} />
      <Route path="/register/employee" element={<EmployeeRegistrationPage />} />
      <Route path="/register/manager" element={<ManagerRegistrationPage />} />
      <Route path="/" element={<Navigate to="/login/employee" replace />} />

      <Route path="/app" element={<ProtectedRoute />}>
        <Route element={<Layout />}>
          <Route path="employee/dashboard" element={<EmployeeDashboard />} />
          <Route path="employee/apply" element={<ApplyLeavePage />} />
          <Route path="employee/history" element={<LeaveHistoryPage />} />
          <Route path="manager/dashboard" element={<ManagerDashboard />} />
          <Route path="manager/approvals" element={<ApproveLeavePage />} />
          <Route path="manager/employees" element={<EmployeeSearchPage />} />
        </Route>
      </Route>
    </Routes>
  );
}

export default App;
