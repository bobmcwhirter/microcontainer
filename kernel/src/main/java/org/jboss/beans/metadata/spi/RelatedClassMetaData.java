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
package org.jboss.beans.metadata.spi;

import java.util.Set;

/**
 * Metadata about a related class.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public interface RelatedClassMetaData extends MetaDataVisitorNode
{
   /**
    * Get the class name.
    *
    * @return the class name.
    */
   String getClassName();

   /**
    * Get the explictily enabled processing options.
    *
    * @return the enabled processing options
    */
   Set<Object> getEnabled();

   /**
    * Get the exact enabled processing option.
    *
    * @param <T> exact type
    * @param type the type we are looking for
    * @return exact type instance or null if no such match
    */
   <T> T getEnabled(Class<T> type);
}