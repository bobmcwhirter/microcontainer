/*
 * 
 */

package org.jboss.test.kernel.inject.test;

import junit.framework.Test;
import org.jboss.test.kernel.inject.support.LifecycleInjectTestObject;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class LifecycleContextualInjectionTestCase extends ContextualInjectionAdapter
{
   public LifecycleContextualInjectionTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(LifecycleContextualInjectionTestCase.class);
   }

   protected String getResource()
   {
      return "LifecycleContextualInjection.xml";
   }

   protected void checkInjection()
   {
      LifecycleInjectTestObject test1 = (LifecycleInjectTestObject) getBean("testObject1");
      assertNotNull(test1.getTesterInterface());
   }

}
