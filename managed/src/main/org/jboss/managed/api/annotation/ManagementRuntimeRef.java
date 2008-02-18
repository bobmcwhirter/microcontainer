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
package org.jboss.managed.api.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.jboss.managed.spi.factory.RuntimeComponentNameTransformer;

/**
 * ManagementRuntimeRef annotation that can be used
 * to identify which property defines the component name.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ManagementRuntimeRef
{
   /**
    * The name transformer.
    *
    * @return the transformer class
    */
   Class<? extends RuntimeComponentNameTransformer> transformer() default DEFAULT_NAME_TRANSFORMER.class;

   /**
    * Used in {@link ManagementRuntimeRef#transformer()} to
    * indicate that no name transformation is defined.
    */
   public static final class DEFAULT_NAME_TRANSFORMER implements RuntimeComponentNameTransformer
   {
      public Object transform(Object value)
      {
         return value;
      }
   }
}
