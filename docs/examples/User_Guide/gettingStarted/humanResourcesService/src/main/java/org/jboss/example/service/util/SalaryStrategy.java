package org.jboss.example.service.util;

import org.jboss.example.service.Employee;

/**
 * Apply some rules to calculate an employee's minimum and maximum salary.
 * 
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public interface SalaryStrategy {

	public int getMinSalary();
	public int getMaxSalary();
	public Integer checkSalary(Employee employee, Integer salary);
}
