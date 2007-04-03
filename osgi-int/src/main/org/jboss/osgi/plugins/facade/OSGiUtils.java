/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.osgi.plugins.facade;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

/**
 * Helpful methods for handling specific OSGi cases.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class OSGiUtils
{
   /**
    * Transform from Dictionary to Map
    *
    * @param dictionary dictionary to transform
    * @return Map<String, Object>
    */
   public static Map<String, Object> toMap(Dictionary dictionary)
   {
      Map<String, Object> map = new HashMap<String, Object>();
      if (dictionary != null && dictionary.size() > 0)
      {
         Enumeration keys = dictionary.keys();
         while(keys.hasMoreElements())
         {
            String key = keys.nextElement().toString();
            Object value = dictionary.get(key);
            map.put(key, value);
         }
      }
      return map;
   }

   /**
    * Get ServiceReference id.
    *
    * @param reference service reference to look for id
    * @return reference's id or error if no such property
    */
   public static Long getServiceId(ServiceReference reference)
   {
      Object value = reference.getProperty(Constants.SERVICE_RANKING);
      if (value == null || value instanceof Long == false)
      {
         throw new IllegalArgumentException("No such property: " + Constants.SERVICE_ID);
      }
      else
      {
         return (Long)value;
      }
   }

   /**
    * Get ServiceReference ranking.
    *
    * @param reference service reference to look for ranking
    * @return reference's ranking or 0 if no such property
    */
   public static Integer getServiceRanking(ServiceReference reference)
   {
      Object value = reference.getProperty(Constants.SERVICE_RANKING);
      if (value == null || value instanceof Integer == false)
      {
         return 0;
      }
      else
      {
         return (Integer)value;
      }
   }
}
