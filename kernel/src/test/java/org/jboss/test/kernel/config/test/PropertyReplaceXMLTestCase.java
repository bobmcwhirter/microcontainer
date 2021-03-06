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
package org.jboss.test.kernel.config.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.kernel.config.support.MyObject;
import org.jboss.test.kernel.config.support.SimpleAnnotation;
import org.jboss.test.kernel.config.support.XMLUtil;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class PropertyReplaceXMLTestCase extends PropertyReplaceTestCase
{
   public PropertyReplaceXMLTestCase(String name)
   {
      super(name);
   }

   public PropertyReplaceXMLTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(PropertyReplaceXMLTestCase.class);
   }

   protected Object instantiateValue(String type) throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      MyObject mybean = (MyObject)util.getBean("MyBean" + type);
      return mybean.getKey();
   }

   protected Object instantiateProperty() throws Throwable
   {
      return instantiateValue("Property");
   }

   protected Object instantiateConstructorParameter() throws Throwable
   {
      return instantiateValue("Parameter");
   }

   protected Object instantiatePlainValue() throws Throwable
   {
      return instantiateValue("PlainValue");
   }

   protected List<ObjectCreator> createCreators()
   {
      List<ObjectCreator> result = new ArrayList<ObjectCreator>();
      ObjectCreator property = new ObjectCreator()
      {
         public Object createObject() throws Throwable
         {
            return instantiateProperty();
         }
      };
      result.add(property);
      ObjectCreator parameter = new ObjectCreator()
      {
         public Object createObject() throws Throwable
         {
            return instantiateConstructorParameter();
         }
      };
      result.add(parameter);
      ObjectCreator plainValue = new ObjectCreator()
      {
         public Object createObject() throws Throwable
         {
            return instantiatePlainValue();
         }
      };
      result.add(plainValue);
      return result;
   }

   protected SimpleAnnotation instantiateAnnotation(boolean replace) throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      KernelController controller = util.getKernel().getController();
      KernelControllerContext context = (KernelControllerContext)controller.getInstalledContext("MyBean" + (replace ? "Replace" : "Ignore"));
      BeanMetaData beanMetaData = context.getBeanMetaData();
      Set<AnnotationMetaData> annotations = beanMetaData.getAnnotations();
      assertNotNull(annotations);
      assertFalse(annotations.isEmpty());
      assertEquals(1, annotations.size());
      return (SimpleAnnotation)annotations.iterator().next().getAnnotationInstance();
   }
}
