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
package org.jboss.test.classloader.bootstrap.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import javax.naming.Context;

import junit.framework.Test;

import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.test.support.MockClassLoaderPolicy;
import org.jboss.test.classloader.AbstractClassLoaderTestWithSecurity;
import org.jboss.test.classloader.bootstrap.support.TestClass;

/**
 * ModifiedBootstrapUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ModifiedBootstrapUnitTestCase extends AbstractClassLoaderTestWithSecurity
{
   public static Test suite()
   {
      return suite(ModifiedBootstrapUnitTestCase.class);
   }

   public ModifiedBootstrapUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testJavaLangObject() throws Exception
   {
      testBootstrapClass(Object.class);
   }
   
   public void testJavaUtilList() throws Exception
   {
      testBootstrapClass(List.class);
   }
   
   public void testJavaxNamingContext() throws Exception
   {
      testBootstrapClass(Context.class);
   }
   
   public void testClassLoaderDomain() throws Exception
   {
      testNotBootstrapClass(ClassLoaderDomain.class);
   }
   
   public void testReflection() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPathsAndPackageNames(TestClass.class);
      ClassLoader classLoader = system.registerClassLoaderPolicy(policy);
      
      Class<?> clazz = assertLoadClass(TestClass.class, classLoader);
      Constructor<?> constructor = clazz.getConstructor((Class[]) null);
      
      Object object = constructor.newInstance((Object[]) null);
      
      Method method = clazz.getMethod("doSomething", (Class[]) null);
      Object result = method.invoke(object, (Object[]) null);
      assertEquals(classLoader, result);
   }
   
   protected void testBootstrapClass(Class<?> clazz) throws Exception
   {
      ClassLoader classLoader = createClassLoaderSystemWithModifiedBootstrapAndMockClassLoader();
      assertLoadClass(clazz, classLoader, clazz.getClassLoader(), true);
   }
   
   protected void testNotBootstrapClass(Class<?> clazz) throws Exception
   {
      ClassLoader classLoader = createClassLoaderSystemWithModifiedBootstrapAndMockClassLoader();
      assertLoadClassFail(clazz, classLoader);
   }
}
