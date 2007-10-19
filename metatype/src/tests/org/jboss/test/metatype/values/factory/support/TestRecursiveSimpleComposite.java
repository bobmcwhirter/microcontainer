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
package org.jboss.test.metatype.values.factory.support;

/**
 * TestSimpleComposite.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class TestRecursiveSimpleComposite
{
   private String something;
   private TestSimpleComposite composite;

   /**
    * Default constructor.
    * Needed with unwrap.
    */
   public TestRecursiveSimpleComposite()
   {
   }

   /**
    * Create a new TestSimpleComposite.
    *
    * @param something the something
    * @param composite the composite
    */
   public TestRecursiveSimpleComposite(String something, TestSimpleComposite composite)
   {
      this.something = something;
      this.composite = composite;
   }

   /**
    * Get the something.
    *
    * @return the something.
    */
   public String getSomething()
   {
      return something;
   }

   /**
    * Set the something.
    *
    * @param something the something.
    */
   public void setSomething(String something)
   {
      this.something = something;
   }

   /**
    * Get the composite.
    *
    * @return the composite
    */
   public TestSimpleComposite getComposite()
   {
      return composite;
   }

   /**
    * Set the composite.
    *
    * @param composite the composite
    */
   public void setComposite(TestSimpleComposite composite)
   {
      this.composite = composite;
   }

   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (obj == null || obj instanceof TestRecursiveSimpleComposite == false)
         return false;

      TestRecursiveSimpleComposite other = (TestRecursiveSimpleComposite) obj;
      return something.equals(other.something) && composite.equals(other.composite);
   }

   public int hashCode()
   {
      return something.hashCode() + 11 * composite.hashCode();
   }
}
