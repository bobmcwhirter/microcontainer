package org.jboss.example.service;

import java.util.Calendar;

import org.jboss.test.kernel.junit.MicrocontainerTest;

public abstract class HRManagerTest extends MicrocontainerTest
{
	protected Employee bob, rebecca, karen, joe;

	public HRManagerTest(String name) {
		super(name);
	}
	   
	protected void setUp() throws Exception {
		super.setUp();

		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		int day = now.get(Calendar.DAY_OF_MONTH);

		Calendar age = Calendar.getInstance();

		bob = new Employee("Bob", "Smith");
		age.set(year - 17, month, day);
		bob.setDateOfBirth(age.getTime());
		bob.setAddress(new Address(5, "Waterloo Road", "London"));

		rebecca = new Employee("Rebecca", "Jones");
		age.set(year - 25, month, day);
		rebecca.setDateOfBirth(age.getTime());
		rebecca.setAddress(new Address(16, "Holt Street", "Belfast"));

		karen = new Employee("Karen", "McKenzie");
		age.set(year - 44, month, day);
		karen.setDateOfBirth(age.getTime());
		karen.setAddress(new Address(78, "Kelvinbridge Road", "Glasgow"));

		joe = new Employee("Joe", "Yates");
		age.set(year - 62, month, day);
		joe.setDateOfBirth(age.getTime());
		joe.setAddress(new Address(30, "Oxford Street", "Manchester"));
	}
}
