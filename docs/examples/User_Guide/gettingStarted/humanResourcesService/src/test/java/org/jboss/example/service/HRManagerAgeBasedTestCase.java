package org.jboss.example.service;

import org.jboss.example.service.HRManager;

/**
 * Check that the AgeBasedSalaryStrategy behaves as expected
 * when used by the HRManager.
 * 
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public class HRManagerAgeBasedTestCase extends HRManagerTest
{	
    public HRManagerAgeBasedTestCase(String name) {
		super(name);
	}

	public void testSalaryStrategy() throws Exception {
		HRManager manager = (HRManager) getBean("HRService");
		assertNotNull(manager);

		assertEquals(false, manager.isHiringFreeze());
		assertEquals(true, manager.addEmployee(bob));
		assertEquals((Integer) 10000, manager.getSalary(bob));
		
		// Test setting a salary using an age based salary strategy
		Employee employee = manager.getEmployee("Bob", "Smith");
		assertNotNull(employee);
		
		manager.setSalary(employee, 600);		
		assertEquals((Integer) 1000, manager.getSalary(employee));
		
		manager.setSalary(employee, 90000);
		assertEquals((Integer) 80000, manager.getSalary(employee));
	}
}
