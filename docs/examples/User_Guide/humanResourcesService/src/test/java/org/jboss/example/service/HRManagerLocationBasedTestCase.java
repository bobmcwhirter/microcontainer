package org.jboss.example.service;

import org.jboss.example.service.HRManager;

public class HRManagerLocationBasedTestCase extends HRManagerTest
{	
    public HRManagerLocationBasedTestCase(String name) {
		super(name);
	}

	public void testSalaryStrategy() throws Exception {
		HRManager manager = (HRManager) getBean("HRService");
		assertNotNull(manager);

		assertEquals(false, manager.isHiringFreeze());
		assertEquals(true, manager.addEmployee(rebecca));
		assertEquals((Integer) 10000, manager.getSalary(rebecca));
		
		// Test setting a salary using a location based salary strategy
		Employee employee = manager.getEmployee("Rebecca", "Jones");
		assertNotNull(employee);
		
		manager.setSalary(employee, 15000);		
		assertEquals((Integer) 20000, manager.getSalary(employee));
		
		manager.setSalary(employee, 71000);
		assertEquals((Integer) 70000, manager.getSalary(employee));
	}
}
