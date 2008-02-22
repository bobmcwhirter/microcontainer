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
package org.jboss.test.metatype.types.factory.support;

import org.jboss.metatype.api.annotations.CompositeKey;
import org.jboss.metatype.api.annotations.CompositeValue;

/**
 * TestIgnoredCompositeItem.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class TestIgnoredCompositeItem
{
   /** The id */
   private String id;
   
   /** The ignored */
   private Object ignored;
   
   /**
    * Get the id
    * 
    * @return the id
    */
   @CompositeKey
   public String getId()
   {
      return id;
   }

   /**
    * Set the id
    * 
    * @param id the id
    */
   public void setId(String id)
   {
      this.id = id;
   }

   /**
    * Get the ignored.
    * 
    * @return the ignored.
    */
   public Object getIgnored()
   {
      return ignored;
   }

   /**
    * Set the ignored.
    * 
    * @param ignored the ignored.
    */
   @CompositeValue(ignore=true)
   public void setIgnored(Object ignored)
   {
      this.ignored = ignored;
   }
}
