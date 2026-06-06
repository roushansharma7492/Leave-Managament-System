import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';

const links = [
  { path: '/app/employee/dashboard', label: 'Employee Dashboard', role: 'EMPLOYEE' },
  { path: '/app/employee/apply', label: 'Apply Leave', role: 'EMPLOYEE' },
  { path: '/app/employee/history', label: 'Leave History', role: 'EMPLOYEE' },
  { path: '/app/manager/dashboard', label: 'Manager Dashboard', role: 'MANAGER' },
  { path: '/app/manager/approvals', label: 'Approve Leaves', role: 'MANAGER' },
  { path: '/app/manager/employees', label: 'Search Employees', role: 'MANAGER' },
];

function Layout() {
  const navigate = useNavigate();
  const [role, setRole] = useState<string | null>(null);

  useEffect(() => {
    setRole(localStorage.getItem('role'));
  }, []);

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('name');
    localStorage.removeItem('employeeId');
    navigate('/login/employee');
  };

  return (
    <div>
      <nav className="navbar navbar-expand-lg navbar-glow py-3 px-4">
        <div className="container-fluid">
          <NavLink className="navbar-brand text-white fw-bold" to={role === 'MANAGER' ? '/app/manager/dashboard' : '/app/employee/dashboard'}>
            LeaveMgmt
          </NavLink>
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarSupportedContent">
            <ul className="navbar-nav me-auto mb-2 mb-lg-0">
              {links.filter(link => link.role === role).map(link => (
                <li className="nav-item" key={link.path}>
                  <NavLink className="nav-link" to={link.path}>
                    {link.label}
                  </NavLink>
                </li>
              ))}
            </ul>
            <div className="d-flex align-items-center gap-3">
              <span className="text-white opacity-75">{localStorage.getItem('name')}</span>
              <button className="btn btn-outline-light btn-sm" onClick={logout}>Logout</button>
            </div>
          </div>
        </div>
      </nav>
      <main className="container py-5">
        <Outlet />
      </main>
      <footer className="text-center py-4 opacity-75">
        Employee Leave Management System © 2026
      </footer>
    </div>
  );
}

export default Layout;
