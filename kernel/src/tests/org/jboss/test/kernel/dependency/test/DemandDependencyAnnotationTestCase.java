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
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.test.kernel.dependency.support.DemandSimpleBeanImpl;
import org.jboss.test.kernel.dependency.support.SupplyPlainDependecySimpleBeanImpl;
import org.jboss.dependency.spi.ControllerState;

/**
 * Demand Dependency Test Case.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class DemandDependencyAnnotationTestCase extends DemandDependencyTestCase
{
   public static Test suite()
   {
      return suite(DemandDependencyAnnotationTestCase.class);
   }

   public DemandDependencyAnnotationTestCase(String name) throws Throwable
   {
      super(name);
   }

   protected ControllerState getWhenRequiredState()
   {
      return ControllerState.DESCRIBED;
   }

   protected void buildMetaData()
   {
      BeanMetaDataBuilder bmd1 = BeanMetaDataBuilderFactory.createBuilder("Name1", SupplyPlainDependecySimpleBeanImpl.class.getName());
      BeanMetaDataBuilder bmd2 = BeanMetaDataBuilderFactory.createBuilder("Name2", DemandSimpleBeanImpl.class.getName());
      setBeanMetaDatas(new BeanMetaData[] { bmd1.getBeanMetaData(), bmd2.getBeanMetaData() });
   }
}
