package org.jboss.example.service.util;

import org.jboss.example.service.Employee;

public interface SalaryStrategy {

	public int getMinSalary();
	public int getMaxSalary();
	public Integer checkSalary(Employee employee, Integer salary);
}
