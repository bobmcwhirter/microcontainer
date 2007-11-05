package org.jboss.example.client;

import junit.framework.TestCase;

public class ClientTestCase extends TestCase
{
   public ClientTestCase(String name) {
      super(name);
   }

   public void testClient() throws Exception {
      Client client = new Client(false);
      assertNotNull(client);
   }
}
