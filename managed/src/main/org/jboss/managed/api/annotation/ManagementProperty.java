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
import org.jboss.managed.spi.factory.InstanceClassFactory;
import org.jboss.managed.spi.factory.ManagedPropertyConstraintsPopulator;
import org.jboss.managed.spi.factory.ManagedPropertyConstraintsPopulatorFactory;

/**
 * ManagementProperty annotation for describing a ManagedProperty
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
   String name() default AnnotationDefaults.EMPTY_STRING;

   /** The internal name of the property as it relates to metadata */
   String mappedName() default AnnotationDefaults.EMPTY_STRING;

   /** Whether this property is mandatory */
   boolean mandatory() default false;
   
   /** Whether to create a managed object for the property */
   boolean managed() default false;
   
   /** Whether to ignore this property */
   boolean ignored() default false;

   /** Whether this property should be included in a deployment template */
   boolean includeInTemplate() default false;

   /** The views this property should be used in */
   ViewUse[] use() default {ViewUse.CONFIGURATION};

   /** The class to use for the ManagedProperty implementation */
   Class<? extends ManagedProperty> propertyFactory() default NULL_PROPERTY_FACTORY.class;
   /** The class to use for the ManagedProperty Fields implementation */
   Class<? extends Fields> fieldsFactory() default NULL_FIELDS_FACTORY.class;
   /** The constraints, allowed values populator factory */
   Class<? extends ManagedPropertyConstraintsPopulatorFactory> constraintsFactory() default NULL_CONSTRAINTS.class;
   /** The constraints, allowed values populator factory */
   Class<? extends InstanceClassFactory> marshallerFactory() default NULL_MARSHALLER_FACTORY.class;

   /**
    * Used in {@link ManagementProperty#constraintsFactory()} to
    * signal that the factory be inferred from the type
    * of the property.
    */
   public static final class NULL_CONSTRAINTS implements ManagedPropertyConstraintsPopulatorFactory
   {
      public ManagedPropertyConstraintsPopulator newInstance()
      {
         return null;
      }
   }

   /**
    * Used in {@link ManagementProperty#fieldsFactory()} to
    * indicate that no Fields factory is defined.
    */
   public static abstract class NULL_FIELDS_FACTORY implements Fields
   {
   }

   /**
    * Used in {@link ManagementProperty#propertyFactory()} to
    * indicate that no ManagedProperty factory is defined.
    */
   public static abstract class NULL_PROPERTY_FACTORY implements ManagedProperty
   {
   }

   /**
    * Used in {@link ManagementProperty#propertyFactory()} to
    * indicate that no ManagedProperty factory is defined.
    */
   public static abstract class NULL_MARSHALLER_FACTORY implements InstanceClassFactory
   {
   }
}
