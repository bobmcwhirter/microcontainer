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
package org.jboss.test.kernel.config.test;

import junit.framework.Test;

import org.jboss.kernel.spi.registry.KernelRegistryEntryNotFoundException;
import org.jboss.test.kernel.config.support.SimpleBean;
import org.jboss.test.kernel.config.support.XMLUtil;

/**
 * Instantiation XML Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class InstantiateXMLTestCase extends InstantiateTestCase
{
   public static Test suite()
   {
      return suite(InstantiateXMLTestCase.class);
   }

   public InstantiateXMLTestCase(String name)
   {
      super(name, true);
   }

   public void testSimpleInstantiateFromBeanInfo() throws Throwable
   {
      // No XML equivalent
   }
      
   protected SimpleBean simpleInstantiateFromBeanMetaData() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected SimpleBean parameterInstantiateFromBeanMetaData() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected SimpleBean parameterInstantiateFromNull() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected SimpleBean parameterInstantiateWithTypeOverride() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }
      
   protected SimpleBean parameterInstantiateViaInterfaceWithTypeOverride() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected SimpleBean parameterInstantiateWithCollection() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected SimpleBean parameterInstantiateWithList() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected SimpleBean parameterInstantiateWithSet() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected SimpleBean parameterInstantiateWithArray() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected SimpleBean parameterInstantiateWithMap() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected SimpleBean parameterInstantiateWithProperties() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected Object valueInstantiateFromArray() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return util.getBean("Value");
   }

   protected Object valueInstantiateFromCollection() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return util.getBean("Value");
   }

   protected Object valueInstantiateFromList() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return util.getBean("Value");
   }

   protected Object valueInstantiateFromObject() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return util.getBean("Value");
   }

   protected Object valueInstantiateFromSet() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return util.getBean("Value");
   }

   protected Object valueInstantiateFromValue() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return util.getBean("Value");
   }

   public void testConstructorDoesNotExist() throws Throwable
   {
      XMLUtil util = bootstrapXML(false);
      try
      {
         util.validate();
         fail("Should not be valid!");
      }
      catch (IllegalStateException expected)
      {
      }

      try
      {
         util.getBean("SimpleBean");
      }
      catch (KernelRegistryEntryNotFoundException expected)
      {
      }
   }
}