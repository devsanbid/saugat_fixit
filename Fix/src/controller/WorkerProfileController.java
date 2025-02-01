/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author saugat
 */

package controller;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.DatabaseConnection;

public class WorkerProfileController {

	public static class WorkerProfile {

		private String firstName;
		private String lastName;
		private String email;
		private String category;
		private String password;

		public WorkerProfile(String firstName, String lastName, String email, String password, String category) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.password = password;
			this.category = category;
		}

		// Getters
		public String getFirstName() {
			return firstName;
		}

		public String getPassword() {
			return password;
		}
		public String getLastName() {
			return lastName;
		}

		public String getEmail() {
			return email;
		}

		public String getCategory() {
			return category;
		}
	}

	// Get worker profile data
	public static WorkerProfile getWorkerProfile(int workerId) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String query = "SELECT first_name, last_name, email,password, category FROM workers WHERE worker_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, workerId);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return new WorkerProfile(
						rs.getString("first_name"),
						rs.getString("last_name"),
						rs.getString("email"),
						rs.getString("password"),
						rs.getString("category")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Update worker profile
	public static boolean updateWorkerProfile(int workerId, String firstName, String lastName,
			String email, String password, boolean isPasswordChanged) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			// If password is changed, verify the current password
			if (isPasswordChanged) {
				String verifyQuery = "SELECT password FROM workers WHERE worker_id = ?";
				PreparedStatement verifyStmt = conn.prepareStatement(verifyQuery);
				verifyStmt.setInt(1, workerId);
				ResultSet rs = verifyStmt.executeQuery();

				if (!rs.next() || !rs.getString("password").equals(AuthenticationController.hashPassword(password))) {
					return false; // Password verification failed
				}
			}

			// Update query without password
			String updateQuery = "UPDATE workers SET first_name = ?, last_name = ?, email = ? WHERE worker_id = ?";
			PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
			updateStmt.setString(1, firstName);
			updateStmt.setString(2, lastName);
			updateStmt.setString(3, email);
			updateStmt.setInt(4, workerId);

			int rowsAffected = updateStmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	// Update password
	public static boolean updatePassword2(int workerId  , String newPassword) {
		try (Connection conn = DatabaseConnection.getConnection()) {
				String hashpassword = AuthenticationController.hashPassword(newPassword);
				String updateQuery = "UPDATE users SET password = ? WHERE worker_id = ?";
				PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
				updateStmt.setString(1,hashpassword);
				updateStmt.setInt(2, workerId);

				int rowsAffected = updateStmt.executeUpdate();

				 if(rowsAffected > 0){
					 return true;
				 }else {
					 return false;
				 }
			}
	catch (SQLException e){
            e.printStackTrace();
		return false;
	}
	}

	// Update password
	public static boolean updatePassword(int workerId  , String newPassword) {
		try (Connection conn = DatabaseConnection.getConnection()) {
				String hashpassword = AuthenticationController.hashPassword(newPassword);
				String updateQuery = "UPDATE workers SET password = ? WHERE worker_id = ?";
				PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
				updateStmt.setString(1,hashpassword);
				updateStmt.setInt(2, workerId);

				int rowsAffected = updateStmt.executeUpdate();

				 if(rowsAffected > 0){
					 return true;
				 }else {
					 return false;
				 }
			}
	catch (SQLException e){
            e.printStackTrace();
		return false;
	}
	}
}
