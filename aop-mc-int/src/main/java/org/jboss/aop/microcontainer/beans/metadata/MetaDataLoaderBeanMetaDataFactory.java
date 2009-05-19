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

import org.jboss.aop.microcontainer.beans.ClassMetaDataLoader;
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
@XmlType(name="metadataLoaderType", propOrder={"aliases", "annotations", "classLoader", "constructor", "properties", "create", "start", "depends", "demands", "supplies", "installs", "uninstalls", "installCallbacks", "uninstallCallbacks"})
public class MetaDataLoaderBeanMetaDataFactory extends AspectManagerAwareBeanMetaDataFactory
{
   private static final long serialVersionUID = 1L;

   private String tag;
   
   private String clazz;

   private List<Element> elements;

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
      BeanMetaDataBuilder builder = AOPBeanMetaDataBuilder.createBuilder(name, ClassMetaDataLoader.class.getName());
      builder.addPropertyMetaData("tag", tag);
      builder.addPropertyMetaData("className", clazz);
      HashMap<String, String> attributes = new HashMap<String, String>();
      attributes.put("tag", tag);
      attributes.put("name", name);
      if (elements != null && elements.size() > 0)
      {
         builder.addPropertyMetaData("element", XmlLoadableRootElementUtil.getRootElementString(elements, "metadata-loader", attributes));
      }

      setAspectManagerProperty(builder);
      beans.add(builder.getBeanMetaData());
      return beans;
   }
}
