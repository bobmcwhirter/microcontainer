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
package org.jboss.example.microcontainer.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A ObjectPrinter.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 37459 $
 */
public class ObjectPrinter
{
   String context;
   
   public ObjectPrinter(String context)
   {
      this.context = context;
   }
   
   public void setPrint(Object print)
   {
      if (print != null)
      {
         System.out.println(context + " type=" + print.getClass().getName());
         if (print instanceof Collection)
         {
            for (Iterator i = ((Collection) print).iterator(); i.hasNext();)
            {
               Object object = i.next();
               System.out.println("value='" + object + "' type=" + object.getClass().getName());
               
            }
         }
         else if (print instanceof Map)
         {
            for (Iterator i = ((Map) print).entrySet().iterator(); i.hasNext();)
            {
               Entry entry = (Entry) i.next();
               Object key = entry.getKey();
               Object value = entry.getValue();
               System.out.println("key='" + key + "' type=" + key.getClass().getName());
               System.out.println("value='" + value + "' type=" + value.getClass().getName());
               
            }
         }
         else
         {
            System.out.println("value='" + print + "'");
         }
         System.out.println();
      }
   }
}
