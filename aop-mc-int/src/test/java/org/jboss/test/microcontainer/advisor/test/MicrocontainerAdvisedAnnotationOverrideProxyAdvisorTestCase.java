/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/ 
package org.jboss.test.microcontainer.advisor.test;

import junit.framework.Test;

import org.jboss.aop.Advised;
import org.jboss.aop.proxy.container.AspectManaged;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.support.MetaDataContextInterceptor;

/**
 * Tests that annotation overrides from AOP are readable
 *  
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class MicrocontainerAdvisedAnnotationOverrideProxyAdvisorTestCase extends AOPMicrocontainerTest
{
   public void testAdvisor() throws Exception
   {
      SomeBean o = (SomeBean) getBean("Intercepted");
      
      //Annotations added with overrides act the same as class annotations, by the stage the bean class
      //is loaded all the bindings will have been deployed 
      assertTrue(o instanceof Advised);
      assertFalse(o instanceof AspectManaged);
      
      MetaDataContextInterceptor.reset();
      o.doSomething();
      assertNotNull(MetaDataContextInterceptor.classAnnotation);
      assertTrue(MetaDataContextInterceptor.classAnnotation instanceof AnnotationWithValue);
      assertEquals(10, ((AnnotationWithValue)MetaDataContextInterceptor.classAnnotation).value());
      assertNull(MetaDataContextInterceptor.joinpointAnnotation);

      MetaDataContextInterceptor.reset();
      o.doSomethingElse();
      assertNotNull(MetaDataContextInterceptor.classAnnotation);
      assertTrue(MetaDataContextInterceptor.classAnnotation instanceof AnnotationWithValue);
      assertEquals(10, ((AnnotationWithValue)MetaDataContextInterceptor.classAnnotation).value());
      assertNotNull(MetaDataContextInterceptor.joinpointAnnotation);
      assertTrue(MetaDataContextInterceptor.joinpointAnnotation instanceof AnnotationWithValue);
      assertEquals(50, ((AnnotationWithValue)MetaDataContextInterceptor.joinpointAnnotation).value());
   }
   
   public static Test suite()
   {
      return suite(MicrocontainerAdvisedAnnotationOverrideProxyAdvisorTestCase.class);
   }

   public MicrocontainerAdvisedAnnotationOverrideProxyAdvisorTestCase(String name)
   {
      super(name);
   }

}
