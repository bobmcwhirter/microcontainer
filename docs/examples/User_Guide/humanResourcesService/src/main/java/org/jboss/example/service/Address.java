package org.jboss.example.service;

public class Address {

	private int number;
	private String street;
	private String city;
	
	public Address(int number, String street, String city) {
		this.number = number;
		this.street = street;
		this.city = city;
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}
