package com.sample.payroll_api_cassandra;

import static spark.Spark.*;

import spark.Filter;
import spark.Request;
import spark.Response;

public class CORS {

	//CORS function
	public void enableCORS(final String origin, final String methods, final String headers) {
		before(new Filter() {
			@Override
			public void handle(Request request, Response response) {
				response.header("Access-Control-Allow-Origin", origin);
				response.header("Access-Control-Request-Method", methods);
				response.header("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
				response.header("Access-Control-Allow-Headers", headers);
			}
		});
	}
		
}