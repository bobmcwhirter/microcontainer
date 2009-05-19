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
package org.jboss.aop.microcontainer.beans.metadata;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.jboss.aop.microcontainer.beans.IntroductionBinding;
import org.jboss.aop.microcontainer.beans.MixinEntry;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.util.id.GUID;
import org.jboss.xb.annotations.JBossXmlSchema;

/**
 * AspectBeanMetaDataFactory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 61194 $
 */
@JBossXmlSchema(namespace="urn:jboss:aop-beans:1.0", elementFormDefault=XmlNsForm.QUALIFIED)
@XmlRootElement(name="introduction")
//@XmlType(name="introductionType", propOrder={"aliases", "annotations", "classLoader", "constructor", "properties", "create", "start", "depends", "demands", "supplies", "installs", "uninstalls", "installCallbacks", "uninstallCallbacks"})
@XmlType(name="introductionType", propOrder={})
public class IntroductionBeanMetaDataFactory extends AspectManagerAwareBeanMetaDataFactory
   implements BeanMetaDataFactory
{
   private static final long serialVersionUID = 1L;

   private String clazz;
   private String expr;
   private String interfaces;
   private List<MixinData> mixins = new ArrayList<MixinData>();
   
   public IntroductionBeanMetaDataFactory()
   {
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

   public String getExpr()
   {
      return expr;
   }

   @XmlAttribute(name="expr")
   public void setExpr(String expr)
   {
      this.expr = expr;
   }

   
   public String getInterfaces()
   {
      return interfaces;
   }

   public void setInterfaces(String interfaces)
   {
      this.interfaces = interfaces;
   }

   public List<MixinData> getMixins()
   {
      return mixins;
   }

   @XmlElement(name="mixin")
   public void setMixins(List<MixinData> mixins)
   {
      this.mixins = mixins;
   }

   @Override
   @XmlTransient
   public List<BeanMetaData> getBeans()
   {
      ArrayList<BeanMetaData> result = new ArrayList<BeanMetaData>();
      
      //Add the Introduction
      if (name == null)
      {
         name = GUID.asString();
      }
     
      BeanMetaDataBuilder introductionBuilder = AOPBeanMetaDataBuilder.createBuilder(name, IntroductionBinding.class.getName());
      introductionBuilder.addPropertyMetaData("name", name);
      setAspectManagerProperty(introductionBuilder);
      if (clazz != null)
      {
         introductionBuilder.addPropertyMetaData("classes", clazz);
      }
      if (expr != null)
      {
         introductionBuilder.addPropertyMetaData("expr", expr);
      }

      result.add(introductionBuilder.getBeanMetaData());
      
      if (interfaces != null)
      {
         addInterfaces(introductionBuilder, interfaces);
      }
      if (mixins != null)
      {
         addMixins(introductionBuilder, result);
      }
      
      return result;
   }

   private void addInterfaces(BeanMetaDataBuilder introductionBuilder, String interfaces)
   {
      addInterfaces(introductionBuilder, "interfaces", interfaces);
   }
   
   private void addInterfaces(BeanMetaDataBuilder introductionBuilder, String propertyName, String interfaces)
   {
      List<ValueMetaData> ifs = introductionBuilder.createList(ArrayList.class.getName(), String.class.getName());
      introductionBuilder.addPropertyMetaData(propertyName, ifs);
      for (String token : interfaces.split(","))
      {
         ifs.add(introductionBuilder.createValue(token.trim()));
      }
   }
   
   private void addMixins(BeanMetaDataBuilder introductionBuilder, List<BeanMetaData> result)
   {
      List<ValueMetaData> mixinList = introductionBuilder.createList(ArrayList.class.getName(), null);
      introductionBuilder.addPropertyMetaData("mixins", mixinList);
      int i = 0;
      for (MixinData mixin : mixins)
      {
         String name = introductionBuilder.getBeanMetaData().getName() + "$" + i++;
         BeanMetaDataBuilder mixinBuilder = AOPBeanMetaDataBuilder.createBuilder(name, MixinEntry.class.getName());
         mixinBuilder.addPropertyMetaData("mixin", mixin.getMixin());
         addInterfaces(mixinBuilder, "interfaces", mixin.getInterfaces());
         mixinBuilder.addPropertyMetaData("transient", mixin.getTransient());
         if (mixin.getConstruction() != null)
         {
            mixinBuilder.addPropertyMetaData("construction", mixin.getConstruction());
         }
         
         result.add(mixinBuilder.getBeanMetaData());
         
         ValueMetaData injectMixin = introductionBuilder.createInject(name);
         mixinList.add(injectMixin);
      }
   }
}
