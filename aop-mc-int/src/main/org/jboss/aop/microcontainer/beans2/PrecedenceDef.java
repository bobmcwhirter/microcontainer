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

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.PrecedenceDefEntry;
import org.jboss.util.id.GUID;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class PrecedenceDef
{
   private AspectManager manager;
   private LinkedHashMap<String, String> entries;
   private String name = GUID.asString();
   
   public AspectManager getManager()
   {
      return manager;
   }
   
   public void setManager(AspectManager manager)
   {
      this.manager = manager;
   }
   
   public LinkedHashMap<String, String> getentries()
   {
      return entries;
   }
   
   public void setEntries(LinkedHashMap<String, String> precedenceEntries)
   {
      this.entries = precedenceEntries;
   }
   
   public String getName()
   {
      return name;
   }
   
   public void setName(String name)
   {
      this.name = name;
   }
   
   public void start()
   {
      if (manager == null)
         throw new IllegalArgumentException("Null manager");
      if (entries == null)
         throw new IllegalArgumentException("Null entries");
      if (entries.size() == 0)
         throw new IllegalArgumentException("No entries");
      
      PrecedenceDefEntry[] pentries = new PrecedenceDefEntry[entries.size()];
      int i = 0;
      for (String aspect : entries.keySet())
      {
         pentries[i++] = new PrecedenceDefEntry(aspect, entries.get(aspect));
      }
      
      manager.addPrecedence(new org.jboss.aop.advice.PrecedenceDef(name, pentries));
   }
   
   public void stop()
   {
      manager.removePrecedence(name);
   }
}
