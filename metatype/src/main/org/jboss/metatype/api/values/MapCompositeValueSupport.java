/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.metatype.api.values;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.MapCompositeMetaType;
import org.jboss.metatype.api.types.MetaType;

/**
 * A CompositeValue for Map<String,MetaValue> 
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class MapCompositeValueSupport extends AbstractMetaValue implements CompositeValue
{
   private static final long serialVersionUID = 1;
   private Map<String, MetaValue> map;
   private MapCompositeMetaType mapType;

   public MapCompositeValueSupport(MetaType<?> valueType)
   {
      this(null, valueType);
   }

   public MapCompositeValueSupport(Map<String, MetaValue> map, MetaType<?> valueType)
   {
      this.map = createMap();
      this.mapType = new MapCompositeMetaType(valueType);
      if(map != null)
      {
         for(Map.Entry<String, MetaValue> entry : map.entrySet())
            this.put(entry.getKey(), entry.getValue());
      }
   }

   /**
    * Create map instance.
    * Default is hash map.
    *
    * @return the map
    */
   protected Map<String, MetaValue> createMap()
   {
      return new HashMap<String, MetaValue>();
   }

   public boolean containsKey(String key)
   {
      return map.containsKey(key);
   }

   public boolean containsValue(MetaValue value)
   {
      return map.containsValue(value);
   }

   public MetaValue get(String key)
   {
      return map.get(key);
   }

   public void put(String key, MetaValue value)
   {
      if(mapType.containsItem(key) == false)
         mapType.addItem(key);
      map.put(key, value);
   }

   public MetaValue[] getAll(String[] keys)
   {
      List<MetaValue> values = new ArrayList<MetaValue>();
      if(keys != null)
      {
         for(String key : keys)
         {
            MetaValue value = map.get(key);
            values.add(value);
         }
      }
      MetaValue[] mvs = new MetaValue[values.size()];
      return values.toArray(mvs);
   }

   public CompositeMetaType getMetaType()
   {
      return mapType;
   }

   public Collection<MetaValue> values()
   {
      return map.values();
   }
}
