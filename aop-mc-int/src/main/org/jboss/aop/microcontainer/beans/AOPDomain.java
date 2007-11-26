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
import org.jboss.aop.DomainDefinition;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class AOPDomain
{
   String name;
   AspectManager manager;
   boolean parentFirst=false;
   boolean inheritDefinitions=true;
   boolean inheritBindings=false;
   String extendz;
   DomainDefinition definition;

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

   public boolean isParentFirst()
   {
      return parentFirst;
   }

   public void setParentFirst(boolean parentFirst)
   {
      this.parentFirst = parentFirst;
   }

   public boolean isInheritDefinitions()
   {
      return inheritDefinitions;
   }

   public void setInheritDefinitions(boolean inheritDefinitions)
   {
      this.inheritDefinitions = inheritDefinitions;
   }

   public boolean isInheritBindings()
   {
      return inheritBindings;
   }

   public void setInheritBindings(boolean inheritBindings)
   {
      this.inheritBindings = inheritBindings;
   }

   public String getExtends()
   {
      return extendz;
   }

   public void setExtends(String extendz)
   {
      this.extendz = extendz;
   }

   public AspectManager getDomain()
   {
      return definition.getManager();
   }
   
   public void start()
   {
      AspectManager parent = manager;
      if (extendz != null)
      {
         DomainDefinition parentDef = manager.getContainer(extendz);
         if (parentDef == null)
         {
            throw new RuntimeException("unable to find parent Domain: " + extendz);
         }
         parent = parentDef.getManager();
      }
         
      definition = new DomainDefinition(name, parent, parentFirst, inheritDefinitions, inheritBindings);
      manager.addContainer(definition);
   }
   
   public void stop()
   {
      manager.removeContainer(name);
   }
   
}
