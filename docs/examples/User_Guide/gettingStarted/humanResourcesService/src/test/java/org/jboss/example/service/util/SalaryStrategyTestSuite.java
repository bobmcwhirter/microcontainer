package org.jboss.example.service.util;

import org.jboss.example.service.util.AgeBasedSalaryTestCase;
import org.jboss.example.service.util.LocationBasedSalaryTestCase;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Allow all SalaryStrategy tests to be run.
 * 
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public class SalaryStrategyTestSuite extends TestSuite
{
   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      TestSuite suite = new TestSuite("SalaryStrategy Tests");
      
      suite.addTestSuite(AgeBasedSalaryTestCase.class);
      suite.addTestSuite(LocationBasedSalaryTestCase.class);
      
      return suite;
   }
}
