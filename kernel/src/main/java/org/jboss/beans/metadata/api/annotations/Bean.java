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
import org.jboss.beans.metadata.api.model.AutowireType;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ErrorHandlingMode;

/**
 * Mark the MC bean.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Bean
{
   /**
    * Get the name.
    *
    * This is only meant to be used before
    * bean meta data is already installed
    * in Controller.
    *
    * @return bean's name
    */
   String name() default "";

   /**
    * Get the aliases.
    *
    * This is only meant to be used before
    * bean meta data is already installed
    * in Controller.
    *
    * @return the aliases
    */
   String[] aliases() default {};

   /**
    * Get the parent.
    *
    * This is only meant to be used before
    * bean meta data is already installed
    * in Controller.
    *
    * @return the parent
    */
   String parent() default "";

   /**
    * Is abstract metadata.
    *
    * This is only meant to be used before
    * bean meta data is already installed
    * in Controller.
    *
    * @return is abstract
    */
   boolean isAbstract() default false;

   /**
    * Get the autowire type.
    *
    * @return the autowire type
    */
   AutowireType autowireType() default AutowireType.NONE;

   /**
    * Get the mode
    *
    * Note that this is only gonna be
    * used after Describe state.
    *
    * @return the mode
    */
   ControllerMode mode() default ControllerMode.AUTOMATIC;

   /**
    * Get error handling mode
    *
    * Note that this is only gonna be
    * used after Describe state.
    *
    * @return the error handling mode
    */
   ErrorHandlingMode errorHandlingMode() default ErrorHandlingMode.DISCARD;

   /**
    * Get the access mode
    *
    * @return the access mode
    */
   BeanAccessMode accessMode() default BeanAccessMode.STANDARD;

   /**
    * Is this bean is a candidate for
    * getting injected via contextual matching
    * or callback resolution.
    *
    * This is only meant to be used before
    * bean meta data is already installed
    * in Controller.
    *
    * @return true (default) if used for autowiring
    */
   boolean autowireCandidate() default true;
}