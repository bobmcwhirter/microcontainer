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
package org.jboss.test.kernel.config.support;

import java.util.Map;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class LDAPFactory
{
   private Map<String, String> map;

   public LDAPFactory(Map<String, String> map)
   {
      if (map == null)
         throw new IllegalArgumentException("Map is null.");
      this.map = map;
   }

   public String getValue(String key)
   {
      return getValue(key, null);
   }

   public String getValue(String key, String def)
   {
      return getValue(key, def, null);
   }

   public String getValue(String key, String def, Transformer<String> transformer)
   {
      if (transformer == null)
         transformer = new Transformer<String>()
         {
            public String transform(String value)
            {
               return value;
            }
         };
      String value = map.get(key);
      if (value == null)
         value = def;
      return transformer.transform(value);
   }
}
