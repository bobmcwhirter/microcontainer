/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.microcontainer.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import junit.framework.Test;

import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.AspectFactory;
import org.jboss.aop.advice.AspectFactoryWithClassLoader;
import org.jboss.aop.microcontainer.beans.Aspect;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.support.ParentLastURLClassLoader;
import org.jboss.test.microcontainer.support.TestAspect;

/**
 * Checks that an aspect can have its classloader overridden
 *
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
public class OverriddenClassLoaderTestCase extends AOPMicrocontainerTest
{
   public OverriddenClassLoaderTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(OverriddenClassLoaderTestCase.class);
   }

   public void testOverriddenLoader() throws Exception
   {
      ClassLoader loader = createParentLastURLClassLoader();
      
      Class<?> clazz = loader.loadClass(TestAspect.class.getName());
      assertNotSame(TestAspect.class, clazz);
      assertSame(loader, clazz.getClassLoader());
      
      Aspect aspect = assertInstanceOf(getBean("Aspect"), Aspect.class, false);     
      AspectDefinition def = aspect.getDefinition();
      AspectFactory factory = def.getFactory();
      
      Object global = factory.createPerVM();
      assertSame(getClass().getClassLoader(), global.getClass().getClassLoader());

      AspectFactoryWithClassLoader factoryCl = assertInstanceOf(factory, AspectFactoryWithClassLoader.class);
      factoryCl.pushScopedClassLoader(loader);
      try
      {
         Object scoped = factory.createPerVM();
         ClassLoader scopedLoader = scoped.getClass().getClassLoader();
         assertSame(loader, scopedLoader);
      }
      finally
      {
         factoryCl.popScopedClassLoader();
      }
   }
   
   private ClassLoader createParentLastURLClassLoader() throws Exception
   {
      File jarFile = createJar();
      
      URL jarUrl = jarFile.toURL();
      return new ParentLastURLClassLoader(new URL[] {jarUrl}, getClass().getClassLoader());
   }
   
   private File createJar() throws Exception
   {
      String resource = TestAspect.class.getName().replace('.', '/') + ".class";
      URL url = getClass().getClassLoader().getResource(resource);
      assertNotNull(resource + " not found.", url);
      File classFile = new File(url.toURI());

      //    File jarFile = new File("test-overridden-classloader.jar");
      File jarFile = createTempFile("test-overridden-classloader", "jar");
      jarFile.deleteOnExit();
      
      if (jarFile.exists())
      {
         assertTrue(jarFile.delete());
      }
      JarOutputStream out = null;
      try
      {
         out = new JarOutputStream(new FileOutputStream(jarFile), new Manifest());
         JarEntry entry = new JarEntry(resource);
         out.putNextEntry(entry);
         FileInputStream in = null;
         try
         {
            in = new FileInputStream(classFile);
            byte[] buf = new byte[200000];
            while(true)
            {
               int i = in.read(buf, 0, buf.length);
               if (i <=0)
               {
                  break;
               }
               out.write(buf, 0, i);
            }
         }
         finally
         {
            in.close();
         }
      }
      finally
      {
         out.close();
      }
      getLog().debug("===> Created jar " + jarFile.getAbsolutePath());
      return jarFile;
   }
   
   private File createTempFile(final String prefix, final String suffix) throws Exception
   {
      if (System.getSecurityManager() == null)
         return File.createTempFile(prefix, suffix);
      else
      {
         try
         {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<File>()
            {
               public File run() throws Exception
               {
                  return File.createTempFile(prefix, suffix);
               }
            });
         }
         catch (PrivilegedActionException e)
         {
            throw e.getException();
         }
      }
   }
}
