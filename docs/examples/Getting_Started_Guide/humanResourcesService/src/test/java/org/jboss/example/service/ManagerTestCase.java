package org.jboss.example.service;

import junit.framework.Test;

import org.jboss.test.kernel.junit.MicrocontainerTest;
import org.jboss.example.service.Manager;

/**
 * @author <a href="mailto:mark.newton@jboss.org">Mark Newton</a>
 */
public class ManagerTestCase extends MicrocontainerTest
{
   public ManagerTestCase(String name)
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
      return suite(ManagerTestCase.class);
   }

   public void testConfigure() throws Exception
   {
      Manager manager = (Manager) getBean("HumanResources");
      assertNotNull(manager);
   }

}
