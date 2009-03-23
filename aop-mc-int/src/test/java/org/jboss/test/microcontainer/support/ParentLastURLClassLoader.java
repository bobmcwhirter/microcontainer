/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.microcontainer.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ParentLastURLClassLoader extends URLClassLoader
{
   ClassLoader parent;
   URLClassLoader delegate;

   public ParentLastURLClassLoader(URL[] urls, ClassLoader parent)
   {
      super(urls, parent);
      delegate = new URLClassLoader(urls);
      this.parent = parent;
   }

   @Override
   public URL findResource(String name)
   {
      URL url = delegate.findResource(name);
      if (url == null && parent instanceof URLClassLoader)
      {
         url = ((URLClassLoader)parent).findResource(name);
      }
      return url;
   }

   @Override
   public URL getResource(String name)
   {
      URL url = delegate.getResource(name);
      if (url == null)
      {
         url = parent.getResource(name);
      }
      return url;
   }

   @Override
   public Class<?> loadClass(String name) throws ClassNotFoundException
   {
      Class<?> clazz = findLoadedClass(name);
      if (clazz != null)
      {
         return clazz;
      }
      String resourceName = name.replace('.', '/') + ".class";
      URL mine = delegate.findResource(resourceName);
      parent.getResource(resourceName);
      if (mine == null || mine.equals(parent))
      {
         return delegate.loadClass(name);
      }
      else
      {
         byte[] bytes = loadBytes(name, resourceName);
         return defineClass(name, bytes, 0, bytes.length);
      }
   }

   private byte[] loadBytes(String name, String resourceName)
   {
      InputStream in = delegate.getResourceAsStream(resourceName);
      try
      {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         byte[] tmp = new byte[1024];
         int read = 0;
         while ( (read = in.read(tmp)) >= 0 )
            baos.write(tmp, 0, read);
         return baos.toByteArray();
      }
      catch (IOException e)
      {
         throw new RuntimeException("Unable to load class byte code " + name, e);
      }
      finally
      {
         try
         {
            in.close();
         }
         catch (IOException e)
         {
         }
      }
   }
   
}