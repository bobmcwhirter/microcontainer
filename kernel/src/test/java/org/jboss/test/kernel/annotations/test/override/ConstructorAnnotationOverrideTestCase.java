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
package org.jboss.test.kernel.annotations.test.override;

import java.util.List;
import java.util.ArrayList;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ConstructorAnnotationOverrideTestCase extends AbstractAnnotationOverrideTestCase
{
   public ConstructorAnnotationOverrideTestCase(String name) throws Throwable
   {
      super(name);
   }

   public ConstructorAnnotationOverrideTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(ConstructorAnnotationOverrideTestCase.class);
   }

   protected String getType()
   {
      return "Constructor";
   }

   protected void addMetaData(AbstractBeanMetaData beanMetaData)
   {
      AbstractConstructorMetaData constructorMetaData = new AbstractConstructorMetaData();
      List<ParameterMetaData> parameters = new ArrayList<ParameterMetaData>();
      parameters.add(new AbstractParameterMetaData("FromXML"));
      constructorMetaData.setParameters(parameters);
      beanMetaData.setConstructor(constructorMetaData);
   }

   public void testConstructorOverride() throws Throwable
   {
      checkOverride();
   }
}
