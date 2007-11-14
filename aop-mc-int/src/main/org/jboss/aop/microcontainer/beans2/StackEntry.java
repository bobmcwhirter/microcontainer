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

import java.util.List;

import org.jboss.aop.advice.InterceptorFactory;

/**
 * A stack-ref entry.
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class StackEntry extends BindingEntry
{
   Stack stack;
   List<InterceptorEntry> advices; 
   InterceptorFactory[] factories;
   
   public Stack getStack()
   {
      return stack;
   }

   public void setStack(Stack stack)
   {
      this.stack = stack;
   }

   public InterceptorFactory[] getInterceptorFactories()
   {
      return factories;
   }

   public void start()
   {
      if (manager == null)
      {
         throw new IllegalArgumentException("Null manager");
      }
      if (aspectBinding == null)
      {
         throw new IllegalArgumentException("Null aspect binding");
      }
      if (stack == null)
      {
         throw new IllegalArgumentException("Null stack");
      }
      advices = stack.getClonedAdvices();
      if (advices == null)
      {
         throw new IllegalArgumentException("Null advices");
      }
         
      
      factories = new InterceptorFactory[advices.size()];
      int i = 0;
      for (InterceptorEntry entry : advices)
      {
         entry.start();
         factories[i++] = entry.getInterceptorFactory(); 
      }
   }

   public void stop()
   {
      advices = null;
      factories = null;
   }
   
}
