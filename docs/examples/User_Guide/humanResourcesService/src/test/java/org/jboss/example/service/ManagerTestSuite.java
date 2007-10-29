package org.jboss.example.service;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public class ManagerTestSuite extends TestSuite
{

   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("Manager Tests");

      suite.addTest(ManagerTestCase.suite());

      return suite;
   }

}
