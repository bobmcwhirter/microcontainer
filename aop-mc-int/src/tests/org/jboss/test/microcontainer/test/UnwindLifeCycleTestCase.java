package org.jboss.test.microcontainer.test;


import junit.framework.Test;

import org.jboss.aop.microcontainer.junit.AOPMicrocontainerTest;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.microcontainer.support.ErrorLifecycleCallback;
import org.jboss.test.microcontainer.support.InstallUninstallLifecycleCallback;

public class UnwindLifeCycleTestCase extends AOPMicrocontainerTest
{

   public UnwindLifeCycleTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(UnwindLifeCycleTestCase.class);
   }

   /**
    * Validate that the 
    * @throws Exception
    */
   public void testUnwind() throws Exception
   {
      try
      {
         deploy("UnwindLifeCycleTestCaseNotAutomatic.xml");
      }
      catch(Exception e)
      {
         
      }
      
      try
      {
         Object o = getBean("Bean");
      }
      catch (RuntimeException expected)
      {
      }
      
      getAssertInstalledLifecycleCallback("DescribeAdvice1");
      getAssertInstalledLifecycleCallback("DescribeAdvice2");
      getAssertInstalledLifecycleCallback("InstantiateAdvice1");
      getAssertInstalledLifecycleCallback("InstantiateAdvice2");
      getAssertInstalledLifecycleCallback("ConfigureAdvice1");
      getAssertInstalledLifecycleCallback("ConfigureAdvice2");
      getAssertInstalledLifecycleCallback("CreateAdvice1");
      getAssertInstalledLifecycleCallback("CreateAdvice2");
      getAssertInstalledLifecycleCallback("StartAdvice1");
      getAssertInstalledLifecycleCallback("StartAdvice2");
      getAssertInstalledLifecycleCallback("InstallAdvice1");
      getAssertInstalledLifecycleCallback("InstallAdvice2");

      //This is the bean causing the error during its install phase, make sure that both install
      //AND uninstall have been called since install might have had partial success before the error
      ErrorLifecycleCallback error = (ErrorLifecycleCallback)getBean("ErrorAdvice");
      assertTrue(error.isInstalledContext());
      assertTrue(error.isUninstalledContext());

      //This comes after the error advice in the chain, and so should not have had install or uninstall called
      InstallUninstallLifecycleCallback startNotInvoked = (InstallUninstallLifecycleCallback)getBean("InstallAdviceNotInvoked");
      assertFalse(startNotInvoked.isInstalledContext());
      assertFalse(startNotInvoked.isUninstalledContext());
      
      //Lifecycle callbacks from previous states should have had uninstall called
      getAssertUninstalledLifecycleCallback("DescribeAdvice1");
      getAssertUninstalledLifecycleCallback("DescribeAdvice2");
      getAssertUninstalledLifecycleCallback("InstantiateAdvice1");
      getAssertUninstalledLifecycleCallback("InstantiateAdvice2");
      getAssertUninstalledLifecycleCallback("ConfigureAdvice1");
      getAssertUninstalledLifecycleCallback("ConfigureAdvice2");
      getAssertUninstalledLifecycleCallback("CreateAdvice1");
      getAssertUninstalledLifecycleCallback("CreateAdvice2");
      getAssertUninstalledLifecycleCallback("StartAdvice1");
      getAssertUninstalledLifecycleCallback("StartAdvice2");
      getAssertUninstalledLifecycleCallback("InstallAdvice1");
      getAssertUninstalledLifecycleCallback("InstallAdvice2");
   }
   
   private void getAssertInstalledLifecycleCallback(String callbackName)
   {
      InstallUninstallLifecycleCallback callback = (InstallUninstallLifecycleCallback)getBean(callbackName);
      assertTrue(callback.isInstalledContext());
   }
   
   private void getAssertUninstalledLifecycleCallback(String callbackName)
   {
      InstallUninstallLifecycleCallback callback = (InstallUninstallLifecycleCallback)getBean(callbackName);
      assertTrue(callback.isUninstalledContext());
   }
}
