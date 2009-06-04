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
 * Creates a set that can be used as a parameter
 * <pre>
 * &#64;SetValue(elementClass="java.lang.String", 
 *                 clazz="org.jboss.example.CustomSet",  
 *                 {&#64;Value(string=&#64;StringValue("string1")), 
 *                 &#64;Value(string=&#64;StringValue("string2")), 
 *                 &#64;Value(string=&#64;StringValue("string3")), 
 *                 &#64;Value(string=&#64;StringValue("string4"))}) 
 * public void setSet(Set collection) {} 
 * </pre>
 * creates a Set of the type <code>org.jboss.example.CustomSet</code>, where all the elements are of type String.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
public @interface SetValue
{
   /**
    * Get the set class. If unspecified, the default is java.util.HashSet.
    *
    * @return the set class
    */
   Class<?> clazz() default void.class;

   /**
    * Get the element class.
    *
    * @return the element class
    */
   Class<?> elementClass() default void.class;

   /**
    * Get the array of the Values in the set.
    *
    * @return the values
    */
   Value[] value();
}
