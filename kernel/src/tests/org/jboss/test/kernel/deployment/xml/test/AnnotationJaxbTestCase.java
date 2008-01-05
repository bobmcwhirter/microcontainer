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
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.test.kernel.deployment.xml.support.Annotation1;
import org.jboss.test.kernel.deployment.xml.support.AnnotationWithAttribute;
import org.jboss.test.kernel.deployment.xml.support.AnnotationWithAttributes;

/**
 * AnnotationJaxbTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 55527 $
 */
public class AnnotationJaxbTestCase extends AbstractMCTest
{
   protected AbstractAnnotationMetaData getAnnotation() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      ConstructorMetaData constructor = bean.getConstructor();
      assertNotNull(constructor);
      Set annotations = constructor.getAnnotations();
      assertEquals(1, annotations.size());
      AbstractAnnotationMetaData annotation = (AbstractAnnotationMetaData) annotations.iterator().next();
      assertNotNull(annotation);
      return annotation;
   }
   
   public void testAnnotationSimple() throws Exception
   {
      AbstractAnnotationMetaData annotation = getAnnotation();
      Annotation ann = annotation.getAnnotationInstance();
      assertEquals(Annotation1.class.getName(), ann.annotationType().getName());
      assertTrue(ann instanceof Annotation1);
   }
   
   public void testAnnotationWithAttribute() throws Exception
   {
      AbstractAnnotationMetaData annotation = getAnnotation();
      Annotation ann = annotation.getAnnotationInstance();
      assertEquals(AnnotationWithAttribute.class.getName(), ann.annotationType().getName());
      assertTrue(ann instanceof AnnotationWithAttribute);
      AnnotationWithAttribute ann1 = (AnnotationWithAttribute)ann;
      assertNotNull(ann1.attribute());
      assertEquals(Long.class, ann1.attribute());
   }
   
   public void testAnnotationWithAttributes() throws Exception
   {
      AbstractAnnotationMetaData annotation = getAnnotation();
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

   /* TODO
   public void testAnnotationBadNoContent() throws Exception
   {
      try
      {
         unmarshalBean("AnnotationBadNoContent.xml");
         fail("Should not be here");
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }
   */

   /* TODO
   public void testAnnotationBadNoContent2() throws Exception
   {
      try
      {
         unmarshalBean("AnnotationBadNoContent2.xml");
         fail("Should not be here");
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }
   */

   /* TODO
   public void testAnnotationBadNoLeadingAt() throws Exception
   {
      try
      {
         unmarshalBean("AnnotationBadNoLeadingAt.xml");
         fail("Should not be here");
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }
   */

   public static Test suite()
   {
      return AnnotationJaxbTestCase.suite(AnnotationJaxbTestCase.class);
   }

   public AnnotationJaxbTestCase(String name)
   {
      super(name);
   }
}
