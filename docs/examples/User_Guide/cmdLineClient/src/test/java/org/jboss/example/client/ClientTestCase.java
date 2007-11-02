package org.jboss.example.client;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ClientTestCase extends TestCase
{
   public ClientTestCase(String name)
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
      suite.addTest(new TestSuite(ClientTestCase.class));
      return suite;
   }

   public void testConfigure() throws Exception
   {
      Client client = new Client(false);
      assertNotNull(client);
   }

}
