import { FormEvent, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { registerManager } from '../api';

const ManagerRegistrationPage = () => {
  const navigate = useNavigate();
  const [employeeId, setEmployeeId] = useState('');
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [department, setDepartment] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    setError('');
    setSuccess('');

    try {
      await registerManager({ employeeId, name, email, password, department });
      setSuccess('Manager registered successfully. You can now login.');
      setTimeout(() => navigate('/login/manager'), 1200);
    } catch (err: any) {
      setError(err?.response?.data?.message || 'Registration failed. Please check your details.');
    }
  };

  return (
    <div className="d-flex align-items-center justify-content-center min-vh-100">
      <div className="col-12 col-md-8 col-lg-5 container-glow p-5">
        <div className="text-center mb-4">
          <h1 className="text-gradient">Manager Registration</h1>
          <p className="text-white-50">Create a manager account to access manager features.</p>
        </div>

        {error && <div className="alert alert-danger">{error}</div>}
        {success && <div className="alert alert-success">{success}</div>}

        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label">Manager ID</label>
            <input
              className="form-control input-glow"
              value={employeeId}
              onChange={(event) => setEmployeeId(event.target.value)}
              placeholder="MGR001"
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Name</label>
            <input
              className="form-control input-glow"
              value={name}
              onChange={(event) => setName(event.target.value)}
              placeholder="Manager Name"
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Email</label>
            <input
              type="email"
              className="form-control input-glow"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              placeholder="manager@example.com"
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Password</label>
            <input
              type="password"
              className="form-control input-glow"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              placeholder="Create a password"
            />
          </div>
          <div className="mb-4">
            <label className="form-label">Department</label>
            <input
              className="form-control input-glow"
              value={department}
              onChange={(event) => setDepartment(event.target.value)}
              placeholder="IT"
            />
          </div>
          <button type="submit" className="btn btn-primary btn-glow w-100">
            Register Manager
          </button>
        </form>

        <div className="mt-4 text-center text-white-50">
          <p>
            Already have an account? <Link to="/login/manager">Manager Login</Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default ManagerRegistrationPage;
