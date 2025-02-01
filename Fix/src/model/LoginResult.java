/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author sanbid
 */

public class LoginResult {

	private boolean success;
	private String userType;
	private String name;
	private String category;  // For workers only

	// Constructor for users
	public LoginResult(boolean success, String userType, String name) {
		this.success = success;
		this.userType = userType;
		this.name = name;
		this.category = null;
	}

	// Constructor for workers
	public LoginResult(boolean success, String userType, String name, String category) {
		this.success = success;
		this.userType = userType;
		this.name = name;
		this.category = category;
	}

	// Getters
	public boolean isSuccess() {
		return success;
	}

	public String getUserType() {
		return userType;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}
}
