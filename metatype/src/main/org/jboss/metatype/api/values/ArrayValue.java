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
package org.jboss.metatype.api.values;

import java.io.Serializable;

import org.jboss.metatype.api.types.ArrayMetaType;

/**
 * ArrayValue.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.1 $
 */
public interface ArrayValue<T extends Serializable>
   extends MetaValue, Iterable<T>
{
   ArrayMetaType<T> getMetaType();
   
   /**
    * Get the underlying array value. This will not be an
    * Object[] in general.
    * @see #getValue(int)
    * 
    * @return the underlying value
    */
   public Object getValue();
   /**
    * Get the length of the array.
    * @return length of the array.
    */
   public int getLength();
   /**
    * Get the array element at index.
    * @param index - index into the array.
    * @return element at index.
    */
   public Object getValue(int index); 
}
