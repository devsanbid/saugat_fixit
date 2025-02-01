package controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sanbid
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.DatabaseConnection;

public class UserProfileController {

	public static class UserProfile {

		private String firstName;
		private String lastName;
		private String email;
		private String password;

		public UserProfile(String firstName, String lastName, String email, String password) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.password = password;
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

	}

	// Get worker profile data
	public static UserProfile getUserProfile(int userId) {
		try (Connection conn = DatabaseConnection.getConnection()) {
			String query = "SELECT first_name, last_name, email,password  FROM users WHERE id = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, userId);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return new UserProfile(
						rs.getString("first_name"),
						rs.getString("last_name"),
						rs.getString("email"),
						rs.getString("password")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}



	// Update password
	public static boolean updatePassword(int userId  , String newPassword) {
		try (Connection conn = DatabaseConnection.getConnection()) {
				String hashpassword = AuthenticationController.hashPassword(newPassword);
				String updateQuery = "UPDATE users SET password = ? WHERE id = ?";
				PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
				updateStmt.setString(1,hashpassword);
				updateStmt.setInt(2, userId);

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
