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
package org.jboss.test.metatype.types.support;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jboss.metatype.api.types.TableMetaType;
import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.TableValue;

/**
 * MockTableValue.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MockTableValue extends MockMetaValue implements TableValue
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;
 
   /**
    * Create a new MockTableValue.
    * 
    * @param metaType the meta type
    */
   public MockTableValue(TableMetaType metaType)
   {
      super(metaType);
   }

   public TableMetaType getMetaType()
   {
      return (TableMetaType) super.getMetaType();
   }

   public MetaValue[] calculateIndex(CompositeValue value)
   {
      throw new org.jboss.util.NotImplementedException("calculateIndex");
   }

   public void clear()
   {
      throw new org.jboss.util.NotImplementedException("clear");
   }

   public boolean containsKey(MetaValue[] key)
   {
      throw new org.jboss.util.NotImplementedException("containsKey");
   }

   public boolean containsValue(CompositeValue value)
   {
      throw new org.jboss.util.NotImplementedException("containsValue");
   }

   public CompositeValue get(MetaValue[] key)
   {
      throw new org.jboss.util.NotImplementedException("get");
   }

   public boolean isEmpty()
   {
      throw new org.jboss.util.NotImplementedException("isEmpty");
   }

   public Set<List<MetaValue>> keySet()
   {
      throw new org.jboss.util.NotImplementedException("keySet");
   }

   public void put(CompositeValue value)
   {
      throw new org.jboss.util.NotImplementedException("put");
   }

   public void putAll(CompositeValue[] values)
   {
      throw new org.jboss.util.NotImplementedException("putAll");
   }

   public CompositeValue remove(MetaValue[] key)
   {
      throw new org.jboss.util.NotImplementedException("remove");
   }

   public int size()
   {
      throw new org.jboss.util.NotImplementedException("size");
   }

   public Collection<CompositeValue> values()
   {
      throw new org.jboss.util.NotImplementedException("values");
   }
}
