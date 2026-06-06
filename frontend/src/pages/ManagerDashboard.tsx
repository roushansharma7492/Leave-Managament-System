import { useEffect, useState } from 'react';
import { fetchManagerDashboard } from '../api';
import { ManagerSummary } from '../types';

const ManagerDashboard = () => {
  const [summary, setSummary] = useState<ManagerSummary | null>(null);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchManagerDashboard()
      .then(setSummary)
      .catch(() => setError('Unable to load manager dashboard.'));
  }, []);

  return (
    <div className="container-glow p-4">
      <div className="card card-glow p-4 mb-4">
        <h2 className="text-gradient">Manager Overview</h2>
        <p className="text-white-50">Monitor your team’s leave workload and approvals.</p>
      </div>

      <div className="row gy-4 mb-4">
        <div className="col-md-4">
          <div className="card card-glow-sm p-4 h-100">
            <h6>Pending Requests</h6>
            <p className="display-5 mb-0">{summary?.pendingRequests ?? '–'}</p>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card card-glow-sm p-4 h-100">
            <h6>Total Employees</h6>
            <p className="display-5 mb-0">{summary?.totalEmployees ?? '–'}</p>
          </div>
        </div>
        <div className="col-md-4">
          <div className="card card-glow-sm p-4 h-100">
            <h6>Approved Requests</h6>
            <p className="display-5 mb-0">{summary?.approvedRequests ?? '–'}</p>
          </div>
        </div>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}
    </div>
  );
};

export default ManagerDashboard;
