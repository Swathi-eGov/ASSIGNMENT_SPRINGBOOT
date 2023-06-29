package com.example.jdbc.springboot.assignmnet.model;


public class UserSearchCriteria {
    private int id;
    private String mobileNumber;

    public UserSearchCriteria(){

    }

    public UserSearchCriteria(int id) {
        this.id = id;
    }

    public UserSearchCriteria(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public UserSearchCriteria(int id, String mobileNumber) {
        this.id = id;
        this.mobileNumber = mobileNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "UserSearchCriteria{" +
                "id=" + id +
                ", mobileNumber='" + mobileNumber + '\'' +
                '}';
    }
}