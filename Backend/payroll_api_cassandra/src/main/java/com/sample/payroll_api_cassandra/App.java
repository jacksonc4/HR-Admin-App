package com.sample.payroll_api_cassandra;

public class App {
	
	//Database object
	private static final Database cassandra = new Database();
	
	//Payroll API context
	private static final String payroll_api_context = "/payrollservice/employees";
	
	//CORS object
	private static final CORS cors = new CORS();
	
	//Employee Routes object
	private static final EmployeeRoutes employeeRoutes = new EmployeeRoutes();
	
	public static void main(String[]args) {
		
		System.out.print("Cluster name / Contact point IP: ");
			String name = "Payroll API";
			String contact_point = "127.0.0.1";
		
		//Build Database
		cassandra.buildCluster(name, contact_point);
		cassandra.createKeyspace("payroll_api");
		cassandra.createTable("employees");
		
		//Create payroll service using the same database instance parameters
		final PayrollService payrollService = new PayrollService(cassandra.getSession(),
				cassandra.getKeyspace(),
				cassandra.getTable());
		
		//Set up routes for Employee API
		employeeRoutes.setUpRoutes(payroll_api_context, payrollService);
		
		//Enable CORS requests
		cors.enableCORS("*", "*", "*");
		
	}
    
}