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
package org.jboss.test.classloader.delegate.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import junit.framework.TestSuite;

import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.DelegateLoader;
import org.jboss.classloader.spi.filter.FilteredDelegateLoader;
import org.jboss.test.classloader.AbstractClassLoaderTest;
import org.jboss.test.classloader.TestThread;
import org.jboss.test.classloader.delegate.support.a.TestA1;
import org.jboss.test.classloader.delegate.support.a.TestAbstractFactory;
import org.jboss.test.classloader.delegate.support.a.TestSleep;
import org.jboss.test.classloader.delegate.support.b.TestB1;
import org.jboss.test.classloader.delegate.support.b.TestFactoryImplementation;
import org.jboss.test.classloader.support.MockClassLoaderPolicy;

/**
 * ModifiedBootstrapUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DelegateUnitTestCase extends AbstractClassLoaderTest
{
   public static TestSuite suite()
   {
      return suite(DelegateUnitTestCase.class);
   }

   public DelegateUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testNoDelegateAToB() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy pb = new MockClassLoaderPolicy("B");
      pb.setPaths(TestB1.class);
      ClassLoader b = system.registerClassLoaderPolicy(pb);

      MockClassLoaderPolicy pa = new MockClassLoaderPolicy("A");
      pa.setPaths(TestA1.class);
      ClassLoader a = system.registerClassLoaderPolicy(pa);
      
      assertLoadClass(TestB1.class, b, false);
      assertLoadClassFail(TestB1.class, a);
   }
   
   public void testDelegateAToB() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy pb = new MockClassLoaderPolicy("B");
      pb.setPaths(TestB1.class);
      ClassLoader b = system.registerClassLoaderPolicy(pb);

      MockClassLoaderPolicy pa = new MockClassLoaderPolicy("A");
      pa.setPaths(TestA1.class);
      pa.setDelegates(Collections.singletonList(new FilteredDelegateLoader(pb)));
      ClassLoader a = system.registerClassLoaderPolicy(pa);
      
      Class<?> fromB = assertLoadClass(TestB1.class, b, false);
      Class<?> fromA = assertLoadClass(TestB1.class, a, b, false);
      
      assertClassEquality(fromB, fromA);
   }
   
   public void testAbstractFactoryObviousWay() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      
      MockClassLoaderPolicy pb = new MockClassLoaderPolicy("B");
      pb.setPaths(TestB1.class);

      MockClassLoaderPolicy pa = new MockClassLoaderPolicy("A");
      pa.setPaths(TestA1.class);

      List<DelegateLoader> delegates = new ArrayList<DelegateLoader>();
      delegates.add(new FilteredDelegateLoader(pa));
      delegates.add(new FilteredDelegateLoader(pb));
      pa.setDelegates(delegates);
      pb.setDelegates(delegates);

      ClassLoader a = system.registerClassLoaderPolicy(pa);
      ClassLoader b = system.registerClassLoaderPolicy(pb);
      
      Class<?> testAbstractFactoryClass = assertLoadClass(TestAbstractFactory.class, a);
      Method method = testAbstractFactoryClass.getMethod("getInstance", null);
      Object instance = method.invoke(null, null);
      Class<?> testFactoryImplementationClass = assertLoadClass(TestFactoryImplementation.class, b);
      assertClassEquality(testFactoryImplementationClass, instance.getClass());
   }
   
   public void testAbstractFactoryWrongWay() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      
      MockClassLoaderPolicy pb = new MockClassLoaderPolicy("B");
      pb.setPaths(TestB1.class);

      MockClassLoaderPolicy pa = new MockClassLoaderPolicy("A");
      pa.setPaths(TestA1.class);

      List<DelegateLoader> delegates = new ArrayList<DelegateLoader>();
      delegates.add(new FilteredDelegateLoader(pa));
      delegates.add(new FilteredDelegateLoader(pb));
      pa.setDelegates(delegates);
      pb.setDelegates(delegates);

      ClassLoader a = system.registerClassLoaderPolicy(pa);
      ClassLoader b = system.registerClassLoaderPolicy(pb);
      
      Class<?> testAbstractFactoryClass = assertLoadClass(TestAbstractFactory.class, b, a);
      Method method = testAbstractFactoryClass.getMethod("getInstance", null);
      Object instance = method.invoke(null, null);
      Class<?> testFactoryImplementationClass = assertLoadClass(TestFactoryImplementation.class, a, b);
      assertClassEquality(testFactoryImplementationClass, instance.getClass());
   }
   
   public void testAbstractFactoryConcurrent() throws Exception
   {
      for (int i = 0; i < 10; ++i)
      {
         log.debug("Attempt: " + i);
         ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
         
         MockClassLoaderPolicy pb = new MockClassLoaderPolicy("B");
         pb.setPaths(TestB1.class);

         MockClassLoaderPolicy pa = new MockClassLoaderPolicy("A");
         pa.setPaths(TestA1.class);

         List<DelegateLoader> delegates = new ArrayList<DelegateLoader>();
         delegates.add(new FilteredDelegateLoader(pa));
         delegates.add(new FilteredDelegateLoader(pb));
         pa.setDelegates(delegates);
         pb.setDelegates(delegates);

         final ClassLoader a = system.registerClassLoaderPolicy(pa);
         final ClassLoader b = system.registerClassLoaderPolicy(pb);
         
         Class<?> testSleep = assertLoadClass(TestSleep.class, a);
         Field field = testSleep.getField("sleep");
         field.set(null, new Long(1));
         
         final CountDownLatch startALatch = new CountDownLatch(1);
         final CountDownLatch startBLatch = new CountDownLatch(1);
         
         TestThread threadA = new TestThread("A")
         {
            public void run()
            {
               try
               {
                  startBLatch.await();
               }
               catch (InterruptedException ignored)
               {
               }
               startALatch.countDown();
               Class<?> testAbstractFactoryClass = assertLoadClass(TestAbstractFactory.class, a);
               try
               {
                  Method method = testAbstractFactoryClass.getMethod("getInstance", null);
                  method.invoke(null, null);
               }
               catch (Exception e)
               {
                  throw new Error("Error", e);
               }
            }
         };
         
         TestThread threadB = new TestThread("B")
         {
            public void run()
            {
               startBLatch.countDown();
               try
               {
                  startALatch.await();
               }
               catch (InterruptedException ignored)
               {
               }
               assertLoadClass(TestFactoryImplementation.class, b);
            }
         };
         
         threadA.start();
         threadB.start();
         
         threadA.doJoin();
         threadB.doJoin();
      }
   }
}
