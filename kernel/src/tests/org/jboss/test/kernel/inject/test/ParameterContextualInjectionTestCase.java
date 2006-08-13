package org.jboss.test.kernel.inject.test;

import org.jboss.test.kernel.junit.MicrocontainerTest;
import org.jboss.test.kernel.inject.support.TesterInterfaceGetter;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import junit.framework.Test;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class ParameterContextualInjectionTestCase extends MicrocontainerTest
{
   public ParameterContextualInjectionTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(ParameterContextualInjectionTestCase.class);
   }

   public void testContextualInjection() throws Throwable
   {
      KernelDeployment deployment = deploy("ParameterContextualInjection.xml");
      try
      {
         validate();         
      }
      finally
      {
         undeploy(deployment);
      }
   }

}
