package com.sample.payroll_api_cassandra;

import static spark.Spark.*;
import com.google.gson.Gson;

public class EmployeeRoutes {
	
	public void setUpRoutes(String api_context, PayrollService api_methods) {
		final Gson gson = new Gson();
		
		post(api_context + "/", (req, res) -> {
			Employee newEmployee = gson.fromJson(req.body(), Employee.class);
			return api_methods.addEmployee(newEmployee);
		}, gson::toJson);
			
		get(api_context + "/", (req, res) -> {
			res.type("application/json");
			return api_methods.getEmployees();
		}, gson::toJson);
			
		get(api_context + "/:id", (req, res) -> {
			res.type("application/json");
			Employee employee = api_methods.getEmployee(req.params(":id"));
				
			if (employee != null) {
				return employee;
					
			}
				
			return "Could not find employee\n";
				
		}, gson::toJson);
			
		options(api_context + "/:id", (req, res) -> {
			res.status(200);
			return "Passed through server OPTIONS method.\n";
				
		}, gson::toJson);
			
		delete(api_context + "/:id", (req, res) -> {
			res.type("application/json");
			Employee employee = api_methods.removeFromPayroll(req.params(":id"));
				
			if (employee != null) {
				return employee.getFirst() + " was removed from payroll.\n";
			}
				
			return null;
				
		}, gson::toJson);
			
		put(api_context + "/:id", (req, res) -> {
			res.type("application/json");
			Employee updated_employee = gson.fromJson(req.body(), Employee.class);
				
			if (updated_employee != null) {
				Employee employee_to_update = api_methods.updateEmployee(req.params(":id"), updated_employee);

				return employee_to_update.getFirst() + " was updated.\n";
					
			}
				
			return null;
				
		}, gson::toJson);
		
	}

}