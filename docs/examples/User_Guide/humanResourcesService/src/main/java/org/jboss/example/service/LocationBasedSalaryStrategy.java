package org.jboss.example.service;

/**
 * Check that the salary is relative to the employee's location.
 */
public class LocationBasedSalaryStrategy implements SalaryStrategy {

	private int minSalary = 5000;
	private int maxSalary = 100000;
	
	private static final int LONDON_MIN_SALARY = 50000;
	private static final int MANCHESTER_MIN_SALARY = 40000;
	private static final int GLASGOW_MIN_SALARY = 30000;
	private static final int BELFAST_MIN_SALARY = 20000;
	
	public Integer checkSalary(Employee employee, Integer salary) {
		
		String city = employee.getAddress().getCity();
		
		// Set minimum salaries depending on the location of the employee
		if (city.equals("London") && salary < LONDON_MIN_SALARY) {
			salary = LONDON_MIN_SALARY;
		} else if (city.equals("Manchester") && salary < MANCHESTER_MIN_SALARY) {
			salary = MANCHESTER_MIN_SALARY;
		} else if (city.equals("Glasgow") && salary < GLASGOW_MIN_SALARY) {
			salary = GLASGOW_MIN_SALARY;
		} else if (city.equals("Belfast") & salary < BELFAST_MIN_SALARY) {
			salary = BELFAST_MIN_SALARY;
		}
		
		// Apply company-wide ranges
		if (salary < minSalary) {
			salary = minSalary;
		} else if (salary > maxSalary) {
			salary = maxSalary;
		}
		
		return salary;
	}

	public void setMinSalary(int minSalary) {
		this.minSalary = minSalary;
	}
	
	public void setMaxSalary(int maxSalary) {
		this.maxSalary = maxSalary;
	}
}
