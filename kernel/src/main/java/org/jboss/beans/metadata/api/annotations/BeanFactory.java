/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.beans.metadata.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.dependency.spi.ControllerMode;

/**
 * Mark the MC bean factory.
 *
 * Only useful before actual metadata instantiation,
 * since we need to instantiate GenericBeanFactoryMetaData
 * and not BeanMetaData.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface BeanFactory
{
   /**
    * Get the name.
    *
    * @return bean's name
    */
   String name() default "";

   /**
    * Get factory class.
    *
    * @return factory class
    */
   Class<?> getFactoryClass() default void.class;

   /**
    * Get the aliases.
    *
    * @return the aliases
    */
   String[] aliases() default {};

   /**
    * Get the mode
    *
    * @return the mode
    */
   ControllerMode mode() default ControllerMode.AUTOMATIC;

   /**
    * Get the access mode
    *
    * @return the access mode
    */
   BeanAccessMode accessMode() default BeanAccessMode.STANDARD;
}