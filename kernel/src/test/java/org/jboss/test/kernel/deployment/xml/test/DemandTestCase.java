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
package org.jboss.test.kernel.deployment.xml.test;

import java.util.Set;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractDemandMetaData;
import org.jboss.beans.metadata.spi.DemandMetaData;
import org.jboss.dependency.spi.ControllerState;

/**
 * Demand TestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class DemandTestCase extends AbstractXMLTest
{
   protected AbstractDemandMetaData getDemand(String name) throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean(name);
      Set<DemandMetaData> demands = bean.getDemands();
      assertNotNull(demands);
      assertEquals(1, demands.size());
      AbstractDemandMetaData demand = (AbstractDemandMetaData) demands.iterator().next();
      assertNotNull(demand);
      return demand;
   }
   
   public void testDemand() throws Exception
   {
      AbstractDemandMetaData demand = getDemand("Demand.xml");
      assertEquals("Demand", demand.getDemand());
      assertEquals(ControllerState.DESCRIBED, demand.getWhenRequired());
   }
   
   public void testDemandWithWhenRequired() throws Exception
   {
      AbstractDemandMetaData demand = getDemand("DemandWithWhenRequired.xml");
      assertEquals("Demand", demand.getDemand());
      assertEquals(ControllerState.CONFIGURED, demand.getWhenRequired());
   }

   public void testDemandWithTransformer() throws Exception
   {
      AbstractDemandMetaData demand = getDemand("DemandWithTransformer.xml");
      assertEquals("Demand", demand.getDemand());
      assertEquals("default", demand.getTransformer());
   }

   public static Test suite()
   {
      return suite(DemandTestCase.class);
   }

   public DemandTestCase(String name)
   {
      super(name);
   }

   protected DemandTestCase(String name, boolean useClone)
   {
      super(name, useClone);
   }
}
