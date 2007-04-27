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
package org.jboss.test.classloader.old.test;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import junit.framework.TestSuite;

import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.test.classloader.AbstractClassLoaderTest;
import org.jboss.test.classloader.old.support.Support;
import org.jboss.test.classloader.support.MockClassLoaderPolicy;

/**
 * ConcurrentLoaderUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.1 $
 */
public class ConcurrentLoaderUnitTestCase extends AbstractClassLoaderTest
{
   public Object lock = new Object ();

   public final static int MAX_CLASSES = 10;
   public final static int NUMBER_OF_LOADING = 10;
   public final static int NUMBER_OF_THREADS = 20;
   private HashSet<Class> classes = new HashSet<Class>();
   private Vector<Loader> loaders = new Vector<Loader>();
   private Timer newInstanceTimer;
   private int doneCount;
   private ClassLoader cl;

   public static TestSuite suite()
   {
      return suite(ConcurrentLoaderUnitTestCase.class);
   }

   public ConcurrentLoaderUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testConcurrentLoad() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      
      MockClassLoaderPolicy policy = new MockClassLoaderPolicy();
      policy.setPaths(Support.class);
      cl = system.registerClassLoaderPolicy(policy);
      
      log.debug("Creating " + NUMBER_OF_THREADS + " threads...");
      newInstanceTimer = new Timer(true);
      newInstanceTimer.scheduleAtFixedRate(new NewInstanceTask(), 0, 100);
      doneCount = 0;
      for (int t = 0; t < NUMBER_OF_THREADS; t ++)
      {
         Loader loader = new Loader (t);
         loader.start();
         loaders.add(loader);
      }
      log.debug("All threads created");
      Thread.sleep(2000);

      synchronized(lock)
      {
         lock.notifyAll ();
      }
      
      log.debug("Unlocked all Loader threads");
      
      synchronized(lock)
      {
         while( doneCount < NUMBER_OF_THREADS )
         {
            lock.wait();
         }
         log.debug("Loader doneCount=" + doneCount);
      }
      log.debug("All Loaders are done");
      newInstanceTimer.cancel();
      
      for (Loader loader : loaders)
      {
         if (loader.error != null)
            failure("Error in loader: ", loader.error);
      }
   }

   class NewInstanceTask extends TimerTask
   {
      public void run()
      {
         int size = classes.size();
         Class[] theClasses = new Class[size];
         classes.toArray(theClasses);
         log.debug("NewInstanceTask, creating " + size + " instances");
         for (int c = 0; c < theClasses.length; ++c)
         {
            try
            {
               Class clazz = theClasses[c];
               Object obj = clazz.newInstance();
               log.debug("Created instance=" + obj);
            }
            catch(Throwable t)
            {
               log.error("Error instantiating class " + theClasses[c], t);
            }
         }
      }
   }

   class Loader extends Thread
   {
      int classid = 0;
      Throwable error;

      public Loader (int classid)
      {
         super("ConcurrentLoader - Thread #" + classid);
         this.classid = classid;
      }

      public void run ()
      {
         int modId = classid % MAX_CLASSES;
         String className = Support.class.getPackage().getName() + ".AnyClass" + modId;

         synchronized(lock)
         {
            try
            {
               log.debug("Thread ready: " + classid);
               lock.wait ();
            }
            catch (Exception e)
            {
               log.error("Error during wait", e);
            }
         }
         log.debug("loading class... " + className);
         for (int i = 0; i < NUMBER_OF_LOADING; ++i)
         {
            log.debug("loading class with id " + classid + " for the " + i + "th time");
            try
            {
               log.debug("before load...");
               long sleep = (long) (1000 * Math.random());
               Thread.sleep(sleep);
               Class clazz = cl.loadClass (className);
               classes.add(clazz);
               Object obj = clazz.newInstance();
               log.debug("Class " + className + " loaded, obj=" + obj);
            }
            catch (Throwable e)
            {
               log.debug("Failed to load class and create instance", e);
               error = e;
            }
         }
         log.debug("...Done loading classes. " + classid);
         synchronized( lock )
         {
            doneCount++;
            lock.notify();
         }
      }
   }
}
