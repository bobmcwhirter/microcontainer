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
package org.jboss.classloading.spi.metadata.helpers;

import org.jboss.classloading.spi.helpers.NameAndVersionSupport;
import org.jboss.classloading.spi.metadata.Capability;

/**
 * AbstractCapability.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractCapability extends NameAndVersionSupport implements Capability
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1099174558119868306L;
   
   /**
    * Create a new AbstractCapability
    */
   public AbstractCapability()
   {
   }
   
   /**
    * Create a new AbstractCapability
    * 
    * @param name the name
    * @throws IllegalArgumentException for a null name
    */
   public AbstractCapability(String name)
   {
      super(name);
   }
   
   /**
    * Create a new AbstractCapability.
    * 
    * @param name the name
    * @param version the version - pass null for the default version
    * @throws IllegalArgumentException for a null name
    */
   public AbstractCapability(String name, Object version)
   {
      super(name, version);
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (obj == null || obj instanceof AbstractCapability == false)
         return false;
      return super.equals(obj);
   }

   @Override
   public String toString()
   {
      StringBuffer buffer = new StringBuffer();
      buffer.append(getClass().getSimpleName());
      buffer.append("{");
      toString(buffer);
      buffer.append("}");
      return buffer.toString();
   }
   
   /**
    * For subclasses to override toString()
    * 
    * @param buffer the buffer
    */
   protected void toString(StringBuffer buffer)
   {
      buffer.append(getName());
      buffer.append(" ").append(getVersion());
   }
}
