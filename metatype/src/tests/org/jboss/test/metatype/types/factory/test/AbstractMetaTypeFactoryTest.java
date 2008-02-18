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
package org.jboss.test.metatype.types.factory.test;

import java.lang.reflect.Type;

import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.MetaTypeFactory;
import org.jboss.metatype.spi.types.MetaTypeBuilder;
import org.jboss.test.metatype.AbstractMetaTypeTest;

/**
 * AbstractMetaTypeFactoryTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractMetaTypeFactoryTest extends AbstractMetaTypeTest
{
   /** The metatype factory */
   private static final MetaTypeFactory metaTypeFactory = MetaTypeFactory.getInstance();
   
   /**
    * Create a new AbstractMetaTypeFactoryTest.
    * 
    * @param name the test name
    */
   public AbstractMetaTypeFactoryTest(String name)
   {
      super(name);
   }
   
   /**
    * Set a builder
    * 
    * @param clazz the clazz
    * @param builder the builder
    */
   protected void setBuilder(Class<?> clazz, MetaTypeBuilder builder)
   {
      metaTypeFactory.setBuilder(clazz, builder);
   }

   /**
    * Resolve a metatype
    * 
    * @param type the type
    * @return the meta type
    */
   protected MetaType<?> resolve(Type type)
   {
      return metaTypeFactory.resolve(type);
   }
}
