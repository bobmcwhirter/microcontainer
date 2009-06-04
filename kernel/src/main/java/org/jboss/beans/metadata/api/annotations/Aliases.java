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
 * Used to create aliases for a bean. An alias is an alternative name for a bean within the controller
 * Equivalent to deployment's alias element.
 *
 * For example this configuration:
 * <pre>
 * &#64;Bean(name="SomeBean")
 * &#64;Aliases({"Red", "Blue"})
 * public class MyBean
 * {
 * }
 * </pre>
 * Creates an instance of MyBean and installs it in the Microcontainer using the name <code>MyBean</code>,
 * and registers the aliases <code>Red</code> and <code>Blue</code>.
 * Other beans can inject it using <code>MyBean</code>, <code>Red</code> and <code>Blue</code>.
 *
 * @see org.jboss.beans.metadata.spi.BeanMetaData#getAliases()
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Cleanup
public @interface Aliases
{
   /**
    * Get aliases array.
    *
    * @return the aliases
    */
   String[] value();

   /**
    * Do system property replacement
    *
    * @return true to replace system property, false otherwise
    */
   boolean replace() default true;
}
