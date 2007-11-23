/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.test.microcontainer.beans.test;

import org.jboss.aop.microcontainer.junit.AOPMicrocontainerTest;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.microcontainer.beans.Dependency;
import org.jboss.test.microcontainer.beans.POJO;
import org.jboss.test.microcontainer.beans.TestAspectWithDependency;
import org.jboss.test.microcontainer.beans.TestInterceptorWithDependency;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public abstract class StackWithMultipleDependencyTest extends AOPMicrocontainerTest
{

   public StackWithMultipleDependencyTest(String name)
   {
      super(name);
   }

   protected abstract String getFile0();
   protected abstract String getFile1();
   protected abstract String getFile2();
   
   public void testInterceptorWithDependencyCorrectOrder() throws Exception
   {
      deploy(getFile0());
      try
      {
         Dependency dependency1 = (Dependency) getBean("Dependency1");
         assertNotNull(dependency1);
         deploy(getFile1());
         Dependency dependency2 = (Dependency) getBean("Dependency2");
         assertNotNull(dependency2);
         try
         {
            deploy(getFile2());
            try
            {
               validate();
               checkIntercepted1(dependency1);
               checkIntercepted2(dependency2);
               checkIntercepted3(dependency1, dependency2);
            }
            finally
            {
               undeploy(getFile2());
            }
         }
         finally
         {
            undeploy(getFile1());
         }
      }
      finally
      {
         undeploy(getFile0());
      }
   }

   public void testInterceptorWithDependencyWrongOrder() throws Exception
   {
      deploy(getFile2());
      try
      {
         checkNoIntercepted1();
         checkNoIntercepted2();
         checkNoIntercepted3();
         
         deploy(getFile0());
         try
         {
            Dependency dependency1 = (Dependency) getBean("Dependency1");
            assertNotNull(dependency1);
            checkIntercepted1(dependency1);
            checkNoIntercepted2();
            checkNoIntercepted3();
            
            deploy(getFile1());
            try
            {
               validate();
               Dependency dependency2 = (Dependency) getBean("Dependency2");
               assertNotNull(dependency2);
               checkIntercepted1(dependency1);
               checkIntercepted2(dependency2);
               checkIntercepted3(dependency1, dependency2);
            }
            finally
            {
               undeploy(getFile1());
            }
         }
         finally
         {
            undeploy(getFile0());
         }
      }
      finally
      {
         undeploy(getFile2());
      }
   }

   public void testInterceptorWithDependencyRedeploy() throws Exception
   {
      deploy(getFile2());
      try
      {
         checkNoIntercepted1();
         checkNoIntercepted2();
         checkNoIntercepted3();
         deploy(getFile0());
         try
         {
            Dependency dependency1 = (Dependency) getBean("Dependency1");
            assertNotNull(dependency1);
            
            checkIntercepted1(dependency1);
            checkNoIntercepted2();
            checkNoIntercepted3();
            
            deploy(getFile1());
            try
            {
               validate();
               Dependency dependency2 = (Dependency) getBean("Dependency2");
               assertNotNull(dependency2);
               
               checkIntercepted1(dependency1);
               checkIntercepted2(dependency2);
               checkIntercepted3(dependency1, dependency2);
            }
            finally
            {
               undeploy(getFile1());
            }
         }
         finally
         {
            undeploy(getFile0());
         }
         
         checkNoIntercepted1();
         checkNoIntercepted2();
         checkNoIntercepted3();
   
         deploy(getFile1());
         try
         {
            Dependency dependency2 = (Dependency) getBean("Dependency2");
            assertNotNull(dependency2);
            
            checkNoIntercepted1();
            checkIntercepted2(dependency2);
            checkNoIntercepted3();
            
            deploy(getFile0());
            try
            {
               validate();
               Dependency dependency1 = (Dependency) getBean("Dependency1");
               assertNotNull(dependency1);
               
               checkIntercepted1(dependency1);
               checkIntercepted2(dependency2);
               checkIntercepted3(dependency1, dependency2);
            }
            finally
            {
               undeploy(getFile0());
            }
         }
         finally
         {
            undeploy(getFile1());
         }
      }
      finally
      {
         undeploy(getFile2());
      }
   }

   public void testInterceptorWithDependencyRedeploy2() throws Exception
   {
      deploy(getFile0());
      try
      {
         Dependency dependency1 = (Dependency) getBean("Dependency1");
         assertNotNull(dependency1);
         deploy(getFile1());
         try
         {
            Dependency dependency2 = (Dependency) getBean("Dependency2");
            assertNotNull(dependency2);
            deploy(getFile2());
            try
            {
               validate();
               checkIntercepted1(dependency1);
               checkIntercepted2(dependency2);
               checkIntercepted3(dependency1, dependency2);
            }
            finally
            {
               undeploy(getFile2());
            }
         }
         finally
         {
            undeploy(getFile1());
         }
   
         assertNotNull(dependency1);
         deploy(getFile1());
         try
         {
            Dependency dependency2 = (Dependency) getBean("Dependency2");
            assertNotNull(dependency2);
            deploy(getFile2());
            try
            {
               validate();
               checkIntercepted1(dependency1);
               checkIntercepted2(dependency2);
               checkIntercepted3(dependency1, dependency2);
            }
            finally
            {
               undeploy(getFile2());
            }
         }
         finally
         {
            undeploy(getFile1());
         }
      }
      finally
      {
         undeploy(getFile0());
      }
   }

   private void checkIntercepted1(Dependency dependency1)
   {
      POJO pojo1 = (POJO) getBean("Intercepted1");
      assertNotNull(pojo1);
      TestAspectWithDependency.invoked = null;
      TestInterceptorWithDependency.invoked = null;
      pojo1.method(2);
      assertNotNull(TestAspectWithDependency.invoked);
      assertTrue(dependency1 == TestAspectWithDependency.invoked);
      assertNull(TestInterceptorWithDependency.invoked);
   }

   private void checkIntercepted2(Dependency dependency2)
   {
      POJO pojo2 = (POJO) getBean("Intercepted2");
      assertNotNull(pojo2);
      TestAspectWithDependency.invoked = null;
      TestInterceptorWithDependency.invoked = null;
      pojo2.method(2);
      assertNull(TestAspectWithDependency.invoked);
      assertNotNull(TestInterceptorWithDependency.invoked);
      System.out.println(TestInterceptorWithDependency.invoked);
      assertTrue(dependency2 == TestInterceptorWithDependency.invoked);
   }

   private void checkIntercepted3(Dependency dependency1, Dependency dependency2)
   {
      POJO pojo3 = (POJO) getBean("Intercepted3");
      assertNotNull(pojo3);
      TestAspectWithDependency.invoked = null;
      TestInterceptorWithDependency.invoked = null;
      pojo3.method(2);
      assertNotNull(TestAspectWithDependency.invoked);
      assertTrue(dependency1 == TestAspectWithDependency.invoked);
      assertNotNull(TestInterceptorWithDependency.invoked);
      assertTrue(dependency2 == TestInterceptorWithDependency.invoked);
   }

   private void checkNoIntercepted1()
   {
      checkNoIntercepted("Intercepted1");
   }

   private void checkNoIntercepted2()
   {
      checkNoIntercepted("Intercepted2");
   }

   private void checkNoIntercepted3()
   {
      checkNoIntercepted("Intercepted3");
   }

   private void checkNoIntercepted(String name)
   {
      POJO pojo;
      try
      {
         pojo = (POJO) getBean(name);
         fail("'" + name + "' should not be installed yet");
      }
      catch (IllegalStateException expected)
      {
      }
      
      pojo = (POJO) getBean(name, ControllerState.DESCRIBED);
      assertNull(name + " should not be deployed until the interceptor is", pojo);
   }

}