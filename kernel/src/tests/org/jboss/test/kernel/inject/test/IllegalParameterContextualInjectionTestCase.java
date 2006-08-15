/*
 * 
 */

package org.jboss.test.kernel.inject.test;

import org.jboss.test.kernel.junit.MicrocontainerTest;
import org.jboss.test.kernel.inject.support.ParameterTestObject;
import org.jboss.kernel.spi.deployment.KernelDeployment;

import junit.framework.Test;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class IllegalParameterContextualInjectionTestCase extends MicrocontainerTest
{
   public IllegalParameterContextualInjectionTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(IllegalParameterContextualInjectionTestCase.class);
   }

   public void testContextualInjection() throws Throwable
   {
      KernelDeployment deployment = null;
      try
      {
         deployment = deploy("IllegalParameterContextualInjection.xml");
         validate();

         ParameterTestObject pto = (ParameterTestObject) getBean("parameterObject1");
         assertNotNull(pto.getDuplicateTester());

      }
      catch(Throwable t)
      {
         getLog().info("Expected throwable: " + t);
      }
      finally
      {
         if (deployment != null)
         {
            undeploy(deployment);
         }
      }
   }

}
