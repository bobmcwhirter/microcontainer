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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.metadata.spi.MetaData;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class LifecycleCallback
{
   public static Map<String, ArrayList<Class<?>>> interceptions = new HashMap<String, ArrayList<Class<?>>>();
   @SuppressWarnings("unchecked")
   Class[] lifecycleAnnotations = new Class[] {Configure.class, Create.class, Describe.class, Install.class, Instantiate.class, Start.class}; 
   public String getName()
   {
      return this.getClass().getName();
   }

   public void install(ControllerContext context)
   {
      handle(context);
   }
   
   public void uninstall(ControllerContext context) 
   {
      handle(context);
   }
   
   @SuppressWarnings("unchecked")
   private void handle(ControllerContext context)
   {
      if (context instanceof KernelControllerContext)
      {
         for (int i = 0 ; i < lifecycleAnnotations.length ; i++)
         {
            MetaData metaData = context.getScopeInfo().getMetaData();
            Object cur = metaData.getAnnotation(lifecycleAnnotations[i]);
            if (cur != null)
            {
               addInterception(context, lifecycleAnnotations[i]);
            }
         }
      }
      else
      {
         throw new RuntimeException(context + " is not a KCC!");
      }
   }
   
   private void addInterception(ControllerContext context, Class<?> annotation)
   {
      String name = (String)context.getName();
      ArrayList<Class<?>> beanInterceptions = interceptions.get(name);
      if (beanInterceptions == null)
      {
         beanInterceptions = new ArrayList<Class<?>>();
         interceptions.put(name, beanInterceptions);
      }
      beanInterceptions.add(annotation);
   }
}
