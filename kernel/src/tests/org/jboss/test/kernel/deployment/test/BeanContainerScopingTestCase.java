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

import java.util.List;

import junit.framework.Test;
import org.jboss.test.kernel.deployment.support.container.BaseContext;
import org.jboss.test.kernel.deployment.support.container.InstanceInterceptor;
import org.jboss.test.kernel.deployment.support.container.ScopedContainer;

/**
 * Test controller scopes.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BeanContainerScopingTestCase extends ScopingDeploymentTest
{
   public BeanContainerScopingTestCase(String name) throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(BeanContainerScopingTestCase.class);
   }

   public void testControllerScopes() throws Throwable
   {
      ScopedContainer sc1 = assertBean("CF1", ScopedContainer.class);
      List<Object> beans1 = sc1.createBeans("cf_base_1");
      assertNotNull(beans1);
      assertEquals(2, beans1.size());
      BaseContext<?, ?> bc1 = assertBean("cf_base_1$BeanContext", BaseContext.class);
      InstanceInterceptor ii1 = assertBean("cf_base_1$InstanceInterceptor", InstanceInterceptor.class);
      assertNotNull(bc1.getInterceptors());
      assertEquals(1, bc1.getInterceptors().size());
      assertSame(ii1, bc1.getInterceptors().get(0));

      ScopedContainer sc2 = assertBean("CF2", ScopedContainer.class);
      List<Object> beans2 = sc2.createBeans("cf_base_2");
      assertNotNull(beans2);
      assertEquals(2, beans2.size());
      BaseContext<?, ?> bc2 = assertBean("cf_base_2$BeanContext", BaseContext.class);
      InstanceInterceptor ii2 = assertBean("cf_base_2$InstanceInterceptor", InstanceInterceptor.class);
      assertNotNull(bc2.getInterceptors());
      assertEquals(1, bc2.getInterceptors().size());
      assertSame(ii2, bc2.getInterceptors().get(0));
   }
}
