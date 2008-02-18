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
package org.jboss.test.classloading.vfs.metadata;

import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Collections;

import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.classloader.plugins.ClassLoaderUtils;
import org.jboss.classloader.plugins.jdk.AbstractJDKChecker;
import org.jboss.classloader.plugins.system.DefaultClassLoaderSystem;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloading.spi.vfs.metadata.VFSClassLoaderFactory;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.kernel.junit.MicrocontainerTest;

/**
 * VFSClassLoadingMicrocontainerTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class VFSClassLoadingMicrocontainerTest extends MicrocontainerTest
{
   protected ClassLoaderSystem system;
   
   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      return new VFSClassLoadingMicrocontainerTestDelegate(clazz);
   }

   public static ClassLoaderSystem getClassLoaderSystem()
   {
      DefaultClassLoaderSystem system = new DefaultClassLoaderSystem();
      ClassLoaderDomain defaultDomain = system.getDefaultDomain();
      defaultDomain.setParentPolicy(ParentPolicy.BEFORE_BUT_JAVA_ONLY);
      return system;
   }
   
   public VFSClassLoadingMicrocontainerTest(String name)
   {
      super(name);
   }
   
   protected String getRoot(Class<?> clazz)
   {
      ProtectionDomain pd = clazz.getProtectionDomain();
      CodeSource cs = pd.getCodeSource();
      URL location = cs.getLocation();
      return location.toString();
   }
   
   protected String getContextName(VFSClassLoaderFactory factory)
   {
      String contextName = factory.getContextName();
      if (contextName == null)
         contextName = factory.getName() + ":" + factory.getVersion();
      return contextName;
   }
   
   protected ClassLoader assertClassLoader(VFSClassLoaderFactory factory) throws Exception
   {
      return assertClassLoader(getContextName(factory));
   }
   
   protected ClassLoader assertClassLoader(String name, String version) throws Exception
   {
      String contextName = name + ":" + version.toString();
      return assertClassLoader(contextName);
   }
   
   protected ClassLoader assertClassLoader(String name) throws Exception
   {
      Object obj = getBean(name);
      return assertInstanceOf(obj, ClassLoader.class);
   }
   
   protected void assertNoClassLoader(VFSClassLoaderFactory factory) throws Exception
   {
      assertNoClassLoader(getContextName(factory));
   }
   
   protected void assertNoClassLoader(String name, String version) throws Exception
   {
      String contextName = name + ":" + version.toString();
      assertNoClassLoader(contextName);
   }
   
   protected void assertClassLoader(Class<?> clazz, ClassLoader expected)
   {
      if (expected == null)
         return;
      ClassLoader cl = clazz.getClassLoader();
      boolean result = expected.equals(cl);
      assertTrue(ClassLoaderUtils.classToString(clazz) + " should have expected classloader=" + expected, result);
   }
   
   protected void assertClassEquality(Class<?> expected, Class<?> actual)
   {
      assertTrue("Should be the same " + ClassLoaderUtils.classToString(expected) +" and " + ClassLoaderUtils.classToString(actual), expected == actual);
   }
   
   protected void assertNoClassEquality(Class<?> expected, Class<?> actual)
   {
      assertTrue("Should NOT be the same " + ClassLoaderUtils.classToString(expected) +" and " + ClassLoaderUtils.classToString(actual), expected != actual);
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, ClassLoader start)
   {
      return assertLoadClass(reference, start, start, false);
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, ClassLoader start, boolean isReference)
   {
      return assertLoadClass(reference, start, start, isReference);
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, ClassLoader start, ClassLoader expected)
   {
      return assertLoadClass(reference, start, expected, false);
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, ClassLoader start, ClassLoader expected, boolean isReference)
   {
      Class<?> result = assertLoadClass(reference.getName(), start, expected);
      if (isReference)
         assertClassEquality(reference, result);
      else
         assertNoClassEquality(reference, result);
      return result;
   }
   
   protected Class<?> assertLoadClass(String name, ClassLoader start)
   {
      return assertLoadClass(name, start, start);
   }
   
   protected Class<?> assertLoadClass(String name, ClassLoader start, ClassLoader expected)
   {
      Class<?> result = null;
      try
      {
         result = start.loadClass(name);
         getLog().debug("Got class: " + ClassLoaderUtils.classToString(result) + " for " + name + " from " + start);
      }
      catch (ClassNotFoundException e)
      {
         failure("Did not expect CNFE for " + name + " from " + start, e);
      }
      assertClassLoader(result, expected);
      return result;
   }
   
   protected void assertLoadClassFail(Class<?> reference, ClassLoader start)
   {
      assertLoadClassFail(reference.getName(), start);
   }
      
   protected void assertLoadClassFail(String name, ClassLoader start)
   {
      try
      {
         start.loadClass(name);
         fail("Should not be here!");
      }
      catch (Exception expected)
      {
         checkThrowable(ClassNotFoundException.class, expected);
      }
   }
   
   protected KernelDeployment install(VFSClassLoaderFactory metaData) throws Exception
   {
      AbstractKernelDeployment deployment = new AbstractKernelDeployment();
      deployment.setName(metaData.getName() + ":" + metaData.getVersion());
      deployment.setBeanFactories(Collections.singletonList((BeanMetaDataFactory) metaData));
      deploy(deployment);
      return deployment;
   }

   protected void assertNoClassLoader(String name) throws Exception
   {
      try
      {
         Object bean = getBean(name, null);
         if (bean != null)
            fail("Should not be here: " + bean);
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalStateException.class, t);
      }
   }
   
   protected void setUp() throws Exception
   {
      super.setUp();
      // This is a hack for a hack. ;-)
      AbstractJDKChecker.getExcluded().add(VFSClassLoadingMicrocontainerTest.class);
      
      system = (ClassLoaderSystem) getBean("ClassLoaderSystem");
   }
}
