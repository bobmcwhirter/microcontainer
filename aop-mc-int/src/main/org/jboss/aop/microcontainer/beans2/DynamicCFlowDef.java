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

import java.util.LinkedHashMap;

import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.DynamicCFlowDefinition;

/**
 * Bean to install a DynamicCFlow
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class DynamicCFlowDef
{
   /**
    * The AspectManager
    */
   private AspectManager manager;
   
   /**
    * The name of the resulting DynamicCFlow
    */
   private String name;
   
   /**
    * The name of the class implementing the dynamic cflow 
    */
   private String className;

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
   
   public String getClassName()
   {
      return className;
   }

   public void setClassName(String className)
   {
      this.className = className;
   }

   public void start()
   {
      if (name == null || name.length() == 0)
         throw new IllegalArgumentException("Null name");
      if (className == null || className.length() == 0)
         throw new IllegalArgumentException("Null className");
      if (manager == null)
         throw new IllegalArgumentException("Null manager");
      
      DynamicCFlowDefinition dynamic = new DynamicCFlowDefinition(null, className, name);
      manager.addDynamicCFlow(name, dynamic);
   }
   
   public void stop()
   {
      manager.removeCFlowStack(name);
   }
}
