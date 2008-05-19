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
import org.jboss.kernel.spi.registry.KernelRegistryEntryAlreadyRegisteredException;
import org.jboss.kernel.spi.registry.KernelRegistryEntryNotFoundException;
import org.jboss.test.kernel.AbstractKernelTest;

/**
 * Registry Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
@SuppressWarnings("deprecation")
public class RegistryTestCase extends AbstractKernelTest
{
   public static Test suite()
   {
      return suite(RegistryTestCase.class);
   }

   public RegistryTestCase(String name)
   {
      super(name);
   }

   public void testGotRegistry() throws Throwable
   {
      KernelRegistry registry = bootstrap().getRegistry();
      assertNotNull(registry);
   }

   public void testRegister() throws Throwable
   {
      KernelRegistry registry = bootstrap().getRegistry();
      KernelRegistryEntry test1 = makeEntry(new Object());
      registry.registerEntry("Name1", test1);
      assertEqualsRegistryTarget(test1, registry.getEntry("Name1"));
   }

   public void testDuplicateRegisterError() throws Throwable
   {
      KernelRegistry registry = bootstrap().getRegistry();
      KernelRegistryEntry test1 = makeEntry(new Object());
      registry.registerEntry("Name1", test1);
      KernelRegistryEntry test2 = makeEntry(new Object());
      try
      {
         registry.registerEntry("Name1", test2);
         fail("Expected duplicate registration error");
      }
      catch (KernelRegistryEntryAlreadyRegisteredException e)
      {
         // expected
      }
      assertEqualsRegistryTarget(test1, registry.getEntry("Name1"));
   }

   public void testRegisterTwice() throws Throwable
   {
      KernelRegistry registry = bootstrap().getRegistry();
      Object object = new Object();
      KernelRegistryEntry test1 = makeEntry(object);
      registry.registerEntry("Name1", test1);
      KernelRegistryEntry test2 = makeEntry(object);
      registry.registerEntry("Name2", test2);
      assertEqualsRegistryTarget(registry.getEntry("Name1"), registry.getEntry("Name2"));
   }

   public void testUnregister() throws Throwable
   {
      KernelRegistry registry = bootstrap().getRegistry();
      try
      {
         registry.getEntry("Name1");
         fail("Expected not registered error");
      }
      catch (KernelRegistryEntryNotFoundException e)
      {
         // expected
      }
      try
      {
         registry.unregisterEntry("Name1");
         fail("Expected not registered error");
      }
      catch (KernelRegistryEntryNotFoundException e)
      {
         // expected
      }
      KernelRegistryEntry test1 = makeEntry(new Object());
      registry.registerEntry("Name1", test1);
      registry.unregisterEntry("Name1");
      try
      {
         registry.getEntry("Name1");
         fail("Expected not registered error");
      }
      catch (KernelRegistryEntryNotFoundException e)
      {
         // expected
      }
      try
      {
         registry.unregisterEntry("Name1");
         fail("Expected not registered error");
      }
      catch (KernelRegistryEntryNotFoundException e)
      {
         // expected
      }
      registry.registerEntry("Name1", test1);
      assertEqualsRegistryTarget(test1, registry.getEntry("Name1"));
   }

   public void testNull() throws Throwable
   {
      KernelRegistry registry = bootstrap().getRegistry();
      try
      {
         registry.getEntry(null);
         fail("Expected null name error");
      }
      catch (IllegalArgumentException e)
      {
         // expected
      }
      try
      {
         KernelRegistryEntry test1 = makeEntry(new Object());
         registry.registerEntry(null, test1);
         fail("Expected null name error");
      }
      catch (IllegalArgumentException e)
      {
         // expected
      }
      try
      {
         registry.registerEntry("Name1", null);
         fail("Expected null object error");
      }
      catch (IllegalArgumentException e)
      {
         // expected
      }
      try
      {
         registry.unregisterEntry(null);
         fail("Expected null name error");
      }
      catch (IllegalArgumentException e)
      {
         // expected
      }
   }

   protected void configureLoggingAfterBootstrap()
   {
      //enableTrace("org.jboss.kernel.plugins.registry");
   }
}