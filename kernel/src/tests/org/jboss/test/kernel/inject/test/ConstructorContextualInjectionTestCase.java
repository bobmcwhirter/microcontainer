/*
 * 
 */

package org.jboss.test.kernel.inject.test;

import junit.framework.Test;

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
}
