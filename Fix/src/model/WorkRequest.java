/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author saugat
 */


import java.sql.Timestamp;
public  class WorkRequest {

	private int workRequestId;
	private int userId;
	private int workerId;
	private Timestamp createdAt;
	private String status;

	public WorkRequest(int workRequestId, int userId, int workerId,
			Timestamp createdAt, String status) {
		this.workRequestId = workRequestId;
		this.userId = userId;
		this.workerId = workerId;
		this.createdAt = createdAt;
		this.status = status;
	}

	// Getters
	public int getWorkRequestId() {
		return workRequestId;
	}

	public int getUserId() {
		return userId;
	}

	public int getWorkerId() {
		return workerId;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public String getStatus() {
		return status;
	}
}
