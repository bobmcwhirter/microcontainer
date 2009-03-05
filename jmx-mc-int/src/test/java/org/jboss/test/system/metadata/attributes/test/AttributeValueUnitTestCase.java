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
package org.jboss.test.system.metadata.attributes.test;

import org.jboss.system.metadata.ServiceValueMetaData;

/**
 * AttributeValueUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AttributeValueUnitTestCase extends AbstractAttributeTest
{
   public AttributeValueUnitTestCase(String name)
   {
      super(name);
   }

   public void testAttributeValueText() throws Exception
   {
      ServiceValueMetaData value = unmarshallSingleValue();
      assertTextValue(value, "value");
   }

   public void testAttributeValueDepends() throws Exception
   {
      ServiceValueMetaData value = unmarshallSingleValue();
      assertDependencyValue(value, TEST1);
   }

   public void testAttributeValueDependsList() throws Exception
   {
      ServiceValueMetaData value = unmarshallSingleValue();
      assertDependencyListValue(value, TEST1);
   }

   public void testAttributeValueElement() throws Exception
   {
      ServiceValueMetaData value = unmarshallSingleValue();
      assertElementValue(value, "dummy");
   }

   public void testAttributeValueJavaBean() throws Exception
   {
      ServiceValueMetaData value = unmarshallSingleValue();
      assertJavaBeanValue(value, "dummy");
   }

   public void testAttributeValueJBXB() throws Exception
   {
      ServiceValueMetaData value = unmarshallSingleValue();
      assertJBXBValue(value, "dummy");
   }

   public void testAttributeValueInject() throws Exception
   {
      ServiceValueMetaData value = unmarshallSingleValue();
      assertInjectValue(value, "bean", "property");
   }
}
