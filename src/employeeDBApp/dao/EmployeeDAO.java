package employeeDBApp.dao;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import employeeDBApp.model.Employee;
import employeeDBApp.util.DBConnection;

public class EmployeeDAO {
	public int addEmployee(Employee emp) throws SQLException {
        String sql = "INSERT INTO employees (name, department, salary, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, emp.getName());
            pst.setString(2, emp.getDepartment());
            pst.setDouble(3, emp.getSalary());
            pst.setString(4, emp.getEmail());
            int affected = pst.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet keys = pst.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    emp.setId(id);
                    return id;
                }
            }
            return -1;
        }
    }

    public Employee getEmployeeById(int id) throws SQLException {
        String sql = "SELECT id, name, department, salary, email FROM employees WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("salary"),
                        rs.getString("email")
                    );
                }
                return null;
            }
        }
    }

    public List<Employee> getAllEmployees() throws SQLException {
        String sql = "SELECT id, name, department, salary, email FROM employees ORDER BY id";
        List<Employee> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(new Employee(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getDouble("salary"),
                    rs.getString("email")
                ));
            }
        }
        return list;
    }

    public boolean updateEmployee(Employee emp) throws SQLException {
        String sql = "UPDATE employees SET name = ?, department = ?, salary = ?, email = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, emp.getName());
            pst.setString(2, emp.getDepartment());
            pst.setDouble(3, emp.getSalary());
            pst.setString(4, emp.getEmail());
            pst.setInt(5, emp.getId());
            int updated = pst.executeUpdate();
            return updated > 0;
        }
    }

    public boolean deleteEmployee(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            int deleted = pst.executeUpdate();
            return deleted > 0;
        }
    }
}


