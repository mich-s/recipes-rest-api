package com.michal.recipes.test;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestJdbc {

	public static void main(String[] args) {

		String jdbcUrl = "jdbc:mysql://localhost:3306/recipes?serverTimezone=UTC&useSSL=false";
		String user = "calendar";
		String pass = "calendar";
		
		try {
			System.out.println("Connecting to: " + jdbcUrl);
			Connection connection = DriverManager.getConnection(jdbcUrl, user, pass);
			System.out.println("Connection successful.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
