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
package org.jboss.beans.metadata.plugins.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * The value factory.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface ValueFactory
{
   /**
    * Get the bean.
    *
    * @return the bean
    */
   String bean();

   /**
    * Get the method.
    *
    * @return the method
    */
   String method();

   /**
    * Get single parameter.
    *
    * @return the single parameter
    */
   String parameter() default "";

   /**
    * Get parameters.
    *
    * @return the parameters
    */
   Parameter[] parameters() default {};

   /**
    * Get default value.
    *
    * @return the default value
    */
   String defaultValue() default "";

   /**
    * Get dependant state.
    *
    * @return the dependant state
    */
   String dependantState() default "Installed";

   /**
    * Get when required state.
    *
    * @return the when required state
    */
   String whenRequiredState() default "Configured";
}
