package org.jboss.test.microcontainer.test;


import junit.framework.Test;

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.support.SimpleBeanImpl;
import org.jboss.test.microcontainer.support.SimpleLifecycleCallback;
import org.jboss.test.microcontainer.support.SimpleLifecycleCallback.Handled;

public class UndeployLifecycleCallbackDependencyTestCase extends AOPMicrocontainerTest
{

   public UndeployLifecycleCallbackDependencyTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(UndeployLifecycleCallbackDependencyTestCase.class);
   }

   /**
    * Validate that the 
    * @throws Throwable for any error
    */
   public void testUndeployAndRedeploy() throws Throwable
   {
      //Deploy first aspect
      deploy("UndeployLifecycleCallbackDependencyTestCaseNotAutomatic0.xml");
      try
      {
         boolean deployedAspect = false;
         //Deploy second aspect
         deploy("UndeployLifecycleCallbackDependencyTestCaseNotAutomatic1.xml");
         try
         {
            SimpleLifecycleCallback.clear();
            assertEquals(0, SimpleLifecycleCallback.interceptions.size());
            //Deploy bean intercepted by both aspects
            deploy("UndeployLifecycleCallbackDependencyTestCaseNotAutomatic2.xml");
            try
            {
               SimpleBeanImpl bean = (SimpleBeanImpl)getBean("Intercepted");
               assertNotNull(bean);
               assertEquals(2, SimpleLifecycleCallback.interceptions.size());
               assertTrue(hasExpectedInterception("Intercepted", ControllerState.CONFIGURED));
               assertTrue(hasExpectedInterception("Intercepted", ControllerState.START));

               SimpleLifecycleCallback.clear();

               ControllerContext ctx = getControllerContext("Intercepted");
               Controller controller = ctx.getController();
               
               //Move the bean intercepted to pre-install state
               controller.change(ctx, ControllerState.PRE_INSTALL);

               assertEquals(2, SimpleLifecycleCallback.interceptions.size());
               assertTrue(hasExpectedInterception("Intercepted", ControllerState.CONFIGURED));
               assertTrue(hasExpectedInterception("Intercepted", ControllerState.START));
               
               //Undeploy the second aspect
               undeploy("UndeployLifecycleCallbackDependencyTestCaseNotAutomatic1.xml");
               
               //Move the bean back to the installed state
               controller.change(ctx, ControllerState.INSTALLED);
               
               bean = (SimpleBeanImpl)getBean("Intercepted");
//               SimpleInterceptor1.invoked = false;
//               SimpleInterceptor2.invoked = false;
//               bean.someMethod();
//               assertTrue(SimpleInterceptor1.invoked);
//               assertFalse(SimpleInterceptor2.invoked);
            }
            finally
            {
               undeploy("UndeployLifecycleCallbackDependencyTestCaseNotAutomatic2.xml");
            }
         }
         finally
         {
            if (deployedAspect)
            {
               undeploy("UndeployLifecycleCallbackDependencyTestCaseNotAutomatic1.xml");
            }
         }
      }
      finally
      {
         undeploy("UndeployLifecycleCallbackDependencyTestCaseNotAutomatic0.xml");
      }
   }
   
   private boolean hasExpectedInterception(String tgt, ControllerState state)
   {
      for (Handled handled : SimpleLifecycleCallback.interceptions)
      {
         if (handled.contextName.equals(tgt) && handled.toState.equals(state))
         {
            return true;
         }
      }
      return false;
   }
//   
//   private void getAssertUninstalledLifecycleCallback(String callbackName)
//   {
//      InstallUninstallLifecycleCallback callback = (InstallUninstallLifecycleCallback)getBean(callbackName);
//      assertTrue(callback.isUninstalledContext());
//   }
}
