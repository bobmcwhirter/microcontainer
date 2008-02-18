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
package org.jboss.managed.api;

import java.io.Serializable;

import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.values.MetaValue;

/**
 * A representation of a managed operation.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public interface ManagedOperation extends Serializable
{
   /**
    * The side-effect impact of invoking an operation
    */
   public enum Impact {
      /** There is not modification of the ManagedObject */
      ReadOnly,
      /** The impact may modify the ManagedObject */
      ReadWrite,
      /** The impact is to modify the ManagedObject */
      WriteOnly,
      /** The impact is not known */
      Unknown
   };

   /**
    * Get the operation description
    * @return the operation description
    */
   public String getDescription();
   /**
    * Get the name of the operation
    * @return the name of the operation
    */
   public String getName();

   /**
    * Get the impact of the operation
    * @return the side-effect type invoking the operation has.
    */
   public Impact getImpact();

   /**
    * The MetaType for the operation return value.
    * @return MetaType for the operation return value.
    */
   public MetaType<?> getReturnType();

   /**
    * The parameter information for the operation arguments. An empty
    * signature array is returned if the operation takes no arguments.
    * @return parameter information for the operation arguments.
    */
   public ManagedParameter[] getParameters();

   /**
    * Invoke the operation given its parameter values.
    * 
    * @param param the varags for the operation parameters.
    * @return the MetaValue for the result.
    */
   public Object invoke(MetaValue... param);
}
