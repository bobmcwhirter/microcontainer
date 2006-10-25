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
package org.jboss.test.deployers.structure.main.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.structure.BasicStructuredDeployers;
import org.jboss.deployers.plugins.structure.vfs.explicit.DeclaredStructure;
import org.jboss.deployers.spi.structure.vfs.StructureDeployer;
import org.jboss.test.deployers.BaseDeployersTest;

/**
 * MainDeployerStructureUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class StructureUnitTestCase extends BaseDeployersTest
{
   public static Test suite()
   {
      return new TestSuite(MainDeployerStructureUnitTestCase.class);
   }
   
   public StructureUnitTestCase(String name)
   {
      super(name);
   }

   @Override
   protected void setUp() throws Exception
   {
      super.setUp();
      enableTrace("org.jboss.deployers");
   }

   public void testSetStructuredDeployers() throws Exception
   {
      BasicStructuredDeployers main = new BasicStructuredDeployers();
      assertEmpty(main.getDeployers());
      
      StructureDeployer declaredDeployer = new DeclaredStructure();
      Set<StructureDeployer> expected = new HashSet<StructureDeployer>();
      expected.add(declaredDeployer);
      main.setDeployers(expected);
      assertEquals(expected, main.getDeployers());
      Set<StructureDeployer> sdset = new TreeSet<StructureDeployer>(main.getDeployers());
      assertEquals(expected, sdset);

      SortedSet<StructureDeployer> deployers = main.getDeployers();
      Iterator<StructureDeployer> iter = deployers.iterator();
      StructureDeployer[] order = new StructureDeployer[4];
      int count = 0;
      while( iter.hasNext() )
      {
         order[count ++] = iter.next();
      }
      boolean equals = declaredDeployer.equals(order[0]);
      log.debug(declaredDeployer+", order[0]="+order[0]+", equals="+equals);
      assertEquals("deployers[0]", declaredDeployer, order[0]);

      expected = Collections.emptySet();
      main.setDeployers(expected);
      assertEquals(expected, main.getDeployers());
   }

}
