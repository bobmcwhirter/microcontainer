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
package org.jboss.aop.microcontainer.beans.beanmetadatafactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.jboss.aop.microcontainer.beans.Aspect;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.xb.annotations.JBossXmlSchema;

/**
 * AspectBeanMetaDataFactory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 61194 $
 */
@JBossXmlSchema(namespace="urn:jboss:aop-beans:1.0", elementFormDefault=XmlNsForm.QUALIFIED)
@XmlRootElement(name="aspect")
@XmlType(propOrder={"aliases", "annotations", "classLoader", "constructor", "properties", "create", "start", "depends", "demands", "supplies", "installs", "uninstalls"})
public class AspectBeanMetaDataFactory extends AspectManagerAwareBeanMetaDataFactory
   implements BeanMetaDataFactory
{
   private static final long serialVersionUID = 1L;

   private String scope = "PER_VM";

   private String factory;

   String aspectName;
   
   boolean initialisedName;
   
   @XmlAttribute
   public void setScope(String scope)
   {
      this.scope = scope;
   }
   
   public String getScope()
   {
      return scope;
   }
   
   @XmlAttribute
   public void setFactory(String clazz)
   {
      this.factory = clazz;
      if (getBeanClass() != null)
      {
         throw new RuntimeException("Cannot set both factory and clazz");
      }
      //Meeded to satisfy validation in BeanFactoryHandler.endElement()
      super.setBeanClass(clazz);
   }
   
   public String getFactory()
   {
      return factory;
   }
   
   @Override
   @XmlAttribute(name="class")
   public void setBeanClass(String bean)
   {
      if (factory != null)
      {
         throw new RuntimeException("Cannot set both factory and clazz");
      }
      super.setBeanClass(bean);
   }
   
   @Override
   public List<BeanMetaData> getBeans()
   {
      ArrayList<BeanMetaData> result = new ArrayList<BeanMetaData>();
      
      if (this.name == null)
      {
         this.name = super.getBeanClass();
      }
      
      //Add the bean factory
      if (!initialisedName)
      {
         aspectName = this.name;
         this.name = "Factory$" + name;
         initialisedName = true;
      }
      result.add(this);
      
      //Add the Aspect
      BeanMetaDataBuilder aspectBuilder = BeanMetaDataBuilder.createBuilder(aspectName, Aspect.class.getName());
      aspectBuilder.addPropertyMetaData("scope", scope);
      util.setAspectManagerProperty(aspectBuilder, "manager");
      
      if (factory != null)
      {
         aspectBuilder.addPropertyMetaData("factory", Boolean.TRUE);
      }
      result.add(aspectBuilder.getBeanMetaData());
      
      if (hasInjectedBeans())
      {
         configureWithDependencies(aspectBuilder);
      }
      else
      {
         configureNoDependencies(aspectBuilder);
      }

      return result;
   }

   private void configureWithDependencies(BeanMetaDataBuilder aspectBuilder)
   {
      aspectBuilder.addPropertyMetaData("adviceBean", name);
      
      BeanMetaDataBuilder thisBuilder = BeanMetaDataBuilder.createBuilder(this);
      thisBuilder.addInstallWithThis("install", aspectBuilder.getBeanMetaData().getName());
      thisBuilder.addUninstall("uninstall", aspectBuilder.getBeanMetaData().getName());
   }
   
   private void configureNoDependencies(BeanMetaDataBuilder aspectBuilder)
   {
      ValueMetaData inject = aspectBuilder.createInject(name);
      aspectBuilder.addPropertyMetaData("advice", inject);
   }
   
   
   private boolean hasInjectedBeans()
   {
      ArrayList<ValueMetaData> dependencies = new ArrayList<ValueMetaData>();
      getDependencies(dependencies, this);
      
      for (ValueMetaData dep : dependencies)
      {
         if(!((String)dep.getUnderlyingValue()).startsWith("jboss.kernel:service="))
         {
            return true;
         }
      }
      return false;
   }
   
   private void getDependencies(ArrayList<ValueMetaData> dependencies, MetaDataVisitorNode node)
   {
      Iterator<? extends MetaDataVisitorNode> children = node.getChildren();
      
      if (children != null)
      {
         while (children.hasNext())
         {
            MetaDataVisitorNode child = children.next();
            if (child instanceof AbstractDependencyValueMetaData)
            {
               dependencies.add((AbstractDependencyValueMetaData)child);
            }
            getDependencies(dependencies, child);
         }
      }
   }
}
