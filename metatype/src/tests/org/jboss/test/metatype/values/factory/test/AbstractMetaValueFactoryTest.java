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
package org.jboss.test.metatype.values.factory.test;

import java.lang.reflect.Type;

import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.MetaTypeFactory;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.MetaValueFactory;
import org.jboss.metatype.spi.types.MetaTypeBuilder;
import org.jboss.metatype.spi.values.MetaValueBuilder;
import org.jboss.test.metatype.AbstractMetaTypeTest;

/**
 * AbstractMetaValueFactoryTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractMetaValueFactoryTest extends AbstractMetaTypeTest
{
   /** The metatype factory */
   private static final MetaTypeFactory metaTypeFactory = MetaTypeFactory.getInstance();

   /** The meta value factory */
   private static final MetaValueFactory metaValueFactory = MetaValueFactory.getInstance();
   
   /**
    * Create a new AbstractMetaValueFactoryTest.
    * 
    * @param name the test name
    */
   public AbstractMetaValueFactoryTest(String name)
   {
      super(name);
   }
   
   /**
    * Set a builder
    * 
    * @param clazz the clazz
    * @param builder the builder
    */
   protected void setBuilder(Class<?> clazz, MetaValueBuilder<?> builder)
   {
      metaTypeFactory.setBuilder(clazz, (MetaTypeBuilder) builder);
      metaValueFactory.setBuilder(clazz, builder);
   }
   
   /**
    * Resolve a meta type
    * 
    * @param type the type
    * @return the meta type
    */
   protected MetaType<?> resolve(Type type)
   {
      return metaTypeFactory.resolve(type);
   }
   
   /**
    * Create a meta value
    * 
    * @param value the value
    * @return the meta value
    */
   protected MetaValue createMetaValue(Object value)
   {
      return metaValueFactory.create(value);
   }
   
   /**
    * Create a meta value
    * 
    * @param value the value
    * @param type the type
    * @return the meta value
    */
   protected MetaValue createMetaValue(Object value, Type type)
   {
      return metaValueFactory.create(value, type);
   }

   /**
    * Unwrap meta value.
    *
    * @param value the meta value
    * @return unwrapped 
    */
   protected Object unwrapMetaValue(MetaValue value)
   {
      return metaValueFactory.unwrap(value);
   }

   /**
    * Unwrap meta value.
    *
    * @param value the meta value
    * @param type expecyed type
    * @return unwrapped
    */
   protected Object unwrapMetaValue(MetaValue value, Type type)
   {
      return metaValueFactory.unwrap(value, type);
   }
}
