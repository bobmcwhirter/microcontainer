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
package org.jboss.test.kernel.annotations.test.wb;

import junit.framework.Test;
import org.jboss.kernel.plugins.annotations.AnnotationPlugin;
import org.jboss.kernel.plugins.annotations.BasicBeanAnnotationAdapter;
import org.jboss.kernel.plugins.annotations.BeanAnnotationAdapter;
import org.jboss.kernel.plugins.annotations.wb.AnnotationsAnnotationPluginFactory;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.test.kernel.annotations.support.Blue;
import org.jboss.test.kernel.annotations.support.Green;
import org.jboss.test.kernel.annotations.support.Red;
import org.jboss.test.kernel.annotations.support.RGBSupplier;
import org.jboss.test.kernel.annotations.support.RGBDemander;
import org.jboss.test.kernel.annotations.test.AbstractBeanAnnotationAdapterTest;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.info.spi.BeanAccessMode;

/**
 * WB annotations matching tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AnnotationsMatchingTestCase extends AbstractBeanAnnotationAdapterTest
{
   public AnnotationsMatchingTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(AnnotationsMatchingTestCase.class);
   }

   @SuppressWarnings("unchecked")
   protected BeanAnnotationAdapter getBeanAnnotationAdapterClass()
   {
      BasicBeanAnnotationAdapter basic = BasicBeanAnnotationAdapter.INSTANCE;

      // injection
      AnnotationPlugin redInjection = AnnotationsAnnotationPluginFactory.createPropertyInjectionPlugin(Red.class);
      basic.addAnnotationPlugin(redInjection);
      redInjection = AnnotationsAnnotationPluginFactory.createFieldInjectionPlugin(Red.class);
      basic.addAnnotationPlugin(redInjection);

      AnnotationPlugin greenInjection = AnnotationsAnnotationPluginFactory.createPropertyInjectionPlugin(Green.class);
      basic.addAnnotationPlugin(greenInjection);
      greenInjection = AnnotationsAnnotationPluginFactory.createFieldInjectionPlugin(Green.class);
      basic.addAnnotationPlugin(greenInjection);

      AnnotationPlugin blueInjection = AnnotationsAnnotationPluginFactory.createPropertyInjectionPlugin(Blue.class);
      basic.addAnnotationPlugin(blueInjection);
      blueInjection = AnnotationsAnnotationPluginFactory.createFieldInjectionPlugin(Blue.class);
      basic.addAnnotationPlugin(blueInjection);

      // class
      AnnotationPlugin redSupply = AnnotationsAnnotationPluginFactory.createClassPlugin(Red.class);
      basic.addAnnotationPlugin(redSupply);
      AnnotationPlugin greenSupply = AnnotationsAnnotationPluginFactory.createClassPlugin(Green.class);
      basic.addAnnotationPlugin(greenSupply);
      AnnotationPlugin blueSupply = AnnotationsAnnotationPluginFactory.createClassPlugin(Blue.class);
      basic.addAnnotationPlugin(blueSupply);

      return basic;
   }

   public void testAnnotationsInjection() throws Throwable
   {
      KernelController controller = getController();
      controller.install(new AbstractBeanMetaData("supplier", RGBSupplier.class.getName()));

      runAnnotationsOnClass(RGBDemander.class, BeanAccessMode.ALL);
   }

   protected void doTestAfterInstall(Object target)
   {
      RGBDemander demander = (RGBDemander)target;
      assertNotNull(demander.getRg());
      assertNotNull(demander.getGb());
      assertNotNull(demander.getRb());
      assertNotNull(demander.getRgb());
   }
}