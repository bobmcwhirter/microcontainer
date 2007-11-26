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

import java.util.List;

import org.jboss.aop.AspectManager;
import org.jboss.aop.pointcut.CFlow;

/**
 * Bean to install a CFlowStack
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class CFlowStack
{
   /**
    * The AspectManager
    */
   private AspectManager manager;
   
   /**
    * The name of the resulting CFlowStack
    */
   private String name;
   
   /**
    * The CFlowStack entries
    */
   private List<CFlowStackEntry> entries;
   
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
   
   public List<CFlowStackEntry> getEntries()
   {
      return entries;
   }
   
   public void setEntries(List<CFlowStackEntry> entries)
   {
      this.entries = entries;
   }
   
   public void start()
   {
      if (name == null || name.length() == 0)
      {
         throw new IllegalArgumentException("Null name");
      }
      if (entries == null || entries.size() == 0)
      {
         throw new IllegalArgumentException("No entries");
      }
      
      org.jboss.aop.pointcut.CFlowStack stack = new org.jboss.aop.pointcut.CFlowStack(name);
      for (CFlowStackEntry entry : entries)
      {
         boolean notCalled = !entry.getCalled();
         stack.addCFlow(new CFlow(entry.getExpr(), notCalled));
      }
      manager.addCFlowStack(stack);
   }
   
   public void stop()
   {
      manager.removeCFlowStack(name);
   }
}
