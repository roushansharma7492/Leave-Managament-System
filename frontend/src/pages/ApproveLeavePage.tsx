import { useEffect, useState } from 'react';
import { fetchPendingLeaves, approveLeave, rejectLeave } from '../api';
import { LeaveHistoryItem, LeaveApproval } from '../types';

const ApproveLeavePage = () => {
  const [pendingLeaves, setPendingLeaves] = useState<LeaveHistoryItem[]>([]);
  const [error, setError] = useState('');

  useEffect(() => {
    loadPending();
  }, []);

  const loadPending = () => {
    fetchPendingLeaves()
      .then(setPendingLeaves)
      .catch(() => setError('Unable to load pending requests.'));
  };

  const handleDecision = async (id: number, approved: boolean) => {
    setError('');
    try {
      const payload: LeaveApproval = {
        leaveRequestId: id,
        status: approved ? 'APPROVED' : 'REJECTED',
      };

      if (approved) {
        await approveLeave(payload);
      } else {
        await rejectLeave(payload);
      }
      loadPending();
    } catch (e: any) {
      setError(e?.response?.data?.message || 'Unable to update request.');
    }
  };

  return (
    <div className="container-glow p-4">
      <div className="card card-glow p-4">
        <h3 className="text-gradient">Approve Leave Requests</h3>
        <p className="text-white-50">Review requests from your team and approve or reject them.</p>

        {error && <div className="alert alert-danger">{error}</div>}

        <div className="table-responsive mt-4">
          <table className="table table-borderless table-striped align-middle text-white-75">
            <thead>
              <tr>
                <th>Employee</th>
                <th>Department</th>
                <th>Date Range</th>
                <th>Type</th>
                <th>Reason</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {pendingLeaves.length === 0 ? (
                <tr>
                  <td colSpan={6} className="text-center text-white-50 py-4">
                    No pending leave requests.
                  </td>
                </tr>
              ) : (
                pendingLeaves.map((leave) => (
                  <tr key={leave.id}>
                    <td>{leave.employeeName}</td>
                    <td>{leave.department}</td>
                    <td>{leave.startDate} → {leave.endDate}</td>
                    <td>{leave.leaveType}</td>
                    <td>{leave.reason}</td>
                    <td>
                      <button
                        className="btn btn-sm btn-success me-2"
                        onClick={() => handleDecision(leave.id, true)}
                      >
                        Approve
                      </button>
                      <button
                        className="btn btn-sm btn-danger"
                        onClick={() => handleDecision(leave.id, false)}
                      >
                        Reject
                      </button>
                    </td>
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

export default ApproveLeavePage;
