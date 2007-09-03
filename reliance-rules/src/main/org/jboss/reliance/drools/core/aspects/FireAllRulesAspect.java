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
package org.jboss.reliance.drools.core.aspects;

import java.lang.reflect.Method;

import org.drools.Agenda;
import org.drools.WorkingMemory;
import org.drools.spi.AgendaFilter;
import org.jboss.aop.joinpoint.MethodInvocation;

/**
 * Invocation aware working memory handler.
 * Checking if we need to fire all rules.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class FireAllRulesAspect
{
   private AgendaFilter filter;
   private Integer limit;

   /**
    * Check if we should fire all rules.
    *
    * @param method the method
    * @param args the arguments
    * @return true if we should fire
    */
   protected boolean shouldFileAllRules(Method method, Object[] args)
   {
      return method.getName().startsWith("fireAllRules") == false;
   }

   /**
    * Inovke method on working memory instance,
    * and conditionally fire all rules.
    *
    * @param invocation the invocation
    * @return invocation's result
    * @throws Throwable for any error
    */
   public Object invoke(MethodInvocation invocation) throws Throwable
   {
      Object target = invocation.getTargetObject();
      if (target instanceof WorkingMemory == false)
         throw new IllegalArgumentException("Target must implement WorkingMemory.");
      WorkingMemory delegate = (WorkingMemory)target;
      Object result = invocation.invokeNext();
      if (shouldFileAllRules(invocation.getActualMethod(), invocation.getArguments()))
      {
         if (filter != null && limit != null)
         {
            delegate.fireAllRules(filter, limit);
         }
         else if (filter != null)
         {
            delegate.fireAllRules(filter);
         }
         else if (limit != null)
         {
            delegate.fireAllRules(limit);
         }
         else
         {
            delegate.fireAllRules();
         }
         Agenda agenda = delegate.getAgenda();
      }
      return result;
   }

   /**
    * Set the filter.
    *
    * @param filter the agenda filter
    */
   public void setFilter(AgendaFilter filter)
   {
      this.filter = filter;
   }

   /**
    * Set the limit.
    *
    * @param limit the fire limit
    */
   public void setLimit(Integer limit)
   {
      this.limit = limit;
   }
}
