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
package org.jboss.test.kernel.config.support;

/**
 * A simple bean factory
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class SimpleBeanFactory 
{
   // Constants -----------------------------------------------------

   /** The method used */
   private static String methodUsed;
   
   // Attributes ----------------------------------------------------

   // Static --------------------------------------------------------

   public static String getMethodUsed()
   {
      return methodUsed;
   }

   public static SimpleBean staticCreateSimpleBean()
   {
      methodUsed = "staticCreateSimpleBean()";
      return new SimpleBean();
   }

   public static SimpleBean staticCreateSimpleBean(String string)
   {
      methodUsed = "staticCreateSimpleBean(String)";
      return new SimpleBean(string);
   }

   public static SimpleBean staticCreateSimpleBean(Integer integer)
   {
      methodUsed = "staticCreateSimpleBean(integer)";
      return new SimpleBean(integer.toString());
   }
   
   // Constructors --------------------------------------------------

   public SimpleBeanFactory()
   {
   }

   // Public --------------------------------------------------------

   public SimpleBean createSimpleBean()
   {
      methodUsed = "createSimpleBean()";
      return new SimpleBean();
   }

   public SimpleBean createSimpleBean(String string)
   {
      methodUsed = "createSimpleBean(String)";
      return new SimpleBean(string);
   }

   public SimpleBean createSimpleBean(Integer integer)
   {
      methodUsed = "createSimpleBean(integer)";
      return new SimpleBean(integer.toString());
   }

   // Package protected ---------------------------------------------

   // Protected -----------------------------------------------------

   // Private -------------------------------------------------------

   // Inner classes -------------------------------------------------
}