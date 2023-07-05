package com.example.jdbc.springboot.assignmnet.model;

import java.util.UUID;

public class UserSearchCriteria {

	private UUID id;
	private String mobileNumber;
	private boolean isActive;

	public UserSearchCriteria() {

	}

	public UserSearchCriteria(UUID id) {
		this.id = id;
	}

	public UserSearchCriteria(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public UserSearchCriteria(boolean isActive) {
		this.isActive = isActive;
	}

	public UserSearchCriteria(UUID id, boolean isActive) {
		this.id = id;
		this.isActive = isActive;
	}

	public UserSearchCriteria(UUID id, String mobileNumber, boolean isActive) {
		this.id = id;
		this.mobileNumber = mobileNumber;
		this.isActive = isActive;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return "UserSearchCriteria{" + "id=" + id + ", mobileNumber='" + mobileNumber + '\'' + '}';
	}
}