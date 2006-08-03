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
package org.jboss.test.microcontainer.matrix.mc.test;


import junit.framework.Test;

import org.jboss.aop.Advised;
import org.jboss.aop.microcontainer.junit.AOPMicrocontainerTest;
import org.jboss.aop.proxy.container.AspectManaged;
import org.jboss.test.microcontainer.matrix.Base;
import org.jboss.test.microcontainer.matrix.Child;

/**
 * Only tested for MC since setting up the metadatacontext manually for the proxyfactory test is too cumbersome
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class BaseNotAdvisedChildNotAdvisedTestCase extends AOPMicrocontainerTest
{
   public void testNotAdvised() throws Exception
   {
      Base base = (Base)getBean("Base");
      assertFalse(base instanceof Advised);
      assertFalse(base instanceof AspectManaged);

      Child child = (Child)getBean("Child");
      assertFalse(child instanceof Advised);
      assertFalse(child instanceof AspectManaged);
   }
   
   
   public static Test suite()
   {
      return suite(BaseNotAdvisedChildNotAdvisedTestCase.class);
   }

   public BaseNotAdvisedChildNotAdvisedTestCase(String name)
   {
      super(name);
   }
}
