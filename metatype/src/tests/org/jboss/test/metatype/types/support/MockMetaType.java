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

import org.jboss.metatype.api.types.AbstractMetaType;

/**
 * MockMetaType.
 * 
 * @param <T> the underlying type
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MockMetaType<T extends Serializable> extends AbstractMetaType<T>
{
   private static final long serialVersionUID = -1;

   /**
    * Create a new MockMetaType.
    * 
    * @param className the class name
    * @param typeName the type name
    * @param description the description
    */
   public MockMetaType(String className, String typeName, String description)
   {
      super(className, typeName, description);
   }

   @Override
   public boolean equals(Object obj)
   {
      throw new org.jboss.util.NotImplementedException("equals");
   }

   @Override
   public int hashCode()
   {
      throw new org.jboss.util.NotImplementedException("hashCode");
   }

   @Override
   public boolean isValue(Object obj)
   {
      throw new org.jboss.util.NotImplementedException("isValue");
   }

   @Override
   public String toString()
   {
      throw new org.jboss.util.NotImplementedException("toString");
   }

}
