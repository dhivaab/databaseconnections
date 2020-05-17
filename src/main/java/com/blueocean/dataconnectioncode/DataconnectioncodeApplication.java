package com.blueocean.dataconnectioncode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import com.blueocean.dataconnectioncode.model.Customer;
import com.blueocean.dataconnectioncode.model.CustomerJPA;
import com.blueocean.dataconnectioncode.repo.CustomerRepository;
import java.sql.*;

@SpringBootApplication
public class DataconnectioncodeApplication implements CommandLineRunner {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	CustomerRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(DataconnectioncodeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// spring jdbc template
		
		// jdbcTemplate.execute("CREATE TABLE customers(" + "id SERIAL, first_name
		// VARCHAR(255), last_name VARCHAR(255))");
		List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
				.map(name -> name.split(" ")).collect(Collectors.toList());

		jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);

		jdbcTemplate
				.query("SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[] { "Josh" },
						(rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"),
								rs.getString("last_name")))
				.forEach(customer -> System.out.println(customer.toString()));

		// spring data jpa
		repository.save(new CustomerJPA(2, "Jack", "Bauer"));
		repository.save(new CustomerJPA(3, "Chloe", "O'Brian"));
		repository.save(new CustomerJPA(4, "Kim", "Bauer"));
		repository.save(new CustomerJPA(5, "David", "Palmer"));
		repository.save(new CustomerJPA(6, "Michelle", "Dessler"));

		for (CustomerJPA customer : repository.findAll()) {
			System.out.println(customer.getFirstName());
		}

		for (int i = 110; i < 150; i++) {
			// manual connection open jdbc
			Connection conn = null;
			Statement stmt = null;
			try {
				// STEP 2: Register JDBC driver
				Class.forName("com.mysql.cj.jdbc.Driver");

				// STEP 3: Open a connection
				System.out.println("Connecting to database...");
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ProductEngg", "root", "root");

				// STEP 4: Execute a query
				System.out.println("Creating database...");
				stmt = conn.createStatement();

				String sql = "CREATE DATABASE STUDENTS" + i;
				stmt.executeUpdate(sql);
				System.out.println("Database created successfully...");
			} catch (SQLException se) {
				// Handle errors for JDBC
				se.printStackTrace();
			} catch (Exception e) {
				// Handle errors for Class.forName
				e.printStackTrace();
			} finally {

			} // end try
			System.out.println("Goodbye!");
		}
	}

}
