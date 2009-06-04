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
package org.jboss.beans.metadata.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Creates an array that can be used as a parameter.
 * 
 * <pre>
 * &#64;ArrayValue(elementClass="java.lang.Float", 
 *            {&#64;Value(string=&#64;StringValue("1.0")), 
 *            &#64;Value(string=&#64;StringValue("2.0"))}) 
 * public void setArray(Float[] array) {}
 * </pre>
 * creates a Float array with the entries {1.0, 2.0}
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface ArrayValue
{
   /**
    * Set the array class.
    *
    * @return array class
    */
   Class<?> clazz() default void.class;

   /**
    * Array's element class. If absent, the default is Object.
    *
    * @return element class
    */
   Class<?> elementClass() default void.class;

   /**
    * Get the values.
    *
    * @return the values
    */
   Value[] value();
}
