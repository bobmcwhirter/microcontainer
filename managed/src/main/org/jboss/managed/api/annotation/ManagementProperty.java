/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.managed.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ManagementProperty annotation for describing a ManagedProperty
 * 
 * TODO: Need more info on meta-type such as constraints, allowed values.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ManagementProperty
{
   /** The description */
   String description() default ManagementConstants.GENERATED;

   /** The external name of the property. If undefined its taken
    * from the property the annotation is on.
    */
   String name() default "";

   /** The internal name of the property as it relates to metadata */
   String mappedName() default "";

   /** Whether this property is mandatory */
   boolean mandatory() default false;
   
   /** Whether to create a managed object for the property */
   boolean managed() default false;
   
   /** Whether to ignore this property */
   boolean ignored() default false;

   /** The views this property should be used in */
   ViewUse[] use() default {ViewUse.RUNTIME};

}
