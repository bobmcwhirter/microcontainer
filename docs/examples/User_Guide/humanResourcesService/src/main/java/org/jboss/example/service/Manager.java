package org.jboss.example.service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An Manager keeps track of a set of employees and their associated salaries.
 */
public class Manager {
    
	private static final Integer STANDARD_SALARY = 10000;
	
	private Map<Employee, Integer> employees;
	 
	private boolean hiringFreeze = false;
	
	private SalaryStrategy salaryStrategy = null;
	
	public Manager() {
		System.out.println("Hello from the manager...");
		employees =  new ConcurrentHashMap<Employee, Integer>();
 	}
	
	public boolean addEmployee(Employee employee) {
		if (hiringFreeze == false) {
			employees.put(employee, STANDARD_SALARY);
		}
		
		return (hiringFreeze == false);
	}
	
	public Set<Employee> getEmployees() {
		return employees.keySet();
	}
	
	public Employee getEmployee(String firstName, String lastName) {
		Set<Employee> employees = getEmployees();
		for (Employee employee : employees) {
			if (employee.getFirstName().equals(firstName)
					&& employee.getLastName().equals(lastName)) {
				return employee;
			}
		}
		
		return null;
	}
	
	public void removeEmployee(Employee employee) {
		employees.remove(employee);
	}
	
	public Integer getSalary(Employee employee) {
		return employees.get(employee);
	}
	
	public void setSalary(Employee employee, Integer salary) {
		
		int adjustedSalary = salary;
		
		if (salaryStrategy != null) {
			adjustedSalary = salaryStrategy.checkSalary(employee, salary);
		}
		employees.put(employee, adjustedSalary);
	}

	public boolean isHiringFreeze() {
		return hiringFreeze;
	}

	public void setHiringFreeze(boolean hiringFreeze) {
		this.hiringFreeze = hiringFreeze;
	}

	public void setSalaryStrategy(SalaryStrategy salaryStrategy) {
		this.salaryStrategy = salaryStrategy;
	}
}
