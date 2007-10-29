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
import org.jboss.beans.metadata.plugins.AbstractSupplyMetaData;

/**
 * SupplyTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class SupplyTestCase extends AbstractXMLTest
{
   protected AbstractSupplyMetaData getSupply(String name) throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean(name);
      Set supplies = bean.getSupplies();
      assertNotNull(supplies);
      assertEquals(1, supplies.size());
      AbstractSupplyMetaData supply = (AbstractSupplyMetaData) supplies.iterator().next();
      assertNotNull(supply);
      return supply;
   }
   
   public void testSupply() throws Exception
   {
      AbstractSupplyMetaData supply = getSupply("Supply.xml");
      assertEquals("Supply", supply.getSupply());
   }

   public void testSupplyWithClass() throws Exception
   {
      AbstractSupplyMetaData supply = getSupply("SupplyWithClass.xml");
      assertEquals(123, supply.getSupply());
      assertEquals("java.lang.Integer", supply.getType());
   }

   public void testSupplyBadNoValue() throws Exception
   {
      try
      {
         unmarshalBean("SupplyBadNoValue.xml");
         fail("Should not be here");
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }
   
   public static Test suite()
   {
      return suite(SupplyTestCase.class);
   }

   public SupplyTestCase(String name)
   {
      super(name);
   }
}
