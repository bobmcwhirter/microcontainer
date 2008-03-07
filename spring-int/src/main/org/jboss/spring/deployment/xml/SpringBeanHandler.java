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
package org.jboss.spring.deployment.xml;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import org.jboss.beans.metadata.plugins.AbstractAnnotationMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractDemandMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractLifecycleMetaData;
import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.beans.metadata.api.enums.AutowireType;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.beans.metadata.spi.DemandMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.spring.annotations.SpringBean;
import org.jboss.spring.metadata.AbstractConstructorArg;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SpringBeanHandler extends DefaultElementHandler
{
   /**
    * The beans handler
    */
   public static final SpringBeanHandler HANDLER = new SpringBeanHandler();

   /**
    * The delimiter pattern
    */
   public static final String DELIMITER_PATTERN = "[,; ]";

   public Object startElement(Object parent, QName name, ElementBinding element)
   {
      AbstractBeanMetaData bean = new AbstractBeanMetaData();
      Set<AnnotationMetaData> annotations = bean.getAnnotations();
      if(annotations == null)
      {
         annotations = new HashSet<AnnotationMetaData>();
         bean.setAnnotations(annotations);
      }
      // It's a bit of a hack, but it's a transparent one
      AbstractAnnotationMetaData springAnnotation = new AbstractAnnotationMetaData();
      springAnnotation.setAnnotation("@" + SpringBean.class.getName());
      annotations.add(springAnnotation);
      return bean;
   }

   public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
   {
      AbstractBeanMetaData bean = (AbstractBeanMetaData) o;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         if ("id".equals(localName))
            bean.setName(attrs.getValue(i));
         else if ("name".equals(localName))
         {
            String name = attrs.getValue(i);
            String[] names = name.split(DELIMITER_PATTERN);
            bean.setAliases(new TreeSet<Object>(Arrays.asList(names)));
         }
         else if ("class".equals(localName))
            bean.setBean(attrs.getValue(i));
         else if ("init-method".equals(localName))
            bean.setCreate(new AbstractLifecycleMetaData(attrs.getValue(i)));
         else if ("lazy-init".equals(localName) && Boolean.parseBoolean(attrs.getValue(i)) == true)
            bean.setMode(ControllerMode.ON_DEMAND);
         else if ("parent".equals(localName))
            bean.setParent(attrs.getValue(i));
         else if ("abstract".equals(localName))
            bean.setAbstract(Boolean.parseBoolean(attrs.getValue(i)));
         else if ("autowire".equals(localName))
            bean.setAutowireType(AutowireType.getInstance(attrs.getValue(i)));
         else if ("autowire-candidate".equals(localName))
            bean.setAutowireCandidate(Boolean.parseBoolean(attrs.getValue(i)));
         else if ("destroy-method".equals(localName))
            bean.setDestroy(new AbstractLifecycleMetaData(attrs.getValue(i)));
         else if ("depends-on".equals(localName))
         {
            Set<DemandMetaData> demands = bean.getDemands();
            if (demands == null)
            {
               demands = new HashSet<DemandMetaData>();
               bean.setDemands(demands);
            }
            String[] values = attrs.getValue(i).split(DELIMITER_PATTERN);
            for(String name : values)
               demands.add(new AbstractDemandMetaData(name));
         }
         else if ("factory-method".equals(localName) || "factory-bean".equals(localName))
         {
            AbstractConstructorMetaData constructor = (AbstractConstructorMetaData) bean.getConstructor();
            if (constructor == null)
            {
               constructor = new AbstractConstructorMetaData();
               bean.setConstructor(constructor);
            }
            if ("factory-method".equals(localName))
               constructor.setFactoryMethod(attrs.getValue(i));
            if ("factory-bean".equals(localName))
               constructor.setFactory(new AbstractDependencyValueMetaData(attrs.getValue(i)));
         }
      }
   }

   public Object endElement(Object object, QName qName, ElementBinding elementBinding)
   {
      AbstractBeanMetaData beanMetaData = (AbstractBeanMetaData) object;
      ConstructorMetaData constructor = beanMetaData.getConstructor();
      if (constructor != null)
      {
         List<ParameterMetaData> parameters = constructor.getParameters();
         if (parameters != null && parameters.size() > 1)
         {
            orderParameters(parameters);
         }
      }
      return beanMetaData;
   }

   protected void orderParameters(List<ParameterMetaData> parameters)
   {
      ParameterMetaData[] pmds = new ParameterMetaData[parameters.size()];
      for(ParameterMetaData pmd : parameters)
      {
         int index = pmd.getIndex();
         // lets first try to set those with explicit index
         if (pmd instanceof AbstractConstructorArg && ((AbstractConstructorArg)pmd).isExplicitIndex())
         {
            if (pmds[index] != null)
               throw new IllegalArgumentException("Argument with index: " + index + " already set!");
            pmds[index] = pmd;
         }
      }
      int index = 0;
      for(ParameterMetaData pmd : parameters)
      {
         // then just put the others in the free places
         if ((pmd instanceof AbstractConstructorArg && ((AbstractConstructorArg)pmd).isExplicitIndex() == false))
         {
            while(pmds[index] != null) index++;
            pmds[index] = pmd;
         }
         index++;
      }
      // todo clone md
      parameters.clear();
      parameters.addAll(Arrays.asList(pmds));
   }

}
