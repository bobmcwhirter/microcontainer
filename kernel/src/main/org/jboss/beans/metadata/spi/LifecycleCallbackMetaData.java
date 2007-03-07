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
package org.jboss.beans.metadata.spi;

import org.jboss.dependency.spi.ControllerState;
import org.jboss.util.JBossInterface;

/**
 * Metadata about lifecycle callbacks
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public interface LifecycleCallbackMetaData extends JBossInterface, MetaDataVisitorNode
{
   /**
    * Get the target state of the bean this callback applies to indicating when this callback should trigger
    * @return the state 
    */
   ControllerState getWhenRequired();
   
   /**
    * The required state of the lifecycle callback bean
    */
   ControllerState getDependentState();
   
   /**
    * Get the bean this callback should be made on
    * @return the state 
    */
   String getBean();

   /**
    * Get the method on the bean that should be called when reaching the required state on installation
    */
   public String getInstallMethod();


   /**
    * Get the method on the bean that should be called when reaching the required state on uninstallation
    */
   public String getUninstallMethod();

}
