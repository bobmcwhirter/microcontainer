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
package org.jboss.test.kernel.deployment.test;

import junit.framework.Test;
import org.jboss.test.kernel.deployment.support.StaticHolder;
import org.jboss.test.kernel.deployment.support.StaticInjector;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BeanContainerStaticTestCase extends AbstractDeploymentTest
{
   public BeanContainerStaticTestCase(String name) throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(BeanContainerStaticTestCase.class);
   }

   public void testStaticInjection() throws Throwable
   {
      StaticInjector injector = assertBean("StaticInjector", StaticInjector.class);
      Class<StaticHolder> clazz = StaticHolder.class;
      String string = "foobar";
      Integer number = 123;

      SecurityManager sm = suspendSecurity();
      try
      {
         injector.injectToNonPublicMethod(clazz, "privMain", string, Object.class);
         assertSame(string, StaticHolder.getPrivField());
         injector.injectToNonPublicMethod(clazz, "protMain", string, Object.class);
         assertSame(string, StaticHolder.getProtField());
         injector.injectToMethod(clazz, "pubMain", string, Object.class);
         assertSame(string, StaticHolder.pubField);

         injector.injectToField(clazz, "privField", number);
         assertSame(number, StaticHolder.getPrivField());
         injector.injectToField(clazz, "protField", number);
         assertSame(number, StaticHolder.getProtField());
         injector.injectToField(clazz, "pubField", number);
         assertSame(number, StaticHolder.pubField);
      }
      finally
      {
         resumeSecurity(sm);
      }
   }
}