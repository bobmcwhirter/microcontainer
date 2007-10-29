package org.jboss.example.client;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public class CmdLineClientTestCase extends TestCase
{
   public CmdLineClientTestCase(String name)
   {
      super(name);
   }

   /**
    * Setup the test
    *
    * @return the test
    */
   public static Test suite()
   {
      TestSuite suite = new TestSuite();
      suite.addTest(new TestSuite(CmdLineClientTestCase.class));
      return suite;
   }

   public void testConfigure() throws Exception
   {
      CmdLineClient client = new CmdLineClient();
      assertNotNull(client);
   }

}
