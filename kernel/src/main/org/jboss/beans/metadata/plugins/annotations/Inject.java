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
package org.jboss.beans.metadata.plugins.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Beans when injected by class type are by default changed to configured
 * state - if not yet configured.
 * You can change this behavior by setting state.
 *
 * @author <a href="mailto:ales.justin@genera-lynx.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR})
public @interface Inject
{
   /**
    * Get bean.
    * Default is no bean.
    *
    * @return bean name
    */
   String bean() default "";

   /**
    * Get property.
    * Default is no property.
    *
    * @return property name
    */
   String property() default "";

   /**
    * Get when required.
    *
    * @return when required.
    */
   String whenRequired() default "";

   /**
    * Get dependent state.
    * Default is Installed.
    *
    * @return dependent state.
    */
   String dependentState() default "Installed";

   /**
    * Get injection type.
    * Default is by class.
    *
    * @return injection type
    */
   InjectType type() default InjectType.BY_CLASS;

   /**
    * Get injection option.
    * Default is Strict.
    *
    * @return injection option
    */
   InjectOption option() default InjectOption.STRICT;

   /**
    * Get from context injection.
    *
    * @return from context type
    */
   FromContext fromContext() default FromContext.NONE;

   /**
    * Is this @Inject valid.
    * Used with @Value.
    *
    * @return is this instance valid
    */
   boolean valid() default true;
}
