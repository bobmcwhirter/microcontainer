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
package org.jboss.aop.microcontainer.beans2;

import org.jboss.aop.AspectManager;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ClassMetaDataLoader
{
   AspectManager manager;
   String tag;
   String className;

   public AspectManager getManager()
   {
      return manager;
   }

   public void setManager(AspectManager manager)
   {
      this.manager = manager;
   }

   public String getTag()
   {
      return tag;
   }

   public void setTag(String tag)
   {
      this.tag = tag;
   }

   public String getClassName()
   {
      return className;
   }

   public void setClassName(String className)
   {
      this.className = className;
   }

   public void start()
   {
      if (manager == null)
         throw new IllegalArgumentException("Null manager");
      if (tag == null)
         throw new IllegalArgumentException("Null tag");
      if (className == null)
         throw new IllegalArgumentException("Null className");
      
      try
      {
         //FIXME Probably need to use something else
         ClassLoader cl = SecurityActions.getContextClassLoader();
         Class clazz = cl.loadClass(className);
         org.jboss.aop.metadata.ClassMetaDataLoader loader = (org.jboss.aop.metadata.ClassMetaDataLoader)clazz.newInstance();
         
         manager.addClassMetaDataLoader(tag, loader);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }
   
   public void stop()
   {
      manager.removeClassMetaDataLoader(tag);
   }
}
