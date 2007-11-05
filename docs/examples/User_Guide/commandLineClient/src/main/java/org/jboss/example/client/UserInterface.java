package org.jboss.example.client;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.jboss.example.service.Address;
import org.jboss.example.service.Employee;

public interface UserInterface {
	Employee getEmployee() throws IOException;
	Address getAddress() throws IOException;
	Date getDateOfBirth() throws ParseException, IOException;
	Integer getSalary() throws IOException;
}
