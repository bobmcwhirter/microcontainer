package org.jboss.example.service;

public interface SalaryStrategy {

	public int getMinSalary();
	public int getMaxSalary();
	public Integer checkSalary(Employee employee, Integer salary);
}
