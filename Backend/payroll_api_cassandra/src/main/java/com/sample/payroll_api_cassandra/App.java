package com.sample.payroll_api_cassandra;

import java.util.Scanner;

import static spark.Spark.*;
import com.google.gson.Gson;

import spark.Filter;
import spark.Request;
import spark.Response;

//API routes
public class App {
	
	//Database object
	private static final Database cassandra = new Database();
	
	//Payroll API context
	private static final String payroll_api_context = "/payrollservice/employees";
	
	//CORS function
	private static void enableCORS(final String origin, final String methods, final String headers) {
	    before(new Filter() {
	        @Override
	        public void handle(Request request, Response response) {
	            response.header("Access-Control-Allow-Origin", origin);
	            response.header("Access-Control-Request-Method", methods);
	            response.header("Access-Control-Allow-Headers", headers);
	        }
	    });
	}
	
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
		final PayrollService payrollService = new PayrollService(cassandra.getCluster(),
				cassandra.getSession(),
				cassandra.getKeyspace(),
				cassandra.getTable());
		
		enableCORS("*", "*", "*");
		
		//Add Employee to payroll
		post(payroll_api_context + "/", (req, res) -> {
			Employee newEmployee = gson.fromJson(req.body(), Employee.class);
			System.out.println(newEmployee);
			return payrollService.addEmployee(newEmployee);
		}, gson::toJson);
		
		//Get all Employees in database
		get(payroll_api_context + "/", (req, res) -> {
			res.type("application/json");
			return payrollService.getEmployees();
		}, gson::toJson);
		
		//Find Employee by ID -> transform to query string later
		get(payroll_api_context + "/:id", (req, res) -> {
			res.type("application/json");
			Employee employee = payrollService.getEmployee(req.params(":id"));
			System.out.println(employee);
			
			if (employee != null) {
				return employee;
				
			}
			
			return "Could not find employee";
			
		}, gson::toJson);
		
		delete(payroll_api_context + "/:id", (req, res) -> {
			res.type("application/json");
			Employee employee = payrollService.removeFromPayroll(req.params(":id"));
			
			if (employee != null) {
				return employee.getFirst() + " was removed from payroll.";
			}
			
			return null;
			
		}, gson::toJson);
		
		put(payroll_api_context + "/:id", (req, res) -> {
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