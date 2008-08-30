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
package org.jboss.test.kernel.dependency.test;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.test.kernel.dependency.support.SimpleBeanWithKernelControllerContextAware;

/**
 * KernelControllerContextAware Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class KernelControllerContextAwareTestCase extends OldAbstractKernelDependencyTest
{
   public static Test suite()
   {
      return suite(KernelControllerContextAwareTestCase.class);
   }

   public KernelControllerContextAwareTestCase(String name) throws Throwable
   {
      super(name);
   }

   public KernelControllerContextAwareTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public void testKernelControllerContextAware() throws Throwable
   {
      kernelControllerContextAware();

      ControllerContext context1 = assertInstall(0, "Name1");
      
      SimpleBeanWithKernelControllerContextAware bean1 = (SimpleBeanWithKernelControllerContextAware) context1.getTarget();
      assertNotNull(bean1);
      assertEquals(context1, bean1.context);
      
      assertUninstall("Name1");
      assertNull(bean1.context);
   }

   public void kernelControllerContextAware() throws Throwable
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("Name1", SimpleBeanWithKernelControllerContextAware.class.getName());
      setBeanMetaDatas(new BeanMetaData[] { metaData1 });
   }
}