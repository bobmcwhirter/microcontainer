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

import java.util.List;

import org.jboss.aop.AspectManager;
import org.jboss.aop.introduction.InterfaceIntroduction;
import org.jboss.util.id.GUID;

/**
 * An AspectBinding.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class IntroductionBinding
{
   protected AspectManager manager;
   
   protected String name = GUID.asString();
   
   protected String classes;
   
   protected List<String> interfaces;

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Get the classes.
    * 
    * @return the classes.
    */
   public String getClasses()
   {
      return classes;
   }

   /**
    * Set the classes.
    * 
    * @param classes The classes to set.
    */
   public void setClasses(String classes)
   {
      this.classes = classes;
   }

   /**
    * Get the interfaces.
    * 
    * @return the interfaces.
    */
   public List getInterfaces()
   {
      return interfaces;
   }

   /**
    * Set the interfaces.
    * 
    * @param interfaces The interfaces to set.
    */
   public void setInterfaces(List<String> interfaces)
   {
      this.interfaces = interfaces;
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

   public void start() throws Exception
   {
      if (manager == null)
         throw new IllegalArgumentException("Null manager");
      if (classes == null)
         throw new IllegalArgumentException("Null classes");
      if (interfaces == null)
         throw new IllegalArgumentException("Null interfaces");
      String[] intfs = interfaces.toArray(new String[interfaces.size()]);
      InterfaceIntroduction introduction = new InterfaceIntroduction(name, classes, intfs);
      manager.addInterfaceIntroduction(introduction);
   }
   
   public void stop() throws Exception
   {
      manager.removeInterfaceIntroduction(name);
   }
}
