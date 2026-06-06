import { useEffect, useState } from 'react';
import { fetchEmployeesByDepartment } from '../api';
import { EmployeeSearchResult } from '../types';

const EmployeeSearchPage = () => {
  const [department, setDepartment] = useState('');
  const [results, setResults] = useState<EmployeeSearchResult[]>([]);
  const [error, setError] = useState('');

  useEffect(() => {
    setDepartment(localStorage.getItem('department') || '');
  }, []);

  const handleSearch = async () => {
    setError('');
    try {
      const employees = await fetchEmployeesByDepartment(department);
      setResults(employees);
    } catch (e: any) {
      setError(e?.response?.data?.message || 'Unable to search employees.');
    }
  };

  return (
    <div className="container-glow p-4">
      <div className="card card-glow p-4 mb-4">
        <h3 className="text-gradient">Employee Search</h3>
        <p className="text-white-50">Search employees by department and view their available leave balance.</p>
        <div className="row g-3 align-items-end">
          <div className="col-md-6">
            <label className="form-label">Department</label>
            <input
              className="form-control input-glow"
              value={department}
              onChange={(event) => setDepartment(event.target.value)}
              placeholder="e.g. Sales, Engineering"
            />
          </div>
          <div className="col-md-2">
            <button className="btn btn-primary btn-glow w-100" onClick={handleSearch}>
              Search
            </button>
          </div>
        </div>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      <div className="table-responsive">
        <table className="table table-borderless table-striped align-middle text-white-75">
          <thead>
            <tr>
              <th>Employee</th>
              <th>ID</th>
              <th>Email</th>
              <th>Department</th>
              <th>Remaining Leaves</th>
            </tr>
          </thead>
          <tbody>
            {results.length === 0 ? (
              <tr>
                <td colSpan={5} className="text-center text-white-50 py-4">
                  Enter a department and select search.
                </td>
              </tr>
            ) : (
              results.map((employee) => (
                <tr key={employee.id}>
                  <td>{employee.name}</td>
                  <td>{employee.employeeId}</td>
                  <td>{employee.email}</td>
                  <td>{employee.department}</td>
                  <td>{employee.remainingLeaves}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default EmployeeSearchPage;
