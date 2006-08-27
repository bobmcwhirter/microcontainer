/*
 * 
 */

package org.jboss.test.kernel.inject.test;

import junit.framework.Test;
import org.jboss.test.kernel.inject.support.ConstructorInjectTestObject;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class ConstructorContextualInjectionTestCase extends ContextualInjectionAdapter
{
   public ConstructorContextualInjectionTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(ConstructorContextualInjectionTestCase.class);
   }

   protected String getResource()
   {
      return "ConstructorContextualInjection.xml";
   }

   protected void checkInjection()
   {
      ConstructorInjectTestObject test1 = (ConstructorInjectTestObject) getBean("testObject1");
      assertNotNull(test1.getTesterInterface());

      ConstructorInjectTestObject test2 = (ConstructorInjectTestObject) getBean("testObject2");
      assertFalse(test2.getTesterInterfaces().isEmpty());

      ConstructorInjectTestObject test3 = (ConstructorInjectTestObject) getBean("testObject3");
      assertNotNull(test3.getTesterInterface());

      ConstructorInjectTestObject test4 = (ConstructorInjectTestObject) getBean("testObject4");
      assertNotNull(test4.getTesterInterface());
   }

}
