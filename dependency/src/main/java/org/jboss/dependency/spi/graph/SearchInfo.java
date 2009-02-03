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
package org.jboss.dependency.spi.graph;

import java.util.Map;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Search info.
 *
 * Holding the type or/and lookup strategy.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface SearchInfo
{
   /**
    * The search type.
    *
    * If there is no supported lookup strategy
    * GraphController implementation might
    * return context on itself.
    *
    * @return the type
    */
   @XmlTransient
   String getType();

   /**
    * Get the info.
    * Additional parameters to be used for search.
    *
    * @return the additional info
    */
   @XmlTransient
   Map<String, ?> getInfo();

   /**
    * Get the lookup strategy.
    *
    * Might be null, but then we expect the GraphController
    * to implement this search type.
    * Otherwise error should be thown by GraphController.
    *
    * @return the strategy or null depending on GraphContorller
    */
   @XmlTransient
   LookupStrategy getStrategy();
}
