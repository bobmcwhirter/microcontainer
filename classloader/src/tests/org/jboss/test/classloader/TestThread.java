/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.test.classloader;

import java.lang.Thread.UncaughtExceptionHandler;

import junit.framework.AssertionFailedError;

import org.jboss.logging.Logger;

/**
 * TestThread.
 * 
 * TODO move to the test project
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class TestThread extends Thread implements UncaughtExceptionHandler
{
   private Logger log = Logger.getLogger(TestThread.class);
   
   private Throwable t;

   public TestThread()
   {
      setUncaughtExceptionHandler(this);
   }

   public TestThread(String name)
   {
      super(name);
      setUncaughtExceptionHandler(this);
   }

   public void uncaughtException(Thread t, Throwable e)
   {
      log.error("Error in thread " + t, e);
      this.t = e;
   }
   
   public void doJoin() throws InterruptedException
   {
      super.join();
      if (t != null)
      {
         if (t instanceof AssertionFailedError)
            throw (AssertionFailedError) t;
         AssertionFailedError e = new AssertionFailedError("Error in thread" + t);
         e.initCause(t);
         throw e;
      }
   }
}
