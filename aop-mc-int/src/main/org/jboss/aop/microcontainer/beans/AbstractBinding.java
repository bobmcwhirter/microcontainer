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

import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.util.id.GUID;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractBinding
{
   protected AspectManager manager;
   
   protected String name = GUID.asString();
   
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

   public void start() throws Exception
   {
      createAndAddBinding();
   }
   
   public void stop() throws Exception
   {
      removeBinding();
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
   
   protected abstract void createAndAddBinding() throws Exception;
   protected abstract void removeBinding() throws Exception;
}
