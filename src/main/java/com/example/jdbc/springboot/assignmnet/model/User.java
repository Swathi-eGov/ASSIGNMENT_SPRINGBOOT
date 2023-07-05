package com.example.jdbc.springboot.assignmnet.model;

import java.util.UUID;

public class User {

	private UUID id;
	private String name;
	private String gender;
	private String mobileNumber;
	private Address address;
	private boolean isActive;
	private long createdTime;

	public User() {

	}

	public User(UUID id, String name, String gender, String mobileNumber, Address address, boolean isActive) {
		super();
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.mobileNumber = mobileNumber;
		this.address = address;
		this.isActive = isActive;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", gender=" + gender + ", mobileNumber=" + mobileNumber
				+ ", address=" + address + ", isActive=" + isActive + ", createdTime=" + createdTime + "]";
	}
}