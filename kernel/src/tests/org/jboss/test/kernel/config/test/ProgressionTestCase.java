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
package org.jboss.test.kernel.config.test;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.test.kernel.config.support.SimpleBean;

/**
 * Progression Test Case.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class ProgressionTestCase extends AbstractKernelConfigTest
{
   public static Test suite()
   {
      return suite(ProgressionTestCase.class);
   }

   public ProgressionTestCase(String name)
   {
      super(name);
   }

   public ProgressionTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public void testProgressionFromOtherBean() throws Throwable
   {
      SimpleBean bean = instantiateProgressionBeans();
      assertEquals(123, bean.getAnint());
      assertEquals((short)987, bean.getAShort().shortValue());
      assertEquals(314159f, bean.getAFloat().floatValue());
   }

   protected SimpleBean instantiateProgressionBeans() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();

      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      bmd.addProperty(new AbstractPropertyMetaData("anint", 123.456));
      bmd.addProperty(new AbstractPropertyMetaData("AShort", 987.6543));
      bmd.addProperty(new AbstractPropertyMetaData("AFloat", 314159));

      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

}
