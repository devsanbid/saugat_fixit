/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package controller;
/**
 *
 * @author saugat
 */


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.DatabaseConnection;
import model.LoginResult;

public class AuthenticationController {

	private static int currentUserId = 1;
	private static int currentWorkerId = 1;
	private static String currentUserType = null;

	// Getters for current user/worker information
	public static int getCurrentUserId() {
		return currentUserId;
	}

	public static int getCurrentWorkerId() {
		return currentWorkerId;
	}

	public static String getCurrentUserType() {
		return currentUserType;
	}

	// User Registration Method
	public static boolean registerUser(String firstName, String lastName, String email,
			String phone, String gender, String password) {
		String hashedPassword = hashPassword(password);
		try (Connection conn = DatabaseConnection.getConnection()) {
			String query = "INSERT INTO users (first_name, last_name, email, phone, gender, password) "
					+ "VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);
			pstmt.setString(3, email);
			pstmt.setString(4, phone);
			pstmt.setString(5, gender);
			pstmt.setString(6, hashedPassword);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Worker Registration Method
	public static boolean registerWorker(String firstName, String lastName, String email,
			int category, String phone, String gender, String password) {
		String hashedPassword = hashPassword(password);
		try (Connection conn = DatabaseConnection.getConnection()) {
			String query = "INSERT INTO workers (first_name, last_name, email, category, "
					+ "phone, gender, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);
			pstmt.setString(3, email);
			pstmt.setInt(4, category);
			pstmt.setString(5, phone);
			pstmt.setString(6, gender);
			pstmt.setString(7, hashedPassword);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Simple password hashing (Note: Use a more secure method in production)
	public static String hashPassword(String password) {
		return String.valueOf(password.hashCode());
	}

	// Login Method for Users
	public static LoginResult loginUser(String email, String password) {
		String hashedPassword = hashPassword(password);
		try (Connection conn = DatabaseConnection.getConnection()) {
			String query = "SELECT id, first_name, last_name FROM users WHERE email = ? AND password = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, email);
			pstmt.setString(2, hashedPassword);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				currentUserId = rs.getInt("id");
				currentWorkerId = -1;
				currentUserType = "user";
				String userName = rs.getString("first_name") + " " + rs.getString("last_name");
				return new LoginResult(true, "user", userName);
			} else {
				return new LoginResult(false, null, null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new LoginResult(false, null, null);
		}
	}

	// Login Method for Workers
	public static LoginResult loginWorker(String email, String password) {
		String hashedPassword = hashPassword(password);
		try (Connection conn = DatabaseConnection.getConnection()) {
			String query = "SELECT worker_id, first_name, last_name, category FROM workers "
					+ "WHERE email = ? AND password = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, email);
			pstmt.setString(2, hashedPassword);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				currentWorkerId = rs.getInt("worker_id");
				currentUserId = -1;
				currentUserType = "worker";
				String workerName = rs.getString("first_name") + " " + rs.getString("last_name");
				String category = rs.getString("category");
				return new LoginResult(true, "worker", workerName, category);
			} else {
				return new LoginResult(false, null, null, null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new LoginResult(false, null, null, null);
		}
	}

	// Logout Method
	public static void logout() {
		currentUserId = -1;
		currentWorkerId = -1;
		currentUserType = null;
	}

	// Check if email already exists (for both users and workers)
	public static boolean isEmailExists(String email, String userType) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String tableName = userType.equals("worker") ? "workers" : "users";
			String query = "SELECT email FROM " + tableName + " WHERE email = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
