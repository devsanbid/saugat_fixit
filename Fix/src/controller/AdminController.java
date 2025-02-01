/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author sanbid
 */

import controller.UserController.User;
import controller.WorkerController.Worker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;

// AdminController.java
public class AdminController {

	public static List<User> getAllUsers() {
		List<User> user = new ArrayList<>();
		String query = "SELECT * FROM users";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				User worker = new User(
						rs.getInt("id"),
						rs.getString("first_name"),
						rs.getString("last_name"),
						rs.getString("email"),
						rs.getString("phone"),
						rs.getString("gender")
				);
				user.add(worker);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public static User getUserById(int id) {
		String query = "SELECT * FROM users WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setInt(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return new User(
							rs.getInt("id"),
							rs.getString("first_name"),
							rs.getString("last_name"),
							rs.getString("email"),
							rs.getString("phone"),
							rs.getString("gender")
					);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static User getUserByPhone(String phone) {
		String query = "SELECT * FROM users WHERE phone = ?";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setString(1, phone);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return new User(
							rs.getInt("id"),
							rs.getString("first_name"),
							rs.getString("last_name"),
							rs.getString("email"),
							rs.getString("phone"),
							rs.getString("gender")
					);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static User getUserByEmail(String email) {
		String query = "SELECT * FROM users WHERE email = ?";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setString(1, email);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return new User(
							rs.getInt("id"),
							rs.getString("first_name"),
							rs.getString("last_name"),
							rs.getString("email"),
							rs.getString("phone"),
							rs.getString("gender")
					);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean deleteUser(int userId) {
		String query = "DELETE FROM users WHERE id = ?";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setInt(1, userId);
			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static List<Worker> getAllWorkers() {
		List<Worker> workers = new ArrayList<>();
		String query = "SELECT * FROM workers";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				Worker worker = new Worker(
						rs.getInt("worker_id"),
						rs.getString("first_name"),
						rs.getString("last_name"),
						rs.getString("email"),
						rs.getString("category"),
						rs.getString("phone"),
						rs.getString("gender")
				);
				workers.add(worker);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return workers;
	}

	public static Worker getWorkerById(int id) {
		String query = "SELECT * FROM workers WHERE worker_id = ?";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setInt(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return new Worker(
							rs.getInt("worker_id"),
							rs.getString("first_name"),
							rs.getString("last_name"),
							rs.getString("email"),
							rs.getString("category"),
							rs.getString("phone"),
							rs.getString("gender")
					);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Worker getWorkerByPhone(String phone) {
		String query = "SELECT * FROM workers WHERE phone = ?";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setString(1, phone);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return new Worker(
							rs.getInt("worker_id"),
							rs.getString("first_name"),
							rs.getString("last_name"),
							rs.getString("email"),
							rs.getString("category"),
							rs.getString("phone"),
							rs.getString("gender")
					);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Worker getWorkerByEmail(String email) {
		String query = "SELECT * FROM workers WHERE email = ?";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setString(1, email);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return new Worker(
							rs.getInt("worker_id"),
							rs.getString("first_name"),
							rs.getString("last_name"),
							rs.getString("email"),
							rs.getString("category"),
							rs.getString("phone"),
							rs.getString("gender")
					);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean deleteWorker(int workerId) {
		String query = "DELETE FROM workers WHERE worker_id = ?";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setInt(1, workerId);
			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
