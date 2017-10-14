package com.sample.payroll_api_cassandra;

public class Employee {
	private String employee_id;
	private String first_name;
	private String last_name;
	private Double salary;
	private String hire_date;
			
	public Employee() {
		
	}
	
	public Employee(String employee_id, String first_name, String last_name, Double salary, String hire_date) {
		this.employee_id = employee_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.salary = salary;
		this.hire_date = hire_date;
	}
		
	public String getID() {
		return employee_id;
	}
		
	public String getFirst() {
		return first_name;
	}
		
	public String getLast() {
		return last_name;
	}
			
	public Double getSalary() {
		return salary;
	}
			
	public String getHireDate() {
		return hire_date;
	}
		
	public void setID(String id) {
		this.employee_id = id;
	}

	public void setFirst(String first_name) {
		this.first_name = first_name;
	}
		
	public void setLast(String last_name) {
		this.last_name = last_name;
	}
			
	public void setSalary(Double salary) {
		this.salary = salary;
	}
			
	public void setHireDate(String hire_date) {
		this.hire_date = hire_date;
	}
			
	public String toString() {
		return "Employee ID: " + employee_id + "\nName: " + first_name + " " + last_name + "\nSalary: $" + salary + "\nHire Date: " + hire_date + "\n";
	}

}