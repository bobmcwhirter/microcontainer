package org.jboss.example.service;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class HRServiceTestSuite extends TestSuite
{
   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      TestSuite suite = new TestSuite("Human Resources Service Tests");
      
      suite.addTest(HRManagerTestCase.suite());
      
      return suite;
   }
}
