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

import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.metatype.api.values.MetaValue;

/**
 * MockCompositeValue.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MockCompositeValue extends MockMetaValue implements CompositeValue
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;

   public MockCompositeValue(CompositeMetaType metaType)
   {
      super(metaType);
   }
   
   public CompositeMetaType getMetaType()
   {
      return (CompositeMetaType) super.getMetaType();
   }

   public boolean containsKey(String key)
   {
      throw new org.jboss.util.NotImplementedException("containsKey");
   }

   public boolean containsValue(MetaValue value)
   {
      throw new org.jboss.util.NotImplementedException("containsValue");
   }

   public MetaValue get(String key)
   {
      throw new org.jboss.util.NotImplementedException("get");
   }

   public MetaValue[] getAll(String[] keys)
   {
      throw new org.jboss.util.NotImplementedException("getAll");
   }

   public Collection<MetaValue> values()
   {
      throw new org.jboss.util.NotImplementedException("values");
   }
}
