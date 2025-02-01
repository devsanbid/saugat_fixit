/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author saugat
 */
public class DatabaseConnection {

	private static final String URL = "jdbc:mysql://localhost:3306/fixit";
	private static final String USERNAME = "sanbid";
	private static final String PASSWORD = "33533";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}

	// Create database and table if not exists
	public static void initializeDatabase() {
		try {
			Connection conn = getConnection();
			// Create database
			conn.createStatement().execute("CREATE DATABASE IF NOT EXISTS fixit");

			// Use the database
			conn.createStatement().execute("USE fixit");

			// Create users table
			String createTableQuery1 = "CREATE TABLE IF NOT EXISTS users ("
					+ "id INT AUTO_INCREMENT PRIMARY KEY,"
					+ "first_name VARCHAR(100) NOT NULL,"
					+ "last_name VARCHAR(100) NOT NULL,"
					+ "email VARCHAR(50) UNIQUE NOT NULL,"
					+ "phone VARCHAR(255) NOT NULL,"
					+ "gender VARCHAR(255) NOT NULL,"
					+ "password VARCHAR(255) NOT NULL,"
					+ "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

			String createTableQuery2 = "CREATE TABLE IF NOT EXISTS workers ("
					+ "worker_id INT AUTO_INCREMENT PRIMARY KEY,"
					+ "first_name VARCHAR(100) NOT NULL,"
					+ "last_name VARCHAR(100) NOT NULL,"
					+ "email VARCHAR(50) UNIQUE NOT NULL,"
					+ "phone VARCHAR(255) NOT NULL,"
					+ "gender VARCHAR(255) NOT NULL,"
					+ "password VARCHAR(255) NOT NULL,"
					+ "category int NOT NULL,"
					+ "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

			String createTableQuery3 = "CREATE TABLE IF NOT EXISTS work_request ("
					+ "work_request_id INT AUTO_INCREMENT PRIMARY KEY,"
					+ "user_id int NOT NULL,"
					+ "foreign key(user_id) references users(id),"
					+ "worker_id int NOT NULL,"
					+ "foreign key(worker_id) references workers(worker_id),"
					+ "status varchar(200),"
					+ "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";


			String createTableQuery5 = "CREATE TABLE IF NOT EXISTS category ("
					+ "id INT AUTO_INCREMENT PRIMARY KEY,"
					+ "category_name varchar(200),"
					+ "worker_id int NOT NULL,"
					+ "foreign key(worker_id) references workers(worker_id),"
					+ "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

			conn.createStatement().execute(createTableQuery1);
			conn.createStatement().execute(createTableQuery2);
			conn.createStatement().execute(createTableQuery3);
			conn.createStatement().execute(createTableQuery5);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
