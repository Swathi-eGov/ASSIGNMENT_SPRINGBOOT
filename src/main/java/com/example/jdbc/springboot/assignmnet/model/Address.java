package com.example.jdbc.springboot.assignmnet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {
	private String city;
	@JsonProperty("street_name")
	private String streetName;
	@JsonProperty("street_address")
	private String streetAddress;
	@JsonProperty("zip_code")
	private String zipCode;
	private String state;
	private String country;
	private Coordinates coordinates;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	public static class Coordinates {
		private double lat;
		private double lng;

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}
	}

	@Override
	public String toString() {
		return "Address{" + "city='" + city + '\'' + ", streetName='" + streetName + '\'' + ", streetAddress='"
				+ streetAddress + '\'' + ", zipCode='" + zipCode + '\'' + ", state='" + state + '\'' + ", country='"
				+ country + '\'' + ", coordinates=" + coordinates + '}';
	}
}
