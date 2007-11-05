package org.jboss.example.client;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class ClientTestSuite extends TestSuite
{
   public static void main(String[] args) {
      TestRunner.run(suite());
   }

   public static Test suite() {
      TestSuite suite = new TestSuite("Client Tests");

      suite.addTestSuite(ClientTestCase.class);

      return suite;
   }
}
