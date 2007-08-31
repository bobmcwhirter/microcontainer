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
package org.jboss.test.deployers.deployer.support;

import java.io.Serializable;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.managed.api.ManagedProperty;
import org.jboss.managed.spi.factory.InstanceClassFactory;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.MetaValueFactory;

/**
 * An InstanceClassFactory implementation for services
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class TestServiceMetaDataICF implements InstanceClassFactory
{
   /** The meta value factory */
   private MetaValueFactory metaValueFactory = MetaValueFactory.getInstance(); 

   public Class<? extends Serializable> getManagedObjectClass(Serializable instance)
      throws ClassNotFoundException
   {
      TestServiceMetaData md = (TestServiceMetaData) instance;
      ClassLoader loader = instance.getClass().getClassLoader();
      Class moClass = loader.loadClass(md.getCode());
      return moClass;
   }
   public MetaValue getValue(BeanInfo beanInfo, ManagedProperty property, Serializable instance)
   {
      TestServiceMetaData md = (TestServiceMetaData) instance;
      // First look to the mapped name
      String name = property.getMappedName();
      if (name == null)
         property.getName();

      Object value = null;
      for (TestServiceAttributeMetaData amd : md.getAttributes())
      {
         if (amd.getName().equals(name))
         {
            value = amd.getValue();
         }
      }

      PropertyInfo propertyInfo = beanInfo.getProperty(name);
      if (propertyInfo == null)
      {
         throw new IllegalStateException("Unable to find property: " + name
               + " for " + instance.getClass().getName());
      }

      return metaValueFactory.create(value, propertyInfo.getType());
   }

}
