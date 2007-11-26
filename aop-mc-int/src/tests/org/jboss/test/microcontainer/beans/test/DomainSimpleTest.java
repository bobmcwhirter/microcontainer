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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jboss.aop.AspectManager;
import org.jboss.test.microcontainer.beans.POJO;
import org.jboss.test.microcontainer.beans.TestAspect;
import org.jboss.test.microcontainer.beans.TestAspectWithProperty;
import org.jboss.test.microcontainer.beans.TestInterceptor;


/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public abstract class DomainSimpleTest extends DomainProxyTest
{
   public DomainSimpleTest(String name)
   {
      super(name);
   }

   public void testDomain()
   {
      TestAspect.invoked = false;
      TestInterceptor.invoked = false;
      TestAspectWithProperty.last = null;
      POJO pojo = (POJO)createProxy("AOPDomain", new POJO());
      assertNotNull(pojo);
      pojo.method();
      assertNotNull(TestAspectWithProperty.last);
      assertEquals("This is only a test", TestAspectWithProperty.last.getSomeProperty());
      assertTrue(TestInterceptor.invoked);
      assertTrue(TestAspect.invoked);
    
      AspectManager domain = getDomain("AOPDomain");
      checkArtifacts(domain, true);
   }

   public void testNoDomain()
   {
      TestInterceptor.invoked = false;
      TestAspectWithProperty.last = null;
      TestAspect.invoked = false;
      POJO pojo = (POJO)createProxy(new POJO());
      assertNotNull(pojo);
      pojo.method();
      assertNull(TestAspectWithProperty.last);
      assertFalse(TestInterceptor.invoked);
      assertFalse(TestAspect.invoked);
      
      AspectManager manager = getMainAspectManager();
      checkArtifacts(manager, false);
   }
   
   private void checkArtifacts(AspectManager manager, boolean shouldBeThere)
   {
      Object o = manager.getTypedef("TypeDef");
      checkShouldBeThere(o, shouldBeThere);
      
      o = manager.getCFlowStack("CFlow");
      checkShouldBeThere(o, shouldBeThere);
      
      o = manager.getDynamicCFlow("DynamicCFlow");
      checkShouldBeThere(o, shouldBeThere);
      
      o = manager.getPointcut("Prepare");
      checkShouldBeThere(o, shouldBeThere);
      
      o = manager.getPointcut("NamedPointcut");
      checkShouldBeThere(o, shouldBeThere);
   
      o = manager.getInterfaceIntroduction("MixinBinding");
      checkShouldBeThere(o, shouldBeThere);
      
      List coll =  manager.getAnnotationIntroductions();
      checkShouldBeThere(coll, shouldBeThere);
      
      coll = manager.getAnnotationOverrides();
      checkShouldBeThere(coll, shouldBeThere);
      
      Map map = manager.getPrecedenceDefs();
      checkShouldBeThere(map, shouldBeThere);
      
      map = manager.getArrayReplacements();
      checkShouldBeThere(map, shouldBeThere);
      
      try
      {
         o = manager.getArrayBinding("TestArrayBinding");
         checkShouldBeThere(o, shouldBeThere);
      }
      catch (ClassCastException expected)
      {
         // TODO remove after AOP 2.0.0.CR1 which fixes the bug
      }
      
   }
   
   private void checkShouldBeThere(Object o, boolean shouldBeThere)
   {
      if (shouldBeThere) assertNotNull(o);
      else assertNull(o);
   }
   
   private void checkShouldBeThere(Collection coll, boolean shouldBeThere)
   {
      if (shouldBeThere)
      {
         assertNotNull(coll);
         assertEquals(1, coll.size());
      }
      else
      {
         if (coll != null)
         {
            assertEquals(0, coll.size());
         }
      }
   }
   
   private void checkShouldBeThere(Map coll, boolean shouldBeThere)
   {
      if (shouldBeThere)
      {
         assertNotNull(coll);
         assertEquals(1, coll.size());
      }
      else
      {
         if (coll != null)
         {
            assertEquals(0, coll.size());
         }
      }
   }
}
