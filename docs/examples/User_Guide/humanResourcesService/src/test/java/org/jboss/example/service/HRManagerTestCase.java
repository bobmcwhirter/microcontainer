package org.jboss.example.service;

import junit.framework.Test;

import org.jboss.test.kernel.junit.MicrocontainerTest;
import org.jboss.example.service.HRManager;

public class HRManagerTestCase extends MicrocontainerTest
{
   public HRManagerTestCase(String name) {
      super(name);
   }

   public static Test suite() {
      return suite(HRManagerTestCase.class);
   }

   public void testConfigure() throws Exception {
      HRManager manager = (HRManager) getBean("HRService");
      assertNotNull(manager);
   }
}
