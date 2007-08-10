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

import org.jboss.managed.api.Fields;
import org.jboss.managed.api.ManagedProperty;
import org.jboss.managed.api.annotation.ManagementProperty.NULL_CONSTRAINTS;
import org.jboss.managed.api.annotation.ManagementProperty.NULL_FIELDS_FACTORY;
import org.jboss.managed.api.annotation.ManagementProperty.NULL_PROPERTY_FACTORY;
import org.jboss.managed.spi.factory.ManagedPropertyConstraintsPopulatorFactory;

/**
 * ManagementObject annotation for describing ManagedObjects.
 * @see {@linkplain ManagedObject}
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.1 $
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ManagementObject
{
   /** The name used for ManagementObjectRef resolution */
   String name() default ManagementConstants.GENERATED;
   /** The name type used for ManagementObjectRef resolution */
   String type() default "";
   /** The metadata attachment name for the ManagedObject */
   String attachmentName() default "";

   /** What properties to include */
   ManagementProperties properties() default ManagementProperties.ALL;

   /** The exposed operations */
   ManagementOperation[] operations() default {};

   /** The class to use for the ManagedProperty implementation */
   Class<? extends ManagedProperty> propertyFactory() default NULL_PROPERTY_FACTORY.class;
   /** The class to use for the ManagedProperty Fields implementation */
   Class<? extends Fields> fieldsFactory() default NULL_FIELDS_FACTORY.class;
   /** The constraints, allowed values populator factory */
   Class<? extends ManagedPropertyConstraintsPopulatorFactory> constraintsFactory()
      default NULL_CONSTRAINTS.class;

}
