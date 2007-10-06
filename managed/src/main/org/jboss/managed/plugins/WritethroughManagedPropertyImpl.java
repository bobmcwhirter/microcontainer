/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.managed.plugins;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;

import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.managed.api.Fields;
import org.jboss.managed.api.ManagedObject;
import org.jboss.metatype.api.values.MetaValue;

/**
 * An extension of ManagedPropertyImpl.
 *
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class WritethroughManagedPropertyImpl extends ManagedPropertyImpl
{
   private static final long serialVersionUID = 1;

   public WritethroughManagedPropertyImpl(String name)
   {
      super(name);
   }

   public WritethroughManagedPropertyImpl(Fields fields)
   {
      super(fields);
   }

   public WritethroughManagedPropertyImpl(ManagedObject managedObject, Fields fields)
   {
      super(managedObject, fields);
   }

   /**
    * Write the value back to the attachment if there is a PropertyInfo
    * in the Fields.PROPERTY_INFO field.
    * TODO: this ignored MetaValues as the tests pass in the corresponding
    * primative
    */
   @Override
   public void setValue(Serializable value)
   {
      super.setValue(value);
      // Skip MetaValues
      if( (value instanceof MetaValue) )
         return;

      // Write the value back to the attachment if there is a PropertyInfo
      PropertyInfo info = super.getField(Fields.PROPERTY_INFO, PropertyInfo.class);
      if (info != null)
      {
         Object bean = getManagedObject().getAttachment();
         try
         {
            info.set(bean, value);
         }
         catch(Throwable t)
         {
            throw new UndeclaredThrowableException(t);
         }
      }
   }

   /**
    * Expose only plain ManangedPropertyImpl.
    *
    * @return simpler ManagedPropertyImpl
    * @throws ObjectStreamException for any error
    */
   private Object writeReplace() throws ObjectStreamException
   {
      return new ManagedPropertyImpl(getManagedObject(), getFields());
   }
}
