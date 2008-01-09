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
package org.jboss.test.javabean.test;

import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.ioc.test.AbstractIoCTest;

/**
 * AbstractJavaBeanTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class OldAbstractJavaBeanTest extends AbstractIoCTest
{
   /**
    * Create a new AbstractJBossXBTest.
    * 
    * @param name the name of the test
    */
   public OldAbstractJavaBeanTest(String name)
   {
      super(name);
   }

   /**
    * Setup the test delegate
    * 
    * @param clazz the class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class clazz) throws Exception
   {
      return new JavaBeanTestDelegate(clazz);
   }
}