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
package org.jboss.test.microcontainer.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;

import org.jboss.aop.Advisor;
import org.jboss.aop.metadata.ClassMetaDataBinding;
import org.jboss.aop.metadata.ClassMetaDataLoader;
import org.jboss.util.xml.XmlLoadable;
import org.w3c.dom.Element;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class XmlLoadableClassMetaDataLoader implements ClassMetaDataLoader, XmlLoadable
{
   Element element;

   public void importXml(Element element)
   {
      this.element = element;
   }

   public Element getElement()
   {
      return element;
   }
   
   public void bind(Advisor advisor, ClassMetaDataBinding data, CtMethod[] methods, CtField[] fields,
         CtConstructor[] constructors) throws Exception
   {
   }

   public void bind(Advisor advisor, ClassMetaDataBinding data, Method[] methods, Field[] fields,
         Constructor<?>[] constructors) throws Exception
   {
   }

   public ClassMetaDataBinding importMetaData(Element element, String name, String tag, String classExpr)
         throws Exception
   {
      return null;
   }

}
