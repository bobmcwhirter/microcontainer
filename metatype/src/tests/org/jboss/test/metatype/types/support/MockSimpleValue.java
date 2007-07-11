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

import java.io.Serializable;

import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.values.SimpleValue;

/**
 * MockSimpleValue.
 * 
 * @param <T> the underlying type
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MockSimpleValue<T extends Serializable> extends MockMetaValue implements SimpleValue<T>
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;

   /**
    * Create a new MockSimpleValue.
    * 
    * @param metaType the meta type
    */
   public MockSimpleValue(SimpleMetaType<T> metaType)
   {
      super(metaType);
   }

   public SimpleMetaType<T> getMetaType()
   {
      return (SimpleMetaType<T>) super.getMetaType();
   }

   public T getValue()
   {
      throw new org.jboss.util.NotImplementedException("getValue");
   }
}
