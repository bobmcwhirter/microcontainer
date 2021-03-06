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
package org.jboss.aop.microcontainer.beans;

import java.util.ArrayList;
import java.util.List;

import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.AdviceStack;
import org.jboss.aop.advice.InterceptorFactory;

/**
 * Defines an interceptor stack
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class Stack
{
   private AspectManager manager;
   
   private String name;
   
   private List<InterceptorEntry> advices;

   public AspectManager getManager()
   {
      return manager;
   }

   public void setManager(AspectManager manager)
   {
      this.manager = manager;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public List<InterceptorEntry> getAdvices()
   {
      return advices;
   }

   public void setAdvices(List<InterceptorEntry> advices)
   {
      this.advices = advices;
   }
   
   public List<InterceptorEntry> getClonedAdvices(Binding binding)
   {
      List<InterceptorEntry> entries = new ArrayList<InterceptorEntry>();
 
      for (InterceptorEntry entry : advices)
      {
         InterceptorEntry cloned = (InterceptorEntry)entry.clone();
         cloned.setBinding(binding);
         entries.add(cloned);
      }
      
      return entries;
   }
   
   public void start()
   {
      if (manager == null)
      {
         throw new IllegalStateException("Null manager");
      }
      if (name == null)
      {
         throw new IllegalStateException("Null name");
      }
      if (advices == null)
      {
         throw new IllegalStateException("Null advices");
      }
      
      ArrayList<InterceptorFactory> factories = new ArrayList<InterceptorFactory>();
      for (InterceptorEntry advice : advices)
      {
         factories.add(advice.getInterceptorFactory());
      }
      AdviceStack stack = new AdviceStack(name, factories);
      manager.addAdviceStack(stack);
   }
   
   public void stop()
   {
      manager.removeInterceptorStack(name);
   }
}
