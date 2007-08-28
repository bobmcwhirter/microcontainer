/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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

import org.jboss.managed.spi.factory.ManagedParameterConstraintsPopulator;
import org.jboss.managed.spi.factory.ManagedParameterConstraintsPopulatorFactory;

/**
 * Annotation for documenting a ManagementOperation parameter.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public @interface ManagementParameter
{
   /** The parameter name */
   String name() default AnnotationDefaults.EMPTY_STRING;
   /** The parameter description */
   String description() default AnnotationDefaults.EMPTY_STRING;

   /** The constraints, allowed values populator factory */
   Class<? extends ManagedParameterConstraintsPopulatorFactory> constraintsFactory()
      default NULL_CONSTRAINTS.class;

   /**
    * Used in {@link ManagementParameter#constraintsFactory()} to
    * signal that the factory be inferred from the type
    * of the property.
    */
   public static final class NULL_CONSTRAINTS
      implements ManagedParameterConstraintsPopulatorFactory
   {
      public ManagedParameterConstraintsPopulator newInstance()
      {
         return null;
      }
   }

}
