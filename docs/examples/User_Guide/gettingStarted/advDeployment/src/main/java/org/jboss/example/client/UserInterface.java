package org.jboss.example.client;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.jboss.example.service.Address;
import org.jboss.example.service.Employee;

/**
 * Allow different implementations of a user interface to be used.
 * i.e. command line, Swing or even a mock for testing purposes.
 * 
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public interface UserInterface {
	Employee getEmployee() throws IOException;
	Address getAddress() throws IOException;
	Date getDateOfBirth() throws ParseException, IOException;
	Integer getSalary() throws IOException;
}
