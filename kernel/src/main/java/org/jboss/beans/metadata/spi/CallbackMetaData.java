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
package org.jboss.beans.metadata.spi;

import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;

/**
 * Metadata about a callback method that should be invoked on us when a bean of a specific type is
 * installed into the {@link Controller}.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public interface CallbackMetaData extends LifecycleMetaData
{
   /**
    * Get the cardinality. This is the number of beans as a range of a certain type that must have been registered
    * with the {@link Controller} before invoking the callback method/property/attribute specified 
    * by a {@link #getProperty()} for that type on a bean's {@link ControllerContext}.
    *
    * @return cardinality
    */
   Cardinality getCardinality();

   /**
    * Get the name of the property that should be invoked when triggering the callback. Alternatively,
    * a method should be called, specified by {@link #getMethodName()}.
    *
    * @return the property name.
    */
   String getProperty();

   /**
    * Get the required state of the items we are listening for. The default is {@link ControllerState#INSTALLED}.
    *
    * @return the required state
    */
   ControllerState getDependentState();

   /**
    * Get signature of the method/property. This is required if overloading has been used.
    *
    * @return method / property parameter signature
    */
   String getSignature();
}
