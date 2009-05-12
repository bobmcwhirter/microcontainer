/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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

import org.jboss.kernel.spi.dependency.DependencyBuilder;
import org.jboss.kernel.spi.dependency.helpers.AbstractDependencyBuilder;

/**
 * Define dependency builder instance.
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DependencyBuilderFactory
{
   /**
    * The dependency builder class to use.
    *
    * @return the dependency builder class
    */
   Class<? extends DependencyBuilder> value() default AbstractDependencyBuilder.class;

   /**
    * Should we check metadata for dependency builder instance.
    * This allows us for more configurable instance.
    *
    * @return true if we should check metadata for dependency builder, false otherwise
    */
   boolean checkMetaDataForBuilderInstance() default false;
}