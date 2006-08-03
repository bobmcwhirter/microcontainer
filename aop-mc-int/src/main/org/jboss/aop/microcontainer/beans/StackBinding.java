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
package org.jboss.aop.microcontainer.beans;

import java.util.Iterator;
import java.util.List;

import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.ScopedInterceptorFactory;
import org.jboss.util.id.GUID;

public class StackBinding
{
   protected String name = GUID.asString();

   protected AspectManager manager;
   
   protected String pointcut;

   protected List advices;
   
   /**
    * Get the manager.
    * 
    * @return the manager.
    */
   public AspectManager getManager()
   {
      return manager;
   }

   /**
    * Set the manager.
    * 
    * @param manager The manager to set.
    */
   public void setManager(AspectManager manager)
   {
      this.manager = manager;
   }

   /**
    * Get the pointcut.
    * 
    * @return the pointcut.
    */
   public String getPointcut()
   {
      return pointcut;
   }

   /**
    * Set the pointcut.
    * 
    * @param pointcut The pointcut to set.
    */
   public void setPointcut(String pointcut)
   {
      this.pointcut = pointcut;
   }

   /**
    * Get the advices.
    * 
    * @return the advices.
    */
   public List getAdvices()
   {
      return advices;
   }

   /**
    * Set the advices.
    * 
    * @param advices The advices to set.
    */
   public void setAdvices(List advices)
   {
      this.advices = advices;
   }

   public void start() throws Exception
   {
      if (pointcut == null)
         throw new IllegalArgumentException("Null pointcut");
      if (manager == null)
         throw new IllegalArgumentException("Null manager");
      if (advices == null || advices.size() == 0)
         throw new IllegalArgumentException("No advices");
      AdviceBinding binding = new AdviceBinding(name, pointcut, null);
      for (Iterator i = advices.iterator(); i.hasNext();)
      {
         AspectDefinition aspect = (AspectDefinition) i.next();
         binding.addInterceptorFactory(new ScopedInterceptorFactory(aspect));
      }
      manager.addBinding(binding);
   }
   
   public void stop() throws Exception
   {
      manager.removeBinding(name);
   }
}
