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
package org.jboss.test.metatype.values.factory.support;

import org.jboss.metatype.api.annotations.CompositeKey;

/**
 * TestRecursiveComposite.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class TestRecursiveComposite
{
   private String id;
   
   private TestRecursiveComposite other;

   /**
    * Create a new TestRecursiveComposite.
    * 
    * @param id
    */
   public TestRecursiveComposite(String id)
   {
      this.id = id;
   }

   /**
    * Get the id.
    * 
    * @return the id.
    */
   @CompositeKey
   public String getId()
   {
      return id;
   }

   /**
    * Set the id.
    * 
    * @param id the id.
    */
   public void setId(String id)
   {
      this.id = id;
   }

   /**
    * Get the other.
    * 
    * @return the other.
    */
   public TestRecursiveComposite getOther()
   {
      return other;
   }

   /**
    * Set the other.
    * 
    * @param other the other.
    */
   public void setOther(TestRecursiveComposite other)
   {
      this.other = other;
   }
}
