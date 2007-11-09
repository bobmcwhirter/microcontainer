package org.jboss.example.service.util;

import org.jboss.example.service.HRManagerTest;
import org.jboss.example.service.util.SalaryStrategy;

/**
 * Check the behaviour of the AgeBasedSalaryStrategy.
 * 
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public class AgeBasedSalaryTestCase extends HRManagerTest
{
	public AgeBasedSalaryTestCase(String name) {
		super(name);
	}

	public void testSalaryRanges() throws Exception {
		SalaryStrategy strategy = (SalaryStrategy) getBean("AgeBasedSalary");
		assertNotNull(strategy);
		
		// Check the minimum and maximum salaries for all employees
		assertEquals(1000, strategy.getMinSalary());		
		assertEquals(80000, strategy.getMaxSalary());		

		// Check the minimum and maximum salaries that bob (age 17) can earn
		assertEquals((Integer) 1000, strategy.checkSalary(bob, 500));
		assertEquals((Integer) 80000, strategy.checkSalary(bob, 85000));

		// Check the minimum and maximum salaries that rebecca (age 25) can earn
		assertEquals((Integer) 25000, strategy.checkSalary(rebecca, 20000));
		assertEquals((Integer) 80000, strategy.checkSalary(rebecca, 90000));
		
		// Check the minimum and maximum salaries that karen (age 44) can earn
		assertEquals((Integer) 50000, strategy.checkSalary(karen, 49000));
		assertEquals((Integer) 80000, strategy.checkSalary(karen, 81000));
		
		// Check the minimum and maximum salaries that joe (age 62) can earn
		assertEquals((Integer) 1000, strategy.checkSalary(joe, 900));
		assertEquals((Integer) 80000, strategy.checkSalary(joe, 90000));
	}
}