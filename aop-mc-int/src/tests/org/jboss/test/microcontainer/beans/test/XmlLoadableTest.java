/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.microcontainer.beans.test;

import java.util.Map;


import org.jboss.aop.AspectManager;
import org.jboss.aop.metadata.ClassMetaDataLoader;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.beans.POJO;
import org.jboss.test.microcontainer.beans.XmlLoadableAspect;
import org.jboss.test.microcontainer.beans.XmlLoadableAspect2;
import org.jboss.test.microcontainer.beans.XmlLoadableClassMetaDataLoader;
import org.jboss.test.microcontainer.beans.XmlLoadableInterceptor;
import org.jboss.test.microcontainer.beans.XmlLoadableInterceptor2;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public /*abstract*/ class XmlLoadableTest extends AOPMicrocontainerTest
{
   public XmlLoadableTest(String name)
   {
      super(name);
   }

   public void testClassMetaDataLoader()
   {
      Map<String, ClassMetaDataLoader> loaders = AspectManager.instance().getClassMetaDataLoaders();
      assertNotNull(loaders);
      assertEquals(1, loaders.size());
      ClassMetaDataLoader loader = loaders.get("xxx");
      assertNotNull(loader);
      assertInstanceOf(loader, XmlLoadableClassMetaDataLoader.class);
      Element element = ((XmlLoadableClassMetaDataLoader)loader).getElement();
      
      checkXml(element, "metadata-loader", "somexml", "hello");
   }

   public void testAspectInterceptorAndDynamicCFlow()
   {
      POJO pojo = (POJO)getBean("Bean");
      pojo.defaultMethod();
      assertTrue(XmlLoadableAspect.intercepted);
      assertTrue(XmlLoadableInterceptor.intercepted);
      
      Element element = XmlLoadableAspect.interceptedElement;
      checkXml(element, "aspect", "somexml", "hello");
      assertEquals(12, XmlLoadableAspect.intAttr);
      //assertEquals("Test1", XmlLoadableAspect.mcProperty);
      
      element = XmlLoadableInterceptor.interceptedElement;
      checkXml(element, "interceptor", "somexml", "hello");
      assertEquals(13, XmlLoadableInterceptor.intAttr);
      //assertEquals("Test2", XmlLoadableInterceptor.mcProperty);

      element = XmlLoadableAspect2.interceptedElement; 
      checkXml(element, "aspect", "somexml", "hi");
      
      element = XmlLoadableInterceptor2.interceptedElement; 
      checkXml(element, "interceptor", "somexml", "hi");
      
      assertEquals(12, XmlLoadableAspect.intAttr);
      assertEquals(13, XmlLoadableInterceptor.intAttr);
   }
   
   private void checkXml(Element element, String rootName, String childName, String childContent)
   {
      assertNotNull(element);
      assertEquals(rootName, element.getTagName());
      NodeList list = element.getElementsByTagName(childName);
      assertNotNull(list);
      assertEquals(1, list.getLength());
      Element child = (Element)list.item(0);
      String s = child.getTextContent();
      assertEquals(childContent, s);
   }
}
