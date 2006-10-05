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
package org.jboss.test.deployers.structure.jar.test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.structure.vfs.jar.JARStructure;
import org.jboss.test.deployers.BaseDeployersTest;

/**
 * JARStructureUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ConfiguredSuffixJARStructureUnitTestCase extends BaseDeployersTest
{
   /** The file structure deployer */
   Set<String> defaultSuffixes = new JARStructure().getSuffixes();

   public static Test suite()
   {
      return new TestSuite(ConfiguredSuffixJARStructureUnitTestCase.class);
   }
   
   public ConfiguredSuffixJARStructureUnitTestCase(String name)
   {
      super(name);
   }

   public void testDefaults() throws Exception
   {
      assertNotNull("default suffixes should not be null", defaultSuffixes);
      assertTrue("default suffixes size should be > 0", defaultSuffixes.size() > 0);
   }

   public void testNewUsingDefaults() throws Exception
   {
      JARStructure structure = new JARStructure();

      Set<String> suffixes = structure.getSuffixes();
      assertNotNull(suffixes);
      assertEquals(defaultSuffixes.size(), suffixes.size());
      for (String suff : defaultSuffixes)
      {
         suffixes.contains(suff);
      }
   }
   
   public void testOverwriteDefaults() throws Exception
   {

      Set<String> newSuffixes = new HashSet<String>();
      newSuffixes.add(".bar");
      newSuffixes.add(".tar");
      newSuffixes.add(".far");
      
      JARStructure structure = new JARStructure(newSuffixes);
      Set<String> suffixes = structure.getSuffixes();
      assertNotNull(suffixes);
      assertEquals(3, suffixes.size());
      assertTrue(suffixes.contains(".bar"));
      assertTrue(suffixes.contains(".tar"));
      assertTrue(suffixes.contains(".bar"));
   }
   
}
