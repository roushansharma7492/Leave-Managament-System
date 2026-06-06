import { useEffect, useState } from 'react';
import { fetchLeaveHistory } from '../api';
import { LeaveHistoryItem } from '../types';

const LeaveHistoryPage = () => {
  const [history, setHistory] = useState<LeaveHistoryItem[]>([]);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchLeaveHistory()
      .then(setHistory)
      .catch(() => setError('Unable to load leave history.'));
  }, []);

  return (
    <div className="container-glow p-4">
      <div className="card card-glow p-4">
        <h3 className="text-gradient">My Leave History</h3>
        <p className="text-white-50">Review your leave requests and application status.</p>

        {error && <div className="alert alert-danger">{error}</div>}

        <div className="table-responsive mt-4">
          <table className="table table-borderless table-striped align-middle text-white-75">
            <thead>
              <tr>
                <th>Date Range</th>
                <th>Type</th>
                <th>Reason</th>
                <th>Status</th>
                <th>Applied On</th>
              </tr>
            </thead>
            <tbody>
              {history.length === 0 ? (
                <tr>
                  <td colSpan={5} className="text-center text-white-50 py-4">
                    No leave requests found.
                  </td>
                </tr>
              ) : (
                history.map((item) => (
                  <tr key={item.id}>
                    <td>{item.startDate} → {item.endDate}</td>
                    <td>{item.leaveType}</td>
                    <td>{item.reason}</td>
                    <td>{item.status}</td>
                    <td>{item.appliedDate}</td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default LeaveHistoryPage;
