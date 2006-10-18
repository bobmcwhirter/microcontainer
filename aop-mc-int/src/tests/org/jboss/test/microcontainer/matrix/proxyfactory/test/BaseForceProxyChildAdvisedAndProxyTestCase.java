/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.microcontainer.matrix.proxyfactory.test;


import junit.framework.Test;

import org.jboss.aop.Advised;
import org.jboss.aop.AspectManager;
import org.jboss.aop.microcontainer.junit.AbstractProxyTest;
import org.jboss.aop.proxy.container.AspectManaged;
import org.jboss.test.microcontainer.matrix.Base;
import org.jboss.test.microcontainer.matrix.Child;
import org.jboss.test.microcontainer.matrix.ChildInterface;
import org.jboss.test.microcontainer.matrix.DynamicAspectDeployer;
import org.jboss.test.microcontainer.matrix.TestInterceptor;

/**
 * Forces a proxy to be used for the base class, which in turn makes the advisor for that class a ClassProxyContainer
 * The idea is to check a creation of the classadvisor for the child class when the superclass advisor is a container. 
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class BaseForceProxyChildAdvisedAndProxyTestCase extends AbstractProxyTest
{
   public void testProxieddBaseClass() throws Exception
   {
      Base base = new Base();
      assertFalse(base instanceof Advised);
      String name = DynamicAspectDeployer.addBinding("execution(* org.jboss.test.microcontainer.matrix.Base->*(..))", TestInterceptor.class);
      try
      {
         Object proxy = createProxy(base);
         assertTrue(proxy instanceof AspectManaged);
         assertFalse(proxy instanceof Advised);
         
         TestInterceptor.reset();
         base.baseOnly();
         assertEquals(0, TestInterceptor.interceptions);
         
         TestInterceptor.reset();
         base.baseOverridden();
         assertEquals(0, TestInterceptor.interceptions);
         
         TestInterceptor.reset();
         ((Base)proxy).baseOnly();
         assertEquals(1, TestInterceptor.interceptions);
         assertNotNull(TestInterceptor.invoked);
         assertEquals("baseOnly", TestInterceptor.invoked.getName());
         
         TestInterceptor.reset();
         ((Base)proxy).baseOverridden();
         assertEquals(1, TestInterceptor.interceptions);
         assertNotNull(TestInterceptor.invoked);
         assertEquals("baseOverridden", TestInterceptor.invoked.getName());
      }
      finally
      {
         DynamicAspectDeployer.removeBinding(name);
      }
}
   
   public void testAdvisedChild() throws Exception
   {
      String name = DynamicAspectDeployer.addBinding("execution(* org.jboss.test.microcontainer.matrix.Base->*(..))", TestInterceptor.class);
      try
      {
         Child plainChild = new Child();
         assertTrue(plainChild instanceof Advised);
         assertFalse(plainChild instanceof AspectManaged);
         
         TestInterceptor.reset();
         plainChild.baseOnly();
         assertEquals(0, TestInterceptor.interceptions);
   
         TestInterceptor.reset();
         plainChild.childOnly();
         assertEquals(1, TestInterceptor.interceptions);
   
         TestInterceptor.reset();
         plainChild.baseOverridden();
         assertEquals(1, TestInterceptor.interceptions);
      }
      finally
      {
         DynamicAspectDeployer.removeBinding(name);
      }
   }
   
   public void testProxiedAndAdvisedChild() throws Exception
   {
      System.out.println("=======> Adding Binding");
      String name = DynamicAspectDeployer.addBinding("execution(* org.jboss.test.microcontainer.matrix.Base->*(..))", TestInterceptor.class);
      Object proxy = null;
      try
      {
   
         Child proxiedChild = new Child();
         assertTrue(proxiedChild instanceof Advised);
         assertFalse(proxiedChild instanceof AspectManaged);
   
         System.out.println("=======> Creating proxy");
         proxy = createProxy(proxiedChild, new Class[] {ChildInterface.class});
         
         TestInterceptor.reset();
         ((Child)proxy).baseOnly();
         assertEquals(1, TestInterceptor.interceptions);
         assertNotNull(TestInterceptor.invoked);
         assertEquals("baseOnly", TestInterceptor.invoked.getName());
   
         TestInterceptor.reset();
         ((Child)proxy).childOnly();
         assertEquals(2, TestInterceptor.interceptions);
         assertNotNull(TestInterceptor.invoked);
         assertEquals("childOnly", TestInterceptor.invoked.getName());
   
         TestInterceptor.reset();
         ((Child)proxy).baseOverridden();
         assertEquals(2, TestInterceptor.interceptions);
         assertNotNull(TestInterceptor.invoked);
         assertEquals("baseOverridden", TestInterceptor.invoked.getName());
      }
      finally
      {
         System.out.println("=======> Removing Binding");
         DynamicAspectDeployer.removeBinding(name);
      }
      
      TestInterceptor.reset();
      ((Child)proxy).baseOnly();
      assertEquals(1, TestInterceptor.interceptions);
   }
   
   public static Test suite()
   {
      return suite(BaseForceProxyChildAdvisedAndProxyTestCase.class);
   }

   public BaseForceProxyChildAdvisedAndProxyTestCase(String name)
   {
      super(name);
   }
}
