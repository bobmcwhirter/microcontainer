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

import java.net.URL;
import java.util.concurrent.CountDownLatch;

import junit.framework.Test;

import org.jboss.classloader.plugins.ClassLoaderUtils;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.test.support.MockClassLoaderPolicy;
import org.jboss.test.classloader.AbstractClassLoaderTest;
import org.jboss.test.classloader.old.support.Derived;
import org.jboss.test.classloader.old.support.Support;

/**
 * CircularityErrorUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Simone.Bordet@hp.com
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.1 $
 */
public class CircularityErrorUnitTestCase extends AbstractClassLoaderTest
{
   private CountDownLatch waiting = new CountDownLatch(1);
   private Throwable sawError;
   
   public static Test suite()
   {
      return suite(CircularityErrorUnitTestCase.class);
   }
   
   public CircularityErrorUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testClassCircularityError() throws Exception
   {
      // The scenario is this one:
      // Thread1 asks classloader1 to load class Derived
      // Thread2 triggers a loadClassInternal for classloader1 to load class Base
      // Thread2 is put in sleep by the ULR since we are loading Derived
      // Thread1 triggers a loadClassInternal for classloader1 to load class Base
      // Thread1 throws ClassCircularityError

      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      final ClassLoader cl = system.registerClassLoaderPolicy(new TestClassLoaderPolicy());

      Class<?> cls = assertLoadClass(Support.class, cl);

      Thread thread1 = new Thread(new Runnable()
      {
         public void run()
         {
            // Be sure thread2 is waiting
            try 
            {
               Thread.sleep(1000);
            }
            catch (InterruptedException x) 
            {
            }
            

            try
            {
               getLog().debug("Thread " + Thread.currentThread() + " loading...");
               assertLoadClass(Derived.class, cl);
               getLog().debug("Thread " + Thread.currentThread() + " loading done !");
            }
            catch (Throwable t)
            {
               sawError = t;
            }
         }
      }, "CircularityErrorThread");
      thread1.start();

      getLog().debug("Thread " + Thread.currentThread() + " waiting...");
      waiting.await();
      getLog().debug("Thread " + Thread.currentThread() + " woken up !");

      // Ask this thread to trigger a loadClassInternal directly; the thread will be put to sleep
      // but the JVM has already registered the fact that
      // it wants to load the class, in this case class Base
      cls.newInstance();
      thread1.join();

      // The ClassCircularityError thrown should allow the call above to complete
      if (sawError != null)
         failure("Unexpected error", sawError);
   }

   public class TestClassLoaderPolicy extends MockClassLoaderPolicy
   {
      public TestClassLoaderPolicy()
      {
         setPaths(Support.class);
      }

      @Override
      public URL getResource(String name)
      {
         getLog().debug(Thread.currentThread() + " is now asked to load class: " + name);

         if (name.equals(ClassLoaderUtils.classNameToPath(Derived.class.getName())))
         {
            waiting.countDown();

            // Wait to trigger ClassCircularityError
            // Do not release the lock on the classloader
            try
            {
               getLog().debug("Loading " + name + ", waiting...");
               Thread.sleep(2000);
               getLog().debug("Loading " + name + " end wait");
            }
            catch (InterruptedException x)
            {
               getLog().debug("Sleep was interrupted", x);
            }
         }

         return super.getResource(name);
      }
   }

}
