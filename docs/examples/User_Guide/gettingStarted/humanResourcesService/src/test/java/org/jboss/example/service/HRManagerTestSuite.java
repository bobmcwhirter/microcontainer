package org.jboss.example.service;

import org.jboss.example.service.util.SalaryStrategyTestSuite;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Allow all HRManager tests to be run.
 * 
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public class HRManagerTestSuite extends TestSuite
{
   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      TestSuite suite = new TestSuite("HRManager Tests");
      
      suite.addTestSuite(HRManagerTestCase.class);
      suite.addTestSuite(HRManagerAgeBasedTestCase.class);
      suite.addTestSuite(HRManagerLocationBasedTestCase.class);
      
      suite.addTest(SalaryStrategyTestSuite.suite());
      
      return suite;
   }
}
