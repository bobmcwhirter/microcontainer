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

import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.advice.AdviceFactory;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.logging.Logger;
import org.jboss.util.id.GUID;

/**
 * An AspectBinding.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AspectBinding
{
   private static final Logger log = Logger.getLogger(AspectBinding.class);
   
   protected AspectManager manager;
   
   protected String name = GUID.asString();
   
   protected String pointcut;
   
   protected AspectDefinition aspect;
   
   protected String method = "invoke";
   
   /**
    * Get the aspectDefinition.
    * 
    * @return the aspectDefinition.
    */
   public AspectDefinition getAspect()
   {
      return aspect;
   }

   /**
    * Set the aspectDefinition.
    * 
    * @param aspect The aspectDefinition to set.
    */
   public void setAspect(AspectDefinition aspect)
   {
      this.aspect = aspect;
   }

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
    * Get the method.
    * 
    * @return the method.
    */
   public String getMethod()
   {
      return method;
   }

   /**
    * Set the method.
    * 
    * @param method The method to set.
    */
   public void setMethod(String method)
   {
      this.method = method;
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

   public void start() throws Exception
   {
      if (pointcut == null)
         throw new IllegalArgumentException("Null pointcut");
      if (manager == null)
         throw new IllegalArgumentException("Null manager");
      if (aspect == null)
         throw new IllegalArgumentException("Null aspect definition");
      AdviceBinding binding = new AdviceBinding(name, pointcut, null);
      binding.addInterceptorFactory(new AdviceFactory(aspect, method));
      manager.addBinding(binding);
//      System.out.println("----> Added binding " + pointcut);
      log.debug("Bound binding " + name);
   }
   
   public void stop() throws Exception
   {
      manager.removeBinding(name);
   }

   public void uninstall() throws Exception
   {
      stop();
   }
   
   public void rebind(AspectDefinition aspect) throws Exception
   {
      this.aspect = aspect;
      stop();
      start();
   }
}
