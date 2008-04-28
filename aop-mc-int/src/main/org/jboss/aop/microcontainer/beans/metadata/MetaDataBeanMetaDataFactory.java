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
package org.jboss.aop.microcontainer.beans.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.jboss.aop.microcontainer.beans.ClassMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.util.id.GUID;
import org.jboss.xb.annotations.JBossXmlSchema;
import org.w3c.dom.Element;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
@JBossXmlSchema(namespace="urn:jboss:aop-beans:1.0", elementFormDefault=XmlNsForm.QUALIFIED)
@XmlRootElement(name="metadata")
//Set a random proporder since we don't want to set any of the properties in GenericBeanFactoryMetaData in the
//freeform xml contained in this element
@XmlType(name="metaDataType", propOrder={"non$$existant$$property"})
public class MetaDataBeanMetaDataFactory extends AspectManagerAwareBeanMetaDataFactory
{
   private static final long serialVersionUID = 1L;

   String tag;
   
   String clazz;

   //It would have been nice to handle the normal metadata elements handled by the SimpleMetaDataElements in a typed
   //way, but what if we have a custom metadata loader that expects elements with the same names?
   List<Element> elements;
   
   public String getTag()
   {
      return tag;
   }

   @XmlAttribute
   public void setTag(String tag)
   {
      this.tag = tag;
   }

   public String getClazz()
   {
      return clazz;
   }
   
   @XmlAttribute(name="class")
   public void setClazz(String clazz)
   {
      this.clazz = clazz;
   }

   public List<Element> getElements()
   {
      return elements;
   }

   @XmlAnyElement(lax=true)
   public void setElements(List<Element> elements)
   {
      this.elements = elements;
   }

   @Override
   public List<BeanMetaData> getBeans()
   {
      List<BeanMetaData> beans = new ArrayList<BeanMetaData>();
      if (name == null)
      {
         name = GUID.asString();
      }
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder(name, ClassMetaData.class.getName());
      builder.addPropertyMetaData("tag", tag);
      builder.addPropertyMetaData("className", clazz);
      HashMap<String, String> attributes = new HashMap<String, String>();
      attributes.put("tag", tag);
      attributes.put("class", clazz);
      builder.addPropertyMetaData("element", XmlLoadableRootElementUtil.getRootElementString(elements, "metadata", attributes));
      
      setAspectManagerProperty(builder);
      beans.add(builder.getBeanMetaData());
      return beans;
   }

   /**
    * Here to match the @XMLType.propOrder
    * @param ignored ???
    */
   public void setNon$$existant$$property(String ignored)
   {
   }
}
