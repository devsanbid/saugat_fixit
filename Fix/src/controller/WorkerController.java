/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author saugat
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;
import model.WorkRequest;

public class WorkerController {

	// Model class for Worker
	public static class Worker {

		private int workerId;
		private String firstName;
		private String lastName;
		private String email;
		private String category;
		private String phone;
		private String gender;

		// Constructor
		public Worker(int workerId, String firstName, String lastName, String email,
				String category, String phone, String gender) {
			this.workerId = workerId;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.category = category;
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

		public String getCategory() {
			return category;
		}

		public String getPhone() {
			return phone;
		}

		public String getGender() {
			return gender;
		}
	}

	// Fetch pending work requests
	public static List<WorkRequest> getPendingRequests() {
		List<WorkRequest> requests = new ArrayList<>();
		String query = "SELECT * FROM work_request WHERE status = 'pending' ORDER BY created_at DESC";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

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

	public static List<WorkRequest> getApprovedRequest() {
		List<WorkRequest> requests = new ArrayList<>();
		String query = "SELECT * FROM work_request WHERE status = 'approved' ORDER BY created_at DESC";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {

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

		// Approve work request
	public static boolean approveRequest(int requestId) {
		String query = "UPDATE work_request SET status = 'approved' WHERE work_request_id = ?";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setInt(1, requestId);
			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				// After approval, add to my_workers table
				WorkRequest request = getRequestById(requestId);
				if (request != null) {
					WorkerController workerController = new WorkerController();
					return workerController.hireWorker(request.getUserId(), request.getWorkerId());
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}


	// Decline work request
	public static boolean declineRequest(int requestId) {
		String query = "UPDATE work_request SET status = 'declined' WHERE work_request_id = ?";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setInt(1, requestId);
			return pstmt.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
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

	// Fetch workers by category
	public List<Worker> getWorkersByCategory(String category) {
		List<Worker> workers = new ArrayList<>();
		String query = "SELECT * FROM workers WHERE category = ?";

		try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setString(1, category);
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
				workers.add(worker);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return workers;
	}

	// Hire a worker
	public boolean hireWorker(int userId, int workerId) {
		String query = "INSERT INTO my_workers (user_id, worker_id) VALUES (?, ?)";

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
