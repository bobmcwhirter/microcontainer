package org.jboss.example.service.util;

import org.jboss.example.service.HRManagerTest;
import org.jboss.example.service.util.SalaryStrategy;

/**
 * Check the behaviour of the LocationBasedSalaryStrategy.
 * 
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public class LocationBasedSalaryTestCase extends HRManagerTest
{
	public LocationBasedSalaryTestCase(String name) {
		super(name);
	}

	public void testSalaryRanges() throws Exception {
		SalaryStrategy strategy = (SalaryStrategy) getBean("LocationBasedSalary");
		assertNotNull(strategy);
		
		// Check the minimum and maximum salaries for all employees
		assertEquals(2000, strategy.getMinSalary());		
		assertEquals(90000, strategy.getMaxSalary());		

		// Check the minimum and maximum salaries that bob (London) can earn
		assertEquals((Integer) 50000, strategy.checkSalary(bob, 30000));
		assertEquals((Integer) 90000, strategy.checkSalary(bob, 95000));

		// Check the minimum and maximum salaries that rebecca (Belfast) can earn
		assertEquals((Integer) 20000, strategy.checkSalary(rebecca, 10000));
		assertEquals((Integer) 90000, strategy.checkSalary(rebecca, 91000));
		
		// Check the minimum and maximum salaries that karen (Glagow) can earn
		assertEquals((Integer) 30000, strategy.checkSalary(karen, 29000));
		assertEquals((Integer) 90000, strategy.checkSalary(karen, 98000));
		
		// Check the minimum and maximum salaries that joe (Manchester) can earn
		assertEquals((Integer) 40000, strategy.checkSalary(joe, 31000));
		assertEquals((Integer) 90000, strategy.checkSalary(joe, 96000));
	}
}