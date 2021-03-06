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
package org.jboss.test.kernel.deployment.xml.test;

import java.lang.annotation.Annotation;
import java.util.Set;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractAnnotationMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.test.kernel.deployment.xml.support.Annotation1;
import org.jboss.test.kernel.deployment.xml.support.AnnotationWithAttribute;
import org.jboss.test.kernel.deployment.xml.support.AnnotationWithAttributes;

/**
 * AnnotationTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AnnotationTestCase extends AbstractXMLTest
{
   protected AbstractAnnotationMetaData getAnnotation(String name) throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean(name);
      ConstructorMetaData constructor = bean.getConstructor();
      assertNotNull(constructor);
      Set<AnnotationMetaData> annotations = constructor.getAnnotations();
      assertEquals(1, annotations.size());
      AbstractAnnotationMetaData annotation = (AbstractAnnotationMetaData) annotations.iterator().next();
      assertNotNull(annotation);
      return annotation;
   }
   
   public void testSimpleAnnotation() throws Exception
   {
      AbstractAnnotationMetaData annotation = getAnnotation("AnnotationSimple.xml");
      Annotation ann = annotation.getAnnotationInstance();
      assertEquals(Annotation1.class.getName(), ann.annotationType().getName());
      assertTrue(ann instanceof Annotation1);
   }
   
   public void testAnnotationWithAttribute() throws Exception
   {
      AbstractAnnotationMetaData annotation = getAnnotation("AnnotationWithAttribute.xml");
      Annotation ann = annotation.getAnnotationInstance();
      assertEquals(AnnotationWithAttribute.class.getName(), ann.annotationType().getName());
      assertTrue(ann instanceof AnnotationWithAttribute);
      AnnotationWithAttribute ann1 = (AnnotationWithAttribute)ann;
      assertNotNull(ann1.attribute());
      assertEquals(Long.class, ann1.attribute());
   }
   
   public void testAnnotationWithAttributes() throws Exception
   {
      AbstractAnnotationMetaData annotation = getAnnotation("AnnotationWithAttributes.xml");
      Annotation ann = annotation.getAnnotationInstance();
      assertEquals(AnnotationWithAttributes.class.getName(), ann.annotationType().getName());
      assertTrue(ann instanceof AnnotationWithAttributes);
      AnnotationWithAttributes ann1 = (AnnotationWithAttributes)ann;
      assertNotNull(ann1.clazz());
      assertEquals(Integer.class, ann1.clazz());
      assertNotNull(ann1.integer());
      assertEquals(100, ann1.integer());
      assertNotNull(ann1.str());
      assertEquals("Annotations are nice", ann1.str());
   }

   public static Test suite()
   {
      return suite(AnnotationTestCase.class);
   }

   public AnnotationTestCase(String name)
   {
      super(name);
   }

   protected AnnotationTestCase(String name, boolean useClone)
   {
      super(name, useClone);
   }
}
