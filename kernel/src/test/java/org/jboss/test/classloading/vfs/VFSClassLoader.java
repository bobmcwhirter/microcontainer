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

package org.jboss.test.classloading.vfs;

import java.net.URLClassLoader;
import java.net.URL;
import java.net.URLStreamHandlerFactory;

/**
 @author Scott.Stark@jboss.org
 @version $Revision$
 */
public class VFSClassLoader extends URLClassLoader
{
   public VFSClassLoader(URL[] urls, ClassLoader parent)
   {
      super(urls, parent);
   }

   public VFSClassLoader(URL[] urls)
   {
      super(urls, new NullClassLoader());
   }

   public VFSClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory)
   {
      super(urls, parent, factory);
   }

   public Class<?> loadClass(String name, boolean flag)
      throws ClassNotFoundException
   {
      
      if (name.startsWith("org.jboss.test") == false)
         return getClass().getClassLoader().loadClass(name);
      Class<?> c;
      try
      {
         c = super.loadClass(name, flag);
      }
      catch(ClassNotFoundException e)
      {
         c = getClass().getClassLoader().loadClass(name);
      }
      return c;
   }
}
