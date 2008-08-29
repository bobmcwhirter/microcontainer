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
package org.jboss.dependency.spi;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public interface LifecycleCallbackItem
{
   /**
    * Gets the target bean implementing this callback
    * @return the target bean name
    */
   Object getBean();
   
   /**
    * Get the target state of the bean this callback applies to indicating when this callback should trigger
    * @return the state 
    */
   ControllerState getWhenRequired();
   
   /**
    * The required state of the lifecycle callback bean
    * @return the dependant state
    */
   ControllerState getDependentState();
   
   /**
    * Call when the target bean is installed
    * @param ctx the context of the target bean
    * @throws Exception for any error
    */
   void install(ControllerContext ctx) throws Exception;
   
   /**
    * Call when the target bean is uninstalled
    * @param ctx the context of the target bean
    */
   void uninstall(ControllerContext ctx);
}
