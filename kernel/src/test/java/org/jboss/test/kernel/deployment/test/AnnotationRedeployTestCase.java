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
package org.jboss.test.kernel.deployment.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractClassLoaderMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.metadata.spi.MetaData;
import org.jboss.test.kernel.annotations.support.TestBean;
import org.jboss.test.kernel.deployment.support.TestAnnotation1;

/**
 * AnnotationRedeployTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class AnnotationRedeployTestCase extends AbstractDeploymentTest
{
   public static Test suite()
   {
      return suite(AnnotationRedeployTestCase.class);
   }

   public AnnotationRedeployTestCase(String name) throws Throwable
   {
      super(name);
   }

   public void testAnnotationRedeploy() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("test", TestBean.class.getName());
      builder.addAnnotation("@" + TestAnnotation1.class.getName());
      ClassLoader cl = new TestClassLoader(getClass().getClassLoader());
      builder.setClassLoader(cl);
      BeanMetaData beanMetaData = builder.getBeanMetaData();
      MetaData metaData = null;

      TestClassLoader2 tcl = new TestClassLoader2(getClass().getClassLoader());
      ClassLoader old = setContextClassLoader(tcl);
      try
      {
         
         KernelControllerContext context = deploy(beanMetaData);
         try
         {
            validate();
            metaData = context.getScopeInfo().getMetaData();
            assertNotNull(metaData);
            Object annotation = metaData.getMetaData(TestAnnotation1.class.getName());
            assertNotNull(annotation);
            assertEquals(cl, annotation.getClass().getClassLoader());
         }
         finally
         {
            undeploy(context);
         }
         
         assertNull(metaData.getMetaData(TestAnnotation1.class.getName()));

         cl = new TestClassLoader(getClass().getClassLoader());
         beanMetaData.setClassLoader(new AbstractClassLoaderMetaData(new AbstractValueMetaData(cl)));
         context = deploy(beanMetaData);
         try
         {
            validate();
            metaData = context.getScopeInfo().getMetaData();
            assertNotNull(metaData);
            Object annotation = metaData.getMetaData(TestAnnotation1.class.getName());
            assertNotNull(annotation);
            assertEquals(cl, annotation.getClass().getClassLoader());
         }
         finally
         {
            undeploy(context);
         }
      }
      finally
      {
         setContextClassLoader(old);
      }
   }
   
   protected ClassLoader setContextClassLoader(ClassLoader tcl)
   {
      SecurityManager sm = suspendSecurity();
      try
      {
         Thread thread = Thread.currentThread();
         ClassLoader result = thread.getContextClassLoader();
         thread.setContextClassLoader(tcl);
         return result;
      }
      finally
      {
         resumeSecurity(sm);
      }
   }
   
   
   private class TestClassLoader extends ClassLoader
   {
      public TestClassLoader(ClassLoader parent)
      {
         super(parent);
      }

      protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
      {
         if (TestAnnotation1.class.getName().equals(name))
         {
            String resourceName = "/" + name.replace('.', '/') + ".class";
            URL url = AnnotationRedeployTestCase.this.getResource(resourceName);
            InputStream is = null;
            byte[] bytes = null;
            try
            {
               is = url.openStream();
               ByteArrayOutputStream baos = new ByteArrayOutputStream();
               byte[] tmp = new byte[1024];
               int read = 0;
               while ( (read = is.read(tmp)) >= 0 )
                  baos.write(tmp, 0, read);
               bytes = baos.toByteArray();
            }
            catch (IOException e)
            {
               throw new RuntimeException("Unable to load class byte code " + name, e);
            }
            finally
            {
               try
               {
                  if (is != null)
                     is.close();
               }
               catch (IOException e)
               {
                  // pointless
               }
            }
            
            return defineClass(name, bytes, 0, bytes.length);
         }
         return super.loadClass(name, resolve);
      }
   }
   
   private class TestClassLoader2 extends ClassLoader
   {
      public TestClassLoader2(ClassLoader parent)
      {
         super(parent);
      }

      protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
      {
         if (TestAnnotation1.class.getName().equals(name))
            throw new ClassNotFoundException("Masked: " + name);
         return super.loadClass(name, resolve);
      }
   }
}