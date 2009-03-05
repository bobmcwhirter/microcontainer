/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.test.system.metadata.test;

import org.jboss.test.system.metadata.attributes.test.AttributeUnitTestCase;
import org.jboss.test.system.metadata.attributes.test.AttributeValueUnitTestCase;
import org.jboss.test.system.metadata.attributes.test.DependsAttributeUnitTestCase;
import org.jboss.test.system.metadata.attributes.test.DependsListAttributeUnitTestCase;
import org.jboss.test.system.metadata.basic.test.BasicMBeanUnitTestCase;
import org.jboss.test.system.metadata.constructor.test.ConstructorUnitTestCase;
import org.jboss.test.system.metadata.depends.test.DependsOptionalAttributeUnitTestCase;
import org.jboss.test.system.metadata.depends.test.DependsUnitTestCase;
import org.jboss.test.system.metadata.value.MetaDataValueTestSuite;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * MetaData Test Suite.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class MetaDataTestSuite extends TestSuite
{
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("MetaData Tests");

      suite.addTest(new TestSuite(BasicMBeanUnitTestCase.class));
      suite.addTest(new TestSuite(ConstructorUnitTestCase.class));
      suite.addTest(new TestSuite(DependsUnitTestCase.class));
      suite.addTest(new TestSuite(DependsOptionalAttributeUnitTestCase.class));
      suite.addTest(new TestSuite(AttributeUnitTestCase.class));
      suite.addTest(new TestSuite(DependsAttributeUnitTestCase.class));
      suite.addTest(new TestSuite(DependsListAttributeUnitTestCase.class));
      suite.addTest(new TestSuite(AttributeValueUnitTestCase.class));
      suite.addTest(MetaDataValueTestSuite.suite());
      
      return suite;
   }
}
