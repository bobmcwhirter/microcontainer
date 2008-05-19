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
package org.jboss.test.kernel.registry.test;

import junit.framework.Test;

import org.jboss.kernel.spi.registry.KernelRegistry;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.kernel.spi.registry.KernelRegistryEntryNotFoundException;
import org.jboss.test.kernel.AbstractKernelTest;
import org.jboss.test.kernel.registry.support.SimpleRegistryFactory;

/**
 * Registry Factory Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class RegistryFactoryTestCase extends AbstractKernelTest
{
   public static Test suite()
   {
      return suite(RegistryFactoryTestCase.class);
   }

   public RegistryFactoryTestCase(String name)
   {
      super(name);
   }

   @SuppressWarnings("deprecation")
   public void testAddRegistryFactory() throws Throwable
   {
      KernelRegistry registry = bootstrap().getRegistry();

      SimpleRegistryFactory factory = new SimpleRegistryFactory();
      Object testObject = new Object();
      factory.entries.put("Name1", testObject);
      KernelRegistryEntry factoryEntry = makeEntry(factory);
      registry.registerEntry("Factory", factoryEntry);
      
      KernelRegistryEntry result = registry.getEntry("Name1");
      assertNotNull(result);
      Object target = result.getTarget();
      assertNotNull(target);
      assertTrue("Should be the same object", testObject == target);
   }

   public void testRemoveRegistryFactory() throws Throwable
   {
      KernelRegistry registry = bootstrap().getRegistry();

      SimpleRegistryFactory factory = new SimpleRegistryFactory();
      Object testObject = new Object();
      factory.entries.put("Name1", testObject);
      KernelRegistryEntry factoryEntry = makeEntry(factory);
      registry.registerEntry("Factory", factoryEntry);
      
      registry.unregisterEntry("Factory");
      
      try
      {
         registry.getEntry("Name1");
         fail("Should not be here");
      }
      catch (KernelRegistryEntryNotFoundException expected)
      {
      }
   }

   public void testAddRemoveAddRegistryFactory() throws Throwable
   {
      KernelRegistry registry = bootstrap().getRegistry();

      SimpleRegistryFactory factory = new SimpleRegistryFactory();
      Object testObject = new Object();
      factory.entries.put("Name1", testObject);
      KernelRegistryEntry factoryEntry = makeEntry(factory);
      registry.registerEntry("Factory", factoryEntry);
      registry.unregisterEntry("Factory");
      registry.registerEntry("Factory", factoryEntry);
      
      KernelRegistryEntry result = registry.getEntry("Name1");
      assertNotNull(result);
      Object target = result.getTarget();
      assertNotNull(target);
      assertTrue("Should be the same object", testObject == target);
   }

   public void testDuplicateRegistryFactory() throws Throwable
   {
      KernelRegistry registry = bootstrap().getRegistry();

      SimpleRegistryFactory factory1 = new SimpleRegistryFactory();
      Object testObject1 = new Object();
      factory1.entries.put("Name1", testObject1);
      KernelRegistryEntry factoryEntry1 = makeEntry(factory1);
      registry.registerEntry("Factory1", factoryEntry1);

      SimpleRegistryFactory factory2 = new SimpleRegistryFactory();
      Object testObject2 = new Object();
      Object testObject3 = new Object();
      factory2.entries.put("Name1", testObject2);
      factory2.entries.put("Name2", testObject3);
      KernelRegistryEntry factoryEntry2 = makeEntry(factory2);
      registry.registerEntry("Factory2", factoryEntry2);
      
      KernelRegistryEntry result = registry.getEntry("Name1");
      assertNotNull(result);
      Object target = result.getTarget();
      assertNotNull(target);
      assertTrue("Should be the object from the first factory", testObject1 == target);
      
      result = registry.getEntry("Name2");
      assertNotNull(result);
      target = result.getTarget();
      assertNotNull(target);
      assertTrue("Should be the object from the second factory", testObject3 == target);
      
      registry.unregisterEntry("Factory1");
      
      result = registry.getEntry("Name1");
      assertNotNull(result);
      target = result.getTarget();
      assertNotNull(target);
      assertTrue("Should be the object from the second factory", testObject2 == target);
      
      registry.registerEntry("Factory1", factoryEntry1);
      
      result = registry.getEntry("Name1");
      assertNotNull(result);
      target = result.getTarget();
      assertNotNull(target);
      assertTrue("Should be the object from the second factory", testObject2 == target);
      
      registry.unregisterEntry("Factory1");
      registry.unregisterEntry("Factory2");
      
      try
      {
         registry.getEntry("Name1");
         fail("Should not be here");
      }
      catch (KernelRegistryEntryNotFoundException expected)
      {
      }
   }

   protected void configureLoggingAfterBootstrap()
   {
      //enableTrace("org.jboss.kernel.plugins.registry");
   }
}