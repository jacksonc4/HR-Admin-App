package com.sample.payroll_api_cassandra;

import java.util.Scanner;

import static spark.Spark.*;
import com.google.gson.Gson;

//API routes
public class App {
	
	//Database object
	public static Database cassandra = new Database();
	
	@SuppressWarnings("resource")
	public static void main(String[]args) {
		final Gson gson = new Gson();
		Scanner in = new Scanner(System.in);
		
		System.out.print("Cluster name / Contact point IP: ");
			String name = "Payroll API";
			String contact_point = "127.0.0.1";
		
		cassandra.buildCluster(name, contact_point);
		cassandra.createKeyspace("payroll_api");
		cassandra.createTable("employees");
		
		//Create payroll service using the same database instance parameters
		PayrollService payrollService = new PayrollService(cassandra.getCluster(),
				cassandra.getSession(),
				cassandra.getKeyspace(),
				cassandra.getTable());
		
		//Add Employee to payroll
		post("/add-employee", (req, res) -> {
			Employee newEmployee = gson.fromJson(req.body(), Employee.class);
			System.out.println(newEmployee);
			return payrollService.addEmployee(newEmployee);
		}, gson::toJson);
		
		//Get all Employees in database
		get("/", (req, res) -> {
			res.type("application/json");
			return payrollService.getEmployees();
		}, gson::toJson);
		
		//Find Employee by ID
		get("/:id", (req, res) -> {
			res.type("application/json");
			Employee employee = payrollService.getEmployee(req.params(":id"));
			System.out.println(employee);
			
			if (employee != null) {
				return employee;
				
			}
			
			return "Could not find employee";
			
		}, gson::toJson);
		
		delete("/:id", (req, res) -> {
			res.type("application/json");
			Employee employee = payrollService.removeFromPayroll(req.params(":id"));
			
			if (employee != null) {
				return employee.getFirst() + " was removed from payroll.";
			}
			
			return null;
			
		}, gson::toJson);
		
		put("/:id", (req, res) -> {
			res.type("application/json");
			Employee updated_employee = gson.fromJson(req.body(), Employee.class);
			
			if (updated_employee != null) {
				System.out.println(updated_employee);
				Employee employee_to_update = payrollService.updateEmployee(req.params(":id"), updated_employee);
				
				return employee_to_update.getFirst() + " was updated.";
				
			}
			
			return null;
			
		}, gson::toJson);
		
		//Delete keyspace when done
		System.out.print("Delete keyspace? (y/n) ");
			String drop = in.nextLine();
				if (drop.equalsIgnoreCase("y")) {
					cassandra.dropKeyspace();
				} else {
					System.out.println("Did nothing.");
				}
						
		in.close();
		
	}
    
}