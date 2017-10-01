package com.sample.payroll_api_cassandra;

import java.util.LinkedList;
import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

//Service functions
public class PayrollService {
	private Cluster cluster;
	private Session session;
	private String keyspace;
	private String table;
	
	public PayrollService(Cluster cluster, Session session, String keyspace, String table) {
		this.cluster = cluster;
		this.session = session;
		this.keyspace = keyspace;
		this.table = table;
	}
	
	//Add a new employee
	public String addEmployee(Employee employee) {
		this.session.execute("INSERT INTO " + this.keyspace + "." + this.table + 
				"(employee_id, first_name, last_name, salary, new_hire) VALUES ('" +
				employee.getID() + "', '" + employee.getFirst() + "', '" + employee.getLast() +
				"', " + employee.getSalary() + ", " + employee.getHireStatus() + ")");
		return "Added employee " + employee.getID();
	}
	
	//Get list of employees
	public List<Employee> getEmployees() {
		ResultSet allHires = this.session.execute("SElECT * FROM " + this.keyspace + "." + this.table + ";");
		List<Employee> employee_list = new LinkedList<Employee>();
		
		if (allHires != null) {			
			for(Row row : allHires) {
				System.out.println("Employee " + row.getString("employee_id") + ": "
						+ row.getString("first_name") + " " + row.getString("last_name"));
				Employee employee = new Employee(row.getString("employee_id"), row.getString("first_name"),
						row.getString("last_name"), row.getDouble("salary"), row.getBool("new_hire"));
					employee_list.add(employee);
						System.out.println(employee);
			}
			
			return employee_list;
			
		}
		
		return null;
		
	}
	
	//Get Employee by id
	public Employee getEmployee(String id) {
		ResultSet result = this.session.execute("SELECT * FROM " + this.keyspace + "." + this.table + " WHERE employee_id='" + id + "';");
		
		if (result != null) {
			Row row = result.one();
				System.out.println("Employee " + row.getString("first_name") + " " + row.getString("last_name") + " was found.");
			Employee employee = new Employee(row.getString("employee_id"), row.getString("first_name"),
				row.getString("last_name"), row.getDouble("salary"), row.getBool("new_hire"));
				
			return employee;	
			
		}
		
		return null;
		
	}
	
	//Remove Employee from payroll
	public Employee removeFromPayroll(String id) {
		ResultSet result = this.session.execute("SELECT * FROM " + this.keyspace + "." + this.table + " WHERE employee_id='" + id + "';");
		Row row = result.one();
		Employee employee = new Employee(row.getString("employee_id"), row.getString("first_name"),
				row.getString("last_name"), row.getDouble("salary"), row.getBool("new_hire"));
		System.out.println(row.getString("first_name") + " " + row.getString("last_name") + " will be removed from payroll.");
		this.session.execute("DELETE from " + this.keyspace + "." + this.table + " WHERE employee_id='" + id + "';");
		
		return employee;
		
	}
	
	//Update an employee
	public Employee updateEmployee(String id, Employee updated_employee) {
		
		if (updated_employee != null) {
			this.session.execute("UPDATE " 
				+ this.keyspace + "." + this.table + " SET salary=" + updated_employee.getSalary()
				+ ", new_hire=" + updated_employee.getHireStatus() + " WHERE employee_id='" + id + "';");
			ResultSet updated = this.session.execute("SELECT * FROM " + this.keyspace + "." + this.table + " WHERE employee_id='" + id + "';");
			Row row = updated.one();
			Employee employee = new Employee(row.getString("employee_id"), row.getString("first_name"),
				row.getString("last_name"), row.getDouble("salary"), row.getBool("new_hire"));
			
			return employee;
			
		} 
		
		return null;
		
	}

}