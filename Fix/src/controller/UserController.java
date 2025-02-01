/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/

package controller;

/**
 *
 * @author saugat
 */

import controller.WorkerController.Worker;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;
import model.WorkRequest;

public class UserController {

	// Model class for Worker
	public static class User {

		private int workerId;
		private String firstName;
		private String lastName;
		private String email;
		private String phone;
		private String gender;

		// Constructor
		public User(int workerId, String firstName, String lastName, String email,
				 String phone, String gender) {
			this.workerId = workerId;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.phone = phone;
			this.gender = gender;
		}

		// Getters
		public int getWorkerId() {
			return workerId;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public String getEmail() {
			return email;
		}


		public String getPhone() {
			return phone;
		}

		public String getGender() {
			return gender;
		}
	}

	// Fetch approved work requests
	public static List<WorkRequest> getApprovedRequests(int userId) {
		List<WorkRequest> requests = new ArrayList<>();
		String query = "SELECT * FROM work_request WHERE status = 'approved' and user_id = ? ORDER BY created_at DESC";

		try (Connection conn = DatabaseConnection.getConnection()){ 

				PreparedStatement pstmt = conn.prepareStatement(query); 
				pstmt.setInt(1, userId);
				ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				WorkRequest request = new WorkRequest(
						rs.getInt("work_request_id"),
						rs.getInt("user_id"),
						rs.getInt("worker_id"),
						rs.getTimestamp("created_at"),
						rs.getString("status")
				);
				requests.add(request);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return requests;
	}

	// Get work request by ID
	public static WorkRequest getRequestById(int requestId) {
		String query = "SELECT * FROM work_request WHERE work_request_id = ?";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setInt(1, requestId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return new WorkRequest(
						rs.getInt("work_request_id"),
						rs.getInt("user_id"),
						rs.getInt("worker_id"),
						rs.getTimestamp("created_at"),
						rs.getString("status")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	// Fetch all workers
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


	public static List<Worker> getWorkersByCategory(String categoryNumber) {
		List<Worker> workers = new ArrayList<>();
		String query = "SELECT * FROM workers WHERE category = ?";
		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, categoryNumber);
			try (ResultSet rs = pstmt.executeQuery()) {
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
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return workers;
	}

	// Hire a worker
	public static boolean hireWorker(int userId, int workerId) {
		String query = "INSERT INTO work_request (user_id, worker_id,status) VALUES (?, ?, 'pending')";

		try (Connection conn = DatabaseConnection.getConnection() ){

			PreparedStatement pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, userId);
			pstmt.setInt(2, workerId);

			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	// Get hired workers for a user
	public List<Worker> getHiredWorkers(int userId) {
		List<Worker> hiredWorkers = new ArrayList<>();
		String query = "SELECT w.* FROM workers w "
				+ "JOIN my_workers mw ON w.worker_id = mw.worker_id "
				+ "WHERE mw.user_id = ?";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

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
				hiredWorkers.add(worker);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hiredWorkers;
	}

	// Remove hired worker
	public boolean removeHiredWorker(int userId, int workerId) {
		String query = "DELETE FROM my_workers WHERE user_id = ? AND worker_id = ?";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setInt(1, userId);
			pstmt.setInt(2, workerId);

			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Check if worker is already hired by user
	public boolean isWorkerHired(int userId, int workerId) {
		String query = "SELECT COUNT(*) FROM my_workers WHERE user_id = ? AND worker_id = ?";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setInt(1, userId);
			pstmt.setInt(2, workerId);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
