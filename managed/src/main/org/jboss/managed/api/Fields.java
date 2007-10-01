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

/**
 * Fields.
 * 
 * TODO the fields names should be annotation class names (where relevant) when the annotations exist
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface Fields extends Serializable
{
   /** The name field name */
   String NAME = "name";

   /** The mapped name field name */
   String MAPPED_NAME = "mappedName";

   /** The description field name */
   String DESCRIPTION = "description";

   /** The meta type field name */
   String META_TYPE = "metaType";

   /** The value */
   String VALUE = "value";

   /** The legal values */
   String LEGAL_VALUES = "legalValues";

   /** The minimum value */
   String MINIMUM_VALUE = "minValue";

   /** The maximum value */
   String MAXIMUM_VALUE = "maxValue";

   /** The mandatory */
   String MANDATORY = "mandatory";
   
   /** The PropertyInfo info for the ManagedObject attachment */
   String PROPERTY_INFO = "propertyInfo";

   /** The annotations associated with the property */
   String ANNOTATIONS = "annotations";

   /** The attachment */
   String ATTACHMENT = "attachment";

   // TODO other standard fields here

   /**
    * Get a field with the given name
    * 
    * @param name the name
    * @return the field value
    */
   Serializable getField(String name);
   
   /**
    * Set a field with the given name
    * 
    * @param name the name
    * @param value the field value
    */
   void setField(String name, Serializable value);
}
