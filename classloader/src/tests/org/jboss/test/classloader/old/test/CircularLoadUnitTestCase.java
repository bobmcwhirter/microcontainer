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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import junit.framework.Test;

import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.test.support.MockClassLoaderPolicy;
import org.jboss.test.classloader.AbstractClassLoaderTestWithSecurity;
import org.jboss.test.classloader.old.support.Base;
import org.jboss.test.classloader.old.support.Class0;
import org.jboss.test.classloader.old.support.Class1;
import org.jboss.test.classloader.old.support.Class2;
import org.jboss.test.classloader.old.support.Derived;
import org.jboss.test.classloader.old.support.LoginInfo;
import org.jboss.test.classloader.old.support.Support;
import org.jboss.test.classloader.old.support.UserOfBase;
import org.jboss.test.classloader.old.support.UserOfLoginInfo;
import org.jboss.test.classloader.old.support.UserOfUsrMgr;
import org.jboss.test.classloader.old.support.UsrMgr;

/**
 * CircularLoadUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Simone.Bordet@hp.com
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.1 $
 */
public class CircularLoadUnitTestCase extends AbstractClassLoaderTestWithSecurity
{
   public CircularLoadUnitTestCase(String name)
   {
      super(name);
   }
   
   public static Test suite()
   {
      return suite(CircularLoadUnitTestCase.class);
   }

   public void testLinkageError() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy le0 = createMockClassLoaderPolicy("le0");
      le0.setPathsAndPackageNames(Base.class);
      le0.setImportAll(true);
      le0.setIncluded(Base.class, UserOfBase.class);
      ClassLoader cl0 = system.registerClassLoaderPolicy(le0);

      MockClassLoaderPolicy le1 = createMockClassLoaderPolicy("le1");
      le1.setPathsAndPackageNames(Base.class);
      le1.setImportAll(true);
      le1.setIncluded(Base.class, Support.class);
      ClassLoader cl1 = system.registerClassLoaderPolicy(le1);

      MockClassLoaderPolicy all = createMockClassLoaderPolicy("all");
      all.setPackageNames(Base.class);
      all.setImportAll(true);
      ClassLoader cl2 = system.registerClassLoaderPolicy(all);
      
      try
      {
         // Load Base
         assertLoadClass(Base.class, cl2, cl0);

         // Load and create an instance of the UserOfBase class
         Class<?> userOfBaseClass = assertLoadClass(UserOfBase.class, cl0);
         Constructor<?> ctor0 = userOfBaseClass.getConstructor((Class[]) null);
         Object userOfBase = ctor0.newInstance((Object[]) null);

         // Load and create an instance of the Support class
         Class<?> supportClass = assertLoadClass(Support.class, cl1);
         Constructor<?> ctor1 = supportClass.getConstructor((Class[]) null);
         Object support = ctor1.newInstance((Object[]) null);

         // Now invoke UserOfBase.testBase(Support)
         Class<?>[] sig = { supportClass };
         Method testBase = userOfBaseClass.getMethod("testBase", sig);
         getLog().info(testBase.toString());
         Object[] args = { support };
         testBase.invoke(userOfBase, args);
      }
      catch(Throwable t)
      {
         failure("Failed", t);
      }
   }
   public void testPackageProtected() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy mock = createMockClassLoaderPolicy();
      mock.setPathsAndPackageNames(Base.class);
      mock.setIncluded(LoginInfo.class, UsrMgr.class, UserOfUsrMgr.class, UserOfLoginInfo.class);
      ClassLoader cl = system.registerClassLoaderPolicy(mock);

      getLog().info("Begin testPackageProtected");

      try
      {
         // Load and create an instance of the UserOfLoginInfo class
         Class<?> c0 = assertLoadClass(UserOfLoginInfo.class, cl);
         Class<?>[] ctorsig0 = {String.class, String.class};
         Constructor<?> ctor0 = c0.getConstructor(ctorsig0);
         Object[] args0 = {"jduke", "theduke"};
         ctor0.newInstance(args0);

         // Load and create an instance of the UserOfUsrMgr class
         Class<?> c1 = assertLoadClass(UserOfUsrMgr.class, cl);
         Class<?>[] ctorsig1 = {String.class, String.class};
         Constructor<?> ctor1 = c1.getConstructor(ctorsig1);
         Object[] args1 = {"jduke", "theduke"};
         Object o1 = ctor1.newInstance(args1);

         // Now invoke UserOfUsrMgr.changePassword(char[] password)
         char[] password = "theduke2".toCharArray();
         Class<?>[] sig = {password.getClass()};
         Method changePassword = c1.getMethod("changePassword", sig);
         getLog().info(changePassword.toString());
         Object[] args = {password};
         changePassword.invoke(o1, args);
      }
      catch(Exception e)
      {
         failure("Failed", e);
      }
   }

   public void testDuplicateClass() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy any0 = createMockClassLoaderPolicy("any0");
      any0.setPathsAndPackageNames(Support.class);
      any0.setImportAll(true);
      any0.setIncluded(Base.class, Class0.class, Class2.class);
      ClassLoader cl0 = system.registerClassLoaderPolicy(any0);
      
      MockClassLoaderPolicy any1 = createMockClassLoaderPolicy("any1");
      any1.setPathsAndPackageNames(Support.class);
      any1.setImportAll(true);
      any1.setIncluded(Class0.class, Class2.class);
      ClassLoader cl1 = system.registerClassLoaderPolicy(any1);

      assertLoadClass(Class0.class, cl0);
      assertLoadClass(Class2.class, cl1, cl0);
      Class<?> base = assertLoadClass(Base.class, cl0);
      Method run = base.getMethod("run", (Class[]) null);
      run.invoke(null, (Object[]) null);
   }
   
   public void testUCLOwner() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy j0 = createMockClassLoaderPolicy("j0");
      j0.setPathsAndPackageNames(Support.class);
      j0.setImportAll(true);
      j0.setIncluded(Class0.class);
      ClassLoader cl0 = system.registerClassLoaderPolicy(j0);

      MockClassLoaderPolicy j1 = createMockClassLoaderPolicy("j1");
      j1.setPathsAndPackageNames(Support.class);
      j1.setImportAll(true);
      j1.setIncluded(Class2.class);
      ClassLoader cl1 = system.registerClassLoaderPolicy(j1);

      // Request a class that does not exist
      LoadThread t0 = new LoadThread(Support.class, cl0, "testUCLOwner.T0", true);
      t0.start();
      // Join the thread
      t0.join(5000);
      assertNull("T0 failed as no class should have been found, loadedClass="+t0.loadedClass, t0.loadedClass);
      assertNotNull("T0 failed as no class should have been found, loadedClass="+t0.loadedClass, t0.loadError);
      checkThrowable(ClassNotFoundException.class, t0.loadError);

      LoadThread t1 = new LoadThread(Class2.class, cl1, "testUCLOwner.T1");
      t1.start();
      // Join the thread
      t1.join(5000);
      if (t1.loadError != null)
         failure("T1 failed to load Class2" , t1.loadError);
      assertNotNull("T1 failed to load Class2", t1.loadedClass);
   }
   
   public void testMissingSuperClass() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy j0 = createMockClassLoaderPolicy("j0");
      j0.setPathsAndPackageNames(Support.class);
      j0.setImportAll(true);
      j0.setIncluded(Class0.class);
      ClassLoader cl0 = system.registerClassLoaderPolicy(j0);

      MockClassLoaderPolicy j3 = createMockClassLoaderPolicy("j3");
      j3.setPathsAndPackageNames(Support.class);
      j3.setImportAll(true);
      j3.setIncluded(Derived.class);
      system.registerClassLoaderPolicy(j3);

      LoadThread t0 = new LoadThread(Derived.class, cl0, "testMissingSuperClass.T0");
      t0.start();
      // Join the thread
      t0.join(5000);
      assertNull("T0 failed as no class should have been found, loadedClass="+t0.loadedClass, t0.loadedClass);
      checkThrowable(NoClassDefFoundError.class, t0.loadError);
   }
   
   public void testLoading() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy j0 = createMockClassLoaderPolicy("j0");
      j0.setPathsAndPackageNames(Support.class);
      j0.setImportAll(true);
      j0.setIncluded(Class0.class);
      ClassLoader cl0 = system.registerClassLoaderPolicy(j0);

      MockClassLoaderPolicy j1 = createMockClassLoaderPolicy("j1");
      j1.setPathsAndPackageNames(Support.class);
      j1.setImportAll(true);
      j1.setIncluded(Class1.class);
      ClassLoader cl1 = system.registerClassLoaderPolicy(j1);

      MockClassLoaderPolicy j2 = createMockClassLoaderPolicy("j2");
      j2.setPathsAndPackageNames(Support.class);
      j2.setImportAll(true);
      j2.setIncluded(Class2.class);
      ClassLoader cl2 = system.registerClassLoaderPolicy(j2);

      LoadThread t0 = new LoadThread(Class2.class, cl0, cl2, "testLoading.T0");
      LoadThread t1 = new LoadThread(Class0.class, cl1, cl0, "testLoading.T1");
      LoadThread t2 = new LoadThread(Class1.class, cl2, cl1, "testLoading.T2");
      t0.start();
      t1.start();
      t2.start();

      // Join the threads
      t0.join();
      if (t0.loadError != null)
         failure("T0 failed", t0.loadError);
      assertNotNull("T0 failed", t0.loadedClass);

      t1.join(5000);
      if (t1.loadError != null)
         failure("T1 failed", t1.loadError);
      assertNotNull("T1 failed", t1.loadedClass);

      t2.join(5000);
      if (t2.loadError != null)
         failure("T2 failed", t2.loadError);
      assertNotNull("T2 failed", t2.loadedClass);
   }

   class LoadThread extends Thread
   {
      String classname;
      ClassLoader loader;
      ClassLoader expected;
      Class<?> loadedClass;
      Throwable loadError;
      boolean fails;

      LoadThread(Class<?> reference, ClassLoader loader, String name)
      {
         this(reference, loader, loader, name, false);
      }

      LoadThread(Class<?> reference, ClassLoader loader, String name, boolean fails)
      {
         this(reference, loader, loader, name, fails);
      }

      LoadThread(Class<?> reference, ClassLoader loader, ClassLoader expected, String name)
      {
         this(reference, loader, expected, name, false);
      }

      LoadThread(Class<?> reference, ClassLoader loader, ClassLoader expected, String name, boolean fails)
      {
         super(name);
         this.classname = reference.getName();
         this.loader = loader;
         this.expected = expected;
         this.fails = fails;
      }

      public void run()
      {
         try
         {
            if (fails)
               loader.loadClass(classname);
            else
               loadedClass = assertLoadClass(classname, loader, expected);
         }
         catch(Throwable t)
         {
            loadError = t;
         }
      }
   }
}
