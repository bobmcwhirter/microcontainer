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
package org.jboss.aop.microcontainer.beans2;

import java.util.List;

import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.advice.InterceptorFactory;
import org.jboss.logging.Logger;
import org.jboss.util.id.GUID;

/**
 * An AspectBinding.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 61194 $
 */
public class AspectBinding
{
   private static final Logger log = Logger.getLogger(AspectBinding.class);

   protected AspectManager manager;

   protected String name = GUID.asString();

   protected String pointcut;

   protected List<BindingEntry> advices;

   protected String cflow;
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

   
   public String getCflow()
   {
      return cflow;
   }

   public void setCflow(String cflow)
   {
      this.cflow = cflow;
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

   public String getName()
   {
      return name;
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

   public List<BindingEntry> getAdvices()
   {
      return advices;
   }

   public void setAdvices(List<BindingEntry> advices)
   {
      this.advices = advices;
   }

   public void start() throws Exception
   {
      if (pointcut == null)
         throw new IllegalArgumentException("Null pointcut");
      if (manager == null)
         throw new IllegalArgumentException("Null manager");
      AdviceBinding binding = new AdviceBinding(name, pointcut, cflow);

      for (BindingEntry entry : advices)
      {
         entry.start();
         InterceptorFactory[] factories = entry.getInterceptorFactories();
         for (InterceptorFactory ifac : factories)
         {
            binding.addInterceptorFactory(ifac);
         }
      }
      manager.addBinding(binding);
      log.debug("Bound binding " + name);
   }

   public void stop() throws Exception
   {
      manager.removeBinding(name);
      for (BindingEntry entry : advices)
      {
         entry.stop();
      }
   }

   public void uninstall() throws Exception
   {
      stop();
   }

   public void rebind() throws Exception
   {
      stop();
      start();
   }
}
