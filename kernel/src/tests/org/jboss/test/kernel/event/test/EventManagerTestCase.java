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
package org.jboss.test.kernel.event.test;

import java.util.ArrayList;

import junit.framework.Test;

import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.event.KernelEvent;
import org.jboss.kernel.spi.event.KernelEventManager;
import org.jboss.kernel.spi.registry.KernelRegistry;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.kernel.spi.registry.KernelRegistryEntryNotFoundException;
import org.jboss.test.kernel.AbstractKernelTest;
import org.jboss.test.kernel.event.support.EventTestCaseSupport;
import org.jboss.test.kernel.event.support.TestEmitter;
import org.jboss.test.kernel.event.support.TestFilter;
import org.jboss.test.kernel.event.support.TestListener;

/**
 * Event Manager Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
@SuppressWarnings("deprecation")
public class EventManagerTestCase extends AbstractKernelTest
{
   public static Test suite()
   {
      return suite(EventManagerTestCase.class);
   }

   public EventManagerTestCase(String name)
   {
      super(name);
   }

   public void testSimpleFireEvent() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelRegistry registry = kernel.getRegistry();
      KernelEventManager eventManager = kernel.getEventManager();
      TestEmitter emitter = new TestEmitter();
      KernelRegistryEntry entry = makeEntry(emitter);
      registry.registerEntry("Emitter", entry);
      TestListener listener = new TestListener();
      eventManager.registerListener("Emitter", listener, null, null);
      emitter.testFire("testtype", null);
      ArrayList<KernelEvent> expected = new ArrayList<KernelEvent>();
      expected.add(EventTestCaseSupport.makeExpected(emitter, "testtype", 0, null, null));
      EventTestCaseSupport.check(listener, expected);
   }

   public void testDoubleFireEvent() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelRegistry registry = kernel.getRegistry();
      KernelEventManager eventManager = kernel.getEventManager();
      TestEmitter emitter = new TestEmitter();
      KernelRegistryEntry entry = makeEntry(emitter);
      registry.registerEntry("Emitter", entry);
      TestListener listener = new TestListener();
      eventManager.registerListener("Emitter", listener, null, null);
      emitter.testFire("testtype", null);
      emitter.testFire("testtype", null);
      ArrayList<KernelEvent> expected = new ArrayList<KernelEvent>();
      expected.add(EventTestCaseSupport.makeExpected(emitter, "testtype", 0, null, null));
      expected.add(EventTestCaseSupport.makeExpected(emitter, "testtype", 1, null, null));
      EventTestCaseSupport.check(listener, expected);
   }

   public void testFilter() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelRegistry registry = kernel.getRegistry();
      KernelEventManager eventManager = kernel.getEventManager();
      TestEmitter emitter = new TestEmitter();
      KernelRegistryEntry entry = makeEntry(emitter);
      registry.registerEntry("Emitter", entry);
      TestListener listener = new TestListener();
      TestFilter filter = new TestFilter();
      eventManager.registerListener("Emitter", listener, filter, null);
      emitter.testFire("testtype", null);
      filter.fireEvent = false;
      emitter.testFire("testtype", null);
      ArrayList<KernelEvent> expected = new ArrayList<KernelEvent>();
      expected.add(EventTestCaseSupport.makeExpected(emitter, "testtype", 0, null, null));
      EventTestCaseSupport.check(listener, expected);
      expected.add(EventTestCaseSupport.makeExpected(emitter, "testtype", 1, null, null));
      EventTestCaseSupport.check(filter, expected);
   }

   public void testHandback() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelRegistry registry = kernel.getRegistry();
      KernelEventManager eventManager = kernel.getEventManager();
      TestEmitter emitter = new TestEmitter();
      KernelRegistryEntry entry = makeEntry(emitter);
      registry.registerEntry("Emitter", entry);
      TestListener listener = new TestListener();
      TestFilter filter = new TestFilter();
      Object handback = new Object();
      eventManager.registerListener("Emitter", listener, filter, handback);
      emitter.testFire("testtype", null);
      ArrayList<KernelEvent> expected = new ArrayList<KernelEvent>();
      expected.add(EventTestCaseSupport.makeExpected(emitter, "testtype", 0, null, handback));
      EventTestCaseSupport.check(listener, expected);
      EventTestCaseSupport.check(filter, expected);
   }

   public void testDoesNotExist() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelEventManager eventManager = kernel.getEventManager();
      TestListener listener = new TestListener();
      try
      {
         eventManager.registerListener("doesnotexist", listener, null, null);
         fail("Should not be here");
      }
      catch (KernelRegistryEntryNotFoundException expected)
      {
      }
   }

   public void testNotAnEmitter() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelRegistry registry = kernel.getRegistry();
      KernelEventManager eventManager = kernel.getEventManager();
      Object notAnEmitter = new Object();
      KernelRegistryEntry entry = makeEntry(notAnEmitter);
      registry.registerEntry("notanemitter", entry);
      TestListener listener = new TestListener();
      try
      {
         eventManager.registerListener("notanemitter", listener, null, null);
         fail("Should not be here");
      }
      catch (ClassCastException expected)
      {
      }
   }
   
   protected void configureLoggingAfterBootstrap()
   {
      //enableTrace("org.jboss.kernel.plugins.event");
   }
}