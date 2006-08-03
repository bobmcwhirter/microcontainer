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
package org.jboss.test.kernel.event.support;

import java.util.ArrayList;

import junit.framework.AssertionFailedError;

import org.jboss.kernel.spi.event.KernelEvent;
import org.jboss.logging.Logger;

/**
 * Event Test Case Support.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class EventTestCaseSupport 
{
   private static final Logger log = Logger.getLogger(EventTestCaseSupport.class);
   
   public static void check(TestListener listener, ArrayList<KernelEvent> expected) throws Exception
   {
      ArrayList<KernelEvent> received = listener.events;
      ArrayList<KernelEvent> actual = new ArrayList<KernelEvent>(received);
      ArrayList<KernelEvent> expect = new ArrayList<KernelEvent>(expected);
      
      log.debug("listener expect=" + expect);
      log.debug("listener actual=" + actual);
      
      actual.removeAll(expected);
      assertTrue("Didn't expect events: " + actual + " expected=" + expected, actual.isEmpty());
      
      expect.removeAll(received);
      assertTrue("Expected events: " + expect + " received=" + received, expect.isEmpty());
   }
   
   public static void check(TestFilter filter, ArrayList<KernelEvent> expected) throws Exception
   {
      ArrayList<KernelEvent> filtered = filter.events;
      ArrayList<KernelEvent> actual = new ArrayList<KernelEvent>(filtered);
      ArrayList<KernelEvent> expect = new ArrayList<KernelEvent>(expected);
      
      log.debug("filter expect=" + expect);
      log.debug("filter actual=" + actual);
      
      actual.removeAll(expected);
      assertTrue("Didn't expect to filter events: " + actual + " expected=" + expected, actual.isEmpty());
      
      expect.removeAll(filtered);
      assertTrue("Expected to filter events: " + expect + " filtered=" + filtered, expect.isEmpty());
   }

   public static TestEvent makeExpected(Object source, String type, long sequence, Object context, Object handback)
   {
      return new TestEvent(source, type, sequence, context, handback);
   }

   public static void assertTrue(String text, boolean assertion)
   {
      if (assertion == false)
         throw new AssertionFailedError(text);
   }
}