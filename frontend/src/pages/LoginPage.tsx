import { FormEvent, useEffect, useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { login } from '../api';
import { AuthResponse } from '../types';

interface LoginProps {
  role: 'EMPLOYEE' | 'MANAGER';
}

const LoginPage = ({ role }: LoginProps) => {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      const storedRole = localStorage.getItem('role');
      if (storedRole === 'MANAGER') {
        navigate('/app/manager/dashboard');
      } else {
        navigate('/app/employee/dashboard');
      }
    }
  }, []);

  const handleLogin = async (event: FormEvent) => {
    event.preventDefault();
    setError('');
    try {
      const data: AuthResponse = await login(username, password);
      if (data.role !== role) {
        setError(`Please log in using the ${role.toLowerCase()} portal.`);
        return;
      }
      localStorage.setItem('token', data.token);
      localStorage.setItem('role', data.role);
      localStorage.setItem('name', data.name);
      localStorage.setItem('employeeId', data.employeeId);
      if (data.role === 'MANAGER') {
        navigate('/app/manager/dashboard');
      } else {
        navigate('/app/employee/dashboard');
      }
    } catch (e: any) {
      setError(e?.response?.data?.message || 'Login failed. Please check your credentials.');
    }
  };

  return (
    <div className="d-flex align-items-center justify-content-center min-vh-100">
      <div className="col-12 col-md-8 col-lg-5 container-glow p-5">
        <div className="text-center mb-4">
          <h1 className="text-gradient">{role === 'MANAGER' ? 'Manager Login' : 'Employee Login'}</h1>
          <p className="text-white-50">Secure access for {role === 'MANAGER' ? 'managers' : 'employees'}.</p>
        </div>

        {error && <div className="alert alert-danger">{error}</div>}

        <form onSubmit={handleLogin}>
          <div className="mb-3">
            <label className="form-label">Employee ID or Email</label>
            <input
              className="form-control input-glow"
              value={username}
              onChange={(event) => setUsername(event.target.value)}
              placeholder="EMP001 or john.doe@company.com"
            />
          </div>
          <div className="mb-4">
            <label className="form-label">Password</label>
            <input
              type="password"
              className="form-control input-glow"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              placeholder="Enter your password"
            />
          </div>
          <button type="submit" className="btn btn-primary btn-glow w-100">
            Continue
          </button>
        </form>

        <div className="mt-4 text-center text-white-50">
          {role === 'MANAGER' ? (
            <>
              <p>
                Employee? <Link to="/login/employee">Switch to Employee Login</Link>
              </p>
              <p>
                New manager? <Link to="/register/manager">Register here</Link>
              </p>
            </>
          ) : (
            <>
              <p>
                Manager? <Link to="/login/manager">Go to Manager Login</Link>
              </p>
              <p>
                New employee? <Link to="/register/employee">Register here</Link>
              </p>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
