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
package org.jboss.test.kernel.annotations.test.field;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import junit.framework.Test;
import org.jboss.kernel.plugins.annotations.BeanAnnotationAdapter;
import org.jboss.test.kernel.annotations.test.AbstractBeanAnnotationAdapterTest;
import org.jboss.test.kernel.annotations.support.MockFieldBeanAnnotationAdapter;
import org.jboss.test.kernel.annotations.support.MockFieldTester;
import org.jboss.test.kernel.annotations.support.MockInjectPlugin;

/**
 * Simple field test.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SimpleFieldTestCase extends AbstractBeanAnnotationAdapterTest
{
   public SimpleFieldTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(SimpleFieldTestCase.class);
   }

   protected BeanAnnotationAdapter getBeanAnnotationAdapterClass()
   {
      return new MockFieldBeanAnnotationAdapter();
   }

   public void testFieldUsage() throws Throwable
   {
      MockFieldTester tester = new MockFieldTester();
      try
      {
         runAnnotationsOnTarget(tester);
         Set<String> expected = new HashSet<String>(Arrays.asList("date"));//, "time", "string"));
         assertEquals(expected, MockInjectPlugin.INSTANCE.getFieldNames());
      }
      finally
      {
         MockInjectPlugin.INSTANCE.clear();
      }
   }
}
