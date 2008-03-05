package org.jboss.test.microcontainer.test;


import junit.framework.Test;

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.support.SimpleBeanImpl;
import org.jboss.test.microcontainer.support.SimpleInterceptor1;
import org.jboss.test.microcontainer.support.SimpleInterceptor2;

public class UndeployAspectDependencyTestCase extends AOPMicrocontainerTest
{

   public UndeployAspectDependencyTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(UndeployAspectDependencyTestCase.class);
   }

   /**
    * Validate that the 
    * @throws Exception
    */
   public void testUndeployAndRedeploy() throws Throwable
   {
      //Deploy first aspect
      deploy("UndeployAspectDependencyTestCaseNotAutomatic0.xml");
      try
      {
         boolean deployedAspect = false;
         //Deploy second aspect
         deploy("UndeployAspectDependencyTestCaseNotAutomatic1.xml");
         try
         {
            //Deploy bean intercepted by both aspects
            deploy("UndeployAspectDependencyTestCaseNotAutomatic2.xml");
            try
            {
               SimpleBeanImpl bean = (SimpleBeanImpl)getBean("Intercepted");
               SimpleInterceptor1.invoked = false;
               SimpleInterceptor2.invoked = false;
               bean.someMethod();
               assertTrue(SimpleInterceptor1.invoked);
               assertTrue(SimpleInterceptor2.invoked);

               ControllerContext ctx = getControllerContext("Intercepted");
               Controller controller = ctx.getController();
               
               //Move the bean intercepted to pre-install state
               controller.change(ctx, ControllerState.PRE_INSTALL);
               
               //Undeploy the second aspect
               undeploy("UndeployAspectDependencyTestCaseNotAutomatic1.xml");
               
               //Move the bean back to the installed state
               controller.change(ctx, ControllerState.INSTALLED);
               
               bean = (SimpleBeanImpl)getBean("Intercepted");
               SimpleInterceptor1.invoked = false;
               SimpleInterceptor2.invoked = false;
               bean.someMethod();
               assertTrue(SimpleInterceptor1.invoked);
               assertFalse(SimpleInterceptor2.invoked);
            }
            finally
            {
               undeploy("UndeployAspectDependencyTestCaseNotAutomatic2.xml");
            }
         }
         finally
         {
            if (deployedAspect)
            {
               undeploy("UndeployAspectDependencyTestCaseNotAutomatic1.xml");
            }
         }
      }
      finally
      {
         undeploy("UndeployAspectDependencyTestCaseNotAutomatic0.xml");
      }

   }
}
