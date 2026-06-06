import { useEffect, useState } from 'react';
import { fetchEmployeeDashboard } from '../api';
import { EmployeeSummary } from '../types';

const EmployeeDashboard = () => {
  const [employee, setEmployee] = useState<EmployeeSummary | null>(null);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchEmployeeDashboard()
      .then(setEmployee)
      .catch(() => setError('Unable to load dashboard.'));
  }, []);

  return (
    <div className="container-glow p-4">
      <div className="row mb-4">
        <div className="col-lg-8">
          <div className="card card-glow p-4">
            <h2 className="text-gradient">Welcome back, {employee?.name ?? 'Employee'}</h2>
            <p className="text-white-50">Track your leave balance, history, and submit requests from one panel.</p>
          </div>
        </div>
        <div className="col-lg-4">
          <div className="card card-glow p-4 h-100">
            <h5>Account Details</h5>
            <ul className="list-unstyled text-white-75">
              <li><strong>Employee ID:</strong> {employee?.employeeId}</li>
              <li><strong>Email:</strong> {employee?.email}</li>
              <li><strong>Department:</strong> {employee?.department}</li>
              <li><strong>Role:</strong> {employee?.role}</li>
            </ul>
          </div>
        </div>
      </div>
      <div className="row gy-4">
        <div className="col-md-4">
          <div className="card card-glow-sm p-4 h-100">
            <h6>Total Leave Balance</h6>
            <p className="display-6 mb-0">{employee?.totalLeaves ?? '–'}</p>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card card-glow-sm p-4 h-100">
            <h6>Leaves Used</h6>
            <p className="display-6 mb-0">{employee?.usedLeaves ?? '–'}</p>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card card-glow-sm p-4 h-100">
            <h6>Remaining Leaves</h6>
            <p className="display-6 mb-0">{employee?.remainingLeaves ?? '–'}</p>
          </div>
        </div>
      </div>
      {error && <div className="alert alert-danger mt-4">{error}</div>}
    </div>
  );
};

export default EmployeeDashboard;
