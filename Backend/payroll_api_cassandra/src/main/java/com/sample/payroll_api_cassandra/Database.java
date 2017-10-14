package com.sample.payroll_api_cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class Database {
	private Cluster cluster;
	private Session session;
	private String keyspace;
	private String table;
	
	public Cluster getCluster() {
		return this.cluster;
	}
	
	public Session getSession() {
		return this.session;
	}
	
	public String getKeyspace() {
		return this.keyspace;
	}
	
	public String getTable() {
		return this.table;
	}
	
	public void buildCluster(String name, String contact_point) {
		System.out.println("Building cluster...");
		this.cluster = Cluster.builder()
			.withClusterName(name)
			.addContactPoint(contact_point)
			.withPort(9042)
			.build();
		System.out.println("Cluster built.");
		this.session = this.cluster.connect();
		System.out.println("Session initialized.");
	
	}
	
	public void createKeyspace(String name) {
		System.out.println("Creating " + name + " keyspace...");
		this.session.execute("CREATE keyspace IF NOT EXISTS " + name + " WITH replication " 
				+ "= {'class':'SimpleStrategy', 'replication_factor':3} AND durable_writes = true;");
			System.out.println("Keyspace created. Now using " + name + " keyspace.");
		this.session.execute("USE " + name + ";");
		this.keyspace = name;
			System.out.println("Now using keyspace " + this.session.getLoggedKeyspace() + ".");
	}
	
	public void createTable(String name) {
		System.out.println("Creating " + name + " table...");
		this.session.execute("CREATE table IF NOT EXISTS " + name + "(employee_id varchar PRIMARY KEY,"
				+ "first_name varchar,"
				+ "last_name varchar,"
				+ "salary double,"
				+ "hire_date varchar);");
			System.out.println(name + " table was created in keyspace " + this.keyspace + ".");
		this.table = name;
	}
	
	public void dropKeyspace() {
		this.session.execute("DROP keyspace " + this.keyspace + ";");
			System.out.println("Dropped keyspace " + this.keyspace + ".");
	}

}