/*
 * 
 */

package org.jboss.test.kernel.inject.test;

import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.kernel.junit.MicrocontainerTest;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public abstract class ContextualInjectionAdapter extends MicrocontainerTest
{

   public ContextualInjectionAdapter(String name)
   {
      super(name);
   }

   protected abstract String getResource();

   public void testInjection() throws Throwable
   {
      KernelDeployment deployment = deploy(getResource());
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
