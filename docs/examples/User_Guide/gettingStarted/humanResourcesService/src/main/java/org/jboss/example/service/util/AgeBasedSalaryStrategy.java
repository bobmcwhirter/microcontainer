package org.jboss.example.service.util;

import java.util.Calendar;

import org.jboss.example.service.Employee;

/**
 * Check that the salary is relative to the employee's age in years.
 */
public class AgeBasedSalaryStrategy implements SalaryStrategy {

	private int minSalary = 5000;
	private int maxSalary = 100000;
	
	private static final int JUNIOR_MIN_SALARY = 25000;
	private static final int SENIOR_MIN_SALARY = 50000;

	public Integer checkSalary(Employee employee, Integer salary) {
		
		Calendar dateOfBirth = Calendar.getInstance();
		dateOfBirth.setTime(employee.getDateOfBirth());
		
		Calendar now = Calendar.getInstance();
		
		if (now.before(dateOfBirth)) {
			throw new IllegalArgumentException("Employee is not born yet!");
		}
		
		int age = now.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
		
		// Check if the employee has already had their birthday this year
		if (age > 0) {
			if (now.get(Calendar.MONTH) - dateOfBirth.get(Calendar.MONTH) > 0) {
				age--;
			} else if (now.get(Calendar.MONTH) - dateOfBirth.get(Calendar.MONTH) == 0) {
				if (now.get(Calendar.DAY_OF_MONTH) - dateOfBirth.get(Calendar.DAY_OF_MONTH) > 0 ) {
					age--;
				}
			}
		}

		// Set minimum salaries depending on the age of the employee
		if (age >= 21 && age < 30 && salary < JUNIOR_MIN_SALARY) {
			salary = JUNIOR_MIN_SALARY; 
		}
		
		if (age >= 30 && age < 50 && salary < SENIOR_MIN_SALARY) {
			salary = SENIOR_MIN_SALARY; 
		}
		
		// Apply company-wide ranges
		if (salary < minSalary) {
			salary = minSalary;
		} else if (salary > maxSalary) {
			salary = maxSalary;
		}
		
		return salary;
	}

	public int getMinSalary() {
		return minSalary;
	}
	
	public void setMinSalary(int minSalary) {
		this.minSalary = minSalary;
	}
	
	public int getMaxSalary() {
		return maxSalary;
	}
	
	public void setMaxSalary(int maxSalary) {
		this.maxSalary = maxSalary;
	}
}
