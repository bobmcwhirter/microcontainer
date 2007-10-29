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
package org.jboss.test.kernel.dependency.test;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.dependency.support.CustomMatcherTransfomer;

/**
 * Test matcher demand/supply.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class MatcherDemandSupplyTestCase extends AbstractKernelDependencyTest
{
   public MatcherDemandSupplyTestCase(String name)
         throws Throwable
   {
      super(name);
   }

   public MatcherDemandSupplyTestCase(String name, boolean xmltest)
         throws Throwable
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(MatcherDemandSupplyTestCase.class);
   }

   public void testMatching() throws Throwable
   {
      setBeanMetaDatas();

      ControllerContext regexpDemand = assertInstall(0, "regexpDemander", getState());
      assertInstall(1, "regexpSupplier");
      assertEquals(ControllerState.INSTALLED, regexpDemand.getState());

      ControllerContext limitDemand = assertInstall(2, "intervalDemander", getState());
      assertInstall(3, "intervalSupplier");
      assertEquals(ControllerState.INSTALLED, limitDemand.getState());

      ControllerContext customDemand = assertInstall(4, "customDemander", getState());
      assertInstall(5, "customSupplier");
      assertEquals(ControllerState.INSTALLED, customDemand.getState());
   }

   protected ControllerState getState()
   {
      return ControllerState.PRE_INSTALL;
   }

   protected void setBeanMetaDatas() throws Throwable
   {
      BeanMetaDataBuilder b0 = BeanMetaDataBuilderFactory.createBuilder("regexpDemander", Object.class.getName());
      b0.addDemand("^[a-zA-Z0-9._%+-]+@acme\\.((org)|(com))$", null, "regexp");
      BeanMetaDataBuilder b1 = BeanMetaDataBuilderFactory.createBuilder("regexpSupplier", Object.class.getName());
      b1.addSupply("aj@acme.org");

      BeanMetaDataBuilder b2 = BeanMetaDataBuilderFactory.createBuilder("intervalDemander", Object.class.getName());
      b2.addDemand("(1,10]", null, "interval");
      BeanMetaDataBuilder b3 = BeanMetaDataBuilderFactory.createBuilder("intervalSupplier", Object.class.getName());
      b3.addSupply("5", "java.lang.Integer");

      BeanMetaDataBuilder b4 = BeanMetaDataBuilderFactory.createBuilder("customDemander", Object.class.getName());
      b4.addDemand("fragment", null, CustomMatcherTransfomer.class.getName());
      BeanMetaDataBuilder b5 = BeanMetaDataBuilderFactory.createBuilder("customSupplier", Object.class.getName());
      b5.addSupply("i supply fragment word");

      setBeanMetaDatas(new BeanMetaData[]
            {
               b0.getBeanMetaData(),
               b1.getBeanMetaData(),
               b2.getBeanMetaData(),
               b3.getBeanMetaData(),
               b4.getBeanMetaData(),
               b5.getBeanMetaData(),
            }
      );
   }
}
