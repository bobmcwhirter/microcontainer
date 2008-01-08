package org.jboss.example.client;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.jboss.example.service.Address;
import org.jboss.example.service.Employee;

/**
 * A mock user interface to simulate values being passed in by a user.
 * 
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public class MockUserInterface implements UserInterface {

	public Address getAddress() throws IOException {
		return new Address(5, "Oxford St", "London");
	}

	public Date getDateOfBirth() throws ParseException, IOException {
		Calendar age = Calendar.getInstance();
		int year = age.get(Calendar.YEAR);
		int month = age.get(Calendar.MONTH);
		int day = age.get(Calendar.DAY_OF_MONTH);
		
		age.set(year - 43, month, day);
		return age.getTime();
	}

	public Employee getEmployee() throws IOException {
		return new Employee("David", "Hasselhof");
	}

	public Integer getSalary() throws IOException {
		return new Integer("50000");
	}
}
