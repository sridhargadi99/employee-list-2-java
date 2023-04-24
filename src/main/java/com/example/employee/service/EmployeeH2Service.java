/*
 * You can use the following import statements
 * 
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.*;
 * 
 */

// Write your code here
package com.example.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.model.EmployeeRowMapper;

@Service
public class EmployeeH2Service implements EmployeeRepository{
    @Autowired
    private JdbcTemplate db;
    
    @Override 
    public ArrayList<Employee> allEmployees(){
        List<Employee> employeeCollection = db.query("SELECT * FROM EMPLOYEELIST", new EmployeeRowMapper());
        ArrayList<Employee> employees = new ArrayList<>(employeeCollection);
        return employees;
    }

    @Override 
    public Employee addEmployee(Employee employee){
        db.update("INSERT INTO EMPLOYEELIST(employeeName, email, department) VALUES(?,?,?)", employee.getEmployeeName(), employee.getEmail(), employee.getDepartment());
        Employee employeeDetails = db.queryForObject("SELECT * FROM EMPLOYEELIST WHERE employeeName = ? and email = ? and department = ?", new EmployeeRowMapper(), employee.getEmployeeName(), employee.getEmail(), employee.getDepartment());
        return employeeDetails;
    }
    
    @Override 
    public Employee getEmployee(int employeeId){
        try{
            Employee employeeDetails = db.queryForObject("SELECT * FROM EMPLOYEELIST WHERE employeeId = ?", new EmployeeRowMapper(), employeeId);
            return employeeDetails;
        }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override 
    public Employee updateEmployee(int employeeId, Employee employee){
        if(employee.getEmployeeName() != null){
            db.update("UPDATE EMPLOYEELIST SET employeeName = ? WHERE employeeId = ? ", employee.getEmployeeName(), employeeId);
        }

        if(employee.getEmail() != null){
            db.update("UPDATE EMPLOYEELIST SET email = ? WHERE employeeId = ?", employee.getEmail(), employeeId);
        }

        if(employee.getDepartment() != null){
            db.update("UPDATE EMPLOYEELIST SET department = ? WHERE employeeId = ?", employee.getDepartment(), employeeId);
        }
        return getEmployee(employeeId);
    }

    @Override 
    public void deleteEmployee(int employeeId){
        db.update("DELETE FROM EMPLOYEELIST WHERE employeeId = ? ", employeeId);
        
    }
    
}
