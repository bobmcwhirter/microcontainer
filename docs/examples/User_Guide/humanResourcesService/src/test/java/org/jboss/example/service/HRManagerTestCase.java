package org.jboss.example.service;

import org.jboss.example.service.HRManager;

public class HRManagerTestCase extends HRServiceTest
{	
    public HRManagerTestCase(String name) {
		super(name);
	}

	public void testHiringFiring() throws Exception {
		HRManager manager = (HRManager) getBean("HRService");
		assertNotNull(manager);

		// Test add and remove (with and without a hiring freeze)
		assertEquals(true, manager.isHiringFreeze());
		assertEquals(false, manager.addEmployee(bob));
		assertEquals(0, manager.getEmployees().size());
		
		manager.setHiringFreeze(false);
		
		assertEquals(false, manager.isHiringFreeze());
		assertEquals(true, manager.addEmployee(bob));
		assertEquals(1, manager.getEmployees().size());
		assertEquals((Integer) 10000, manager.getSalary(bob));
		
		// Test setting a salary without using a salary strategy
		Employee employee = manager.getEmployee("Bob", "Smith");
		assertNotNull(employee);
		manager.setSalary(employee, 600);
		
		assertEquals((Integer) 600, manager.getSalary(employee));
		
		manager.removeEmployee(bob);
		assertEquals(0, manager.getEmployees().size());	
	}
}
