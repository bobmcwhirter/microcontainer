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
package org.jboss.deployers.spi.metadata;

/**
 * Manifest meta data model.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface ManifestMetaData
{
   /**
    * Get the manifest attribute value.
    *
    * @param name the name
    * @return attribute value or null if attribute doesn't exist
    */
   String getMainAttribute(String name);

   /**
    * Get attribute value from specific attrbiutes.
    * 
    * @param attributesName attribute group name
    * @param name string key for accessing specific attribute
    * @return attribute value or null if attribute doesn't exist
    */
   String getAttribute(String attributesName, String name);

   /**
    * Get attribute value from specific attrbiutes.
    *
    * @param entryName entry name
    * @param name string key for accessing specific attribute
    * @return attribute value or null if attribute doesn't exist
    */
   String getEntry(String entryName, String name);
}
