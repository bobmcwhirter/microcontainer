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
package org.jboss.test.kernel.annotations.test.inheritance;

import junit.framework.Test;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.test.kernel.annotations.support.SubSetsAnnotationTester;
import org.jboss.test.kernel.annotations.support.SetsAnnotationTester;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;

/**
 * Sets annotation inheritance test.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SetsAnnotationInheritanceTestCase extends AbstractAnnotationInheritanceTest
{
   public SetsAnnotationInheritanceTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(SetsAnnotationInheritanceTestCase.class);
   }

   public void testInheritance() throws Throwable
   {
      KernelController controller = getController();
      AbstractBeanMetaData bmd1 = new AbstractBeanMetaData("deployer", Object.class.getName());
      controller.install(bmd1);
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("demander", Object.class.getName());
      builder.addDemand("somesupply");
      controller.install(builder.getBeanMetaData());
      runAnnotationsOnClass(SubSetsAnnotationTester.class);
      controller.install(new AbstractBeanMetaData("setssupplier", SetsAnnotationTester.class.getName()));
      checkDemander(ControllerState.INSTALLED);
   }

   protected void doTestAfterInstall()
   {
      checkDemander(ControllerState.PRE_INSTALL);
   }

   protected void checkDemander(ControllerState state)
   {
      try
      {
         Controller controller = getController();
         ControllerContext context = controller.getContext("demander", state);
         assertNotNull(context);
      }
      catch (Throwable t)
      {
         throw new RuntimeException(t);
      }
   }
}
