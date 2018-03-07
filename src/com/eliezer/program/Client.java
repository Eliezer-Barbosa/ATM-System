package com.eliezer.program;

import java.io.Serializable;

public class Client implements Serializable {
	
private String fullName, firstName, middleName, lastName;
	
	public Client(String firsName, String middleName, String lastName) {
		this.setFirstName(firsName);
		this.setMiddleName(middleName);
		this.setLastName(lastName);
		this.setFullName(this.getFirstName() + " " + this.getMiddleName() + " " +
		this.getLastName());
		
	}
	
	public Client() {
		// TODO Auto-generated constructor stub
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	

}
