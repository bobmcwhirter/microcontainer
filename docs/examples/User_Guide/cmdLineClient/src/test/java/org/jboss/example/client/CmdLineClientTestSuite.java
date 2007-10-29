package org.jboss.example.client;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public class CmdLineClientTestSuite extends TestSuite
{

   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("CmdLineClient Tests");

      suite.addTest(CmdLineClientTestCase.suite());

      return suite;
   }

}
