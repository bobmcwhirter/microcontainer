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

import org.jboss.kernel.spi.event.KernelEvent;
import org.jboss.test.kernel.AbstractKernelTest;
import org.jboss.test.kernel.event.support.EventTestCaseSupport;
import org.jboss.test.kernel.event.support.TestEmitter;
import org.jboss.test.kernel.event.support.TestFilter;
import org.jboss.test.kernel.event.support.TestListener;

/**
 * Emitter Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class EmitterTestCase extends AbstractKernelTest
{
   public static Test suite()
   {
      return suite(EmitterTestCase.class);
   }
   
   public EmitterTestCase(String name)
   {
      super(name);
   }

   public void testSimpleFireEvent() throws Throwable
   {
      TestEmitter emitter = new TestEmitter();
      TestListener listener = new TestListener();
      emitter.registerListener(listener, null, null);
      emitter.testFire("testtype", null);
      ArrayList<KernelEvent> expected = new ArrayList<KernelEvent>();
      expected.add(EventTestCaseSupport.makeExpected(emitter, "testtype", 0, null, null));
      EventTestCaseSupport.check(listener, expected);
   }

   public void testDoubleFireEvent() throws Throwable
   {
      TestEmitter emitter = new TestEmitter();
      TestListener listener = new TestListener();
      emitter.registerListener(listener, null, null);
      emitter.testFire("testtype", null);
      emitter.testFire("testtype", null);
      ArrayList<KernelEvent> expected = new ArrayList<KernelEvent>();
      expected.add(EventTestCaseSupport.makeExpected(emitter, "testtype", 0, null, null));
      expected.add(EventTestCaseSupport.makeExpected(emitter, "testtype", 1, null, null));
      EventTestCaseSupport.check(listener, expected);
   }

   public void testFilter() throws Throwable
   {
      TestEmitter emitter = new TestEmitter();
      TestListener listener = new TestListener();
      TestFilter filter = new TestFilter();
      emitter.registerListener(listener, filter, null);
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
      TestEmitter emitter = new TestEmitter();
      TestListener listener = new TestListener();
      TestFilter filter = new TestFilter();
      Object handback = new Object();
      emitter.registerListener(listener, filter, handback);
      emitter.testFire("testtype", null);
      ArrayList<KernelEvent> expected = new ArrayList<KernelEvent>();
      expected.add(EventTestCaseSupport.makeExpected(emitter, "testtype", 0, null, handback));
      EventTestCaseSupport.check(listener, expected);
      EventTestCaseSupport.check(filter, expected);
   }
   
   protected void setUp() throws Exception
   {
      super.setUp();
      //enableTrace("org.jboss.kernel.plugins.event");
   }
}