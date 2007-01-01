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
package org.jboss.test.managed;

import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;

/**
 * ManagedTest base class that extends AbstractTestCaseWithSetup
 * to introduce the ManagedTestDelegate as the setUp delegate.
 * 
 * @see org.jboss.test.AbstractTestSetup#setUp
 * @see org.jboss.test.AbstractTestDelegate#getDelegate
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public abstract class ManagedTest extends AbstractTestCaseWithSetup
{
   /**
    * A static getDelegate method that is called by the AbstractTestDelegate
    * getDelegate logic to obtain the test specific delegate. This sets the
    * default delegate for ManagedTests to ManagedTestDelegate.
    * 
    * @param clazz the test class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class clazz) throws Exception
   {
      return new ManagedTestDelegate(clazz);
   }
   
   /**
    * Create a new Managed test
    * 
    * @param name the test name
    */
   public ManagedTest(String name)
   {
      super(name);
   }

   /**
    * Adds a call to configureLogging after super.setUp.
    */
   protected void setUp() throws Exception
   {
      super.setUp();
      configureLogging();
   }
}
