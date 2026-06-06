import { FormEvent, useState } from 'react';
import { applyLeave } from '../api';
import { LeaveRequestForm } from '../types';

const ApplyLeavePage = () => {
  const [form, setForm] = useState<LeaveRequestForm>({
    leaveType: 'CASUAL',
    startDate: '',
    endDate: '',
    reason: '',
  });
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    setError('');
    setMessage('');

    try {
      await applyLeave(form);
      setMessage('Leave request submitted successfully.');
      setForm({ leaveType: 'CASUAL', startDate: '', endDate: '', reason: '' });
    } catch (e: any) {
      setError(e?.response?.data?.message || 'Unable to submit leave.');
    }
  };

  return (
    <div className="container-glow p-4">
      <div className="card card-glow p-4">
        <h3 className="text-gradient">Apply for Leave</h3>
        <p className="text-white-50">Submit a leave request with dates, leave type, and reason.</p>
        {message && <div className="alert alert-success">{message}</div>}
        {error && <div className="alert alert-danger">{error}</div>}

        <form onSubmit={handleSubmit}>
          <div className="row gy-3">
            <div className="col-md-6">
              <label className="form-label">Leave Type</label>
              <select
                className="form-select input-glow"
                value={form.leaveType}
                onChange={(event) => setForm({ ...form, leaveType: event.target.value })}
              >
                <option value="CASUAL">Casual</option>
                <option value="SICK">Sick</option>
                <option value="EARNED">Earned</option>
              </select>
            </div>
            <div className="col-md-3">
              <label className="form-label">Start Date</label>
              <input
                type="date"
                className="form-control input-glow"
                value={form.startDate}
                onChange={(event) => setForm({ ...form, startDate: event.target.value })}
              />
            </div>
            <div className="col-md-3">
              <label className="form-label">End Date</label>
              <input
                type="date"
                className="form-control input-glow"
                value={form.endDate}
                onChange={(event) => setForm({ ...form, endDate: event.target.value })}
              />
            </div>
            <div className="col-12">
              <label className="form-label">Reason</label>
              <textarea
                className="form-control input-glow"
                rows={5}
                value={form.reason}
                onChange={(event) => setForm({ ...form, reason: event.target.value })}
                placeholder="Describe why you need this leave"
              />
            </div>
          </div>
          <button type="submit" className="btn btn-primary btn-glow mt-4">
            Submit Request
          </button>
        </form>
      </div>
    </div>
  );
};

export default ApplyLeavePage;
