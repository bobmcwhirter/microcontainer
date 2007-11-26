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
package org.jboss.aop.microcontainer.beans.beanmetadatafactory;

import java.util.ArrayList;
import java.util.List;


import org.jboss.aop.microcontainer.beans.AOPDomain;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class DomainBeanMetaDataFactory extends AspectManagerAwareBeanMetaDataFactory
{
   private static final long serialVersionUID = 1L;
   String parentFirst;
   String inheritDefinitions;
   String inheritBindings;
   String extendz;
   
   ArrayList<BeanMetaData> childBeans = new ArrayList<BeanMetaData>();
   
   public DomainBeanMetaDataFactory()
   {
      setBeanClass("IGNORED");
   }

   public void setParentFirst(String parentFirst)
   {
      this.parentFirst = parentFirst;
   }

   public void setInheritDefinitions(String inheritDefinitions)
   {
      this.inheritDefinitions = inheritDefinitions;
   }

   public void setInheritBindings(String inheritBindings)
   {
      this.inheritBindings = inheritBindings;
   }

   public void setExtends(String extendz)
   {
      this.extendz = extendz;
   }

   public void addChildBean(BeanMetaData child)
   {
      childBeans.add(child);
   }
   
   @Override
   public List<BeanMetaData> getBeans()
   {
      ArrayList<BeanMetaData> result = new ArrayList<BeanMetaData>();
      
      AbstractBeanMetaData domain = new AbstractBeanMetaData(AOPDomain.class.getName());
      BeanMetaDataUtil.setSimpleProperty(domain, "name", getName());
      domain.setName(getName());
      
      if (parentFirst != null)
      {
         BeanMetaDataUtil.setSimpleProperty(domain, "parentFirst", parentFirst);
      }
      if (inheritDefinitions != null)
      {
         BeanMetaDataUtil.setSimpleProperty(domain, "inheritDefinitions", inheritDefinitions);
      }
      if (inheritBindings != null)
      {
         BeanMetaDataUtil.setSimpleProperty(domain, "inheritBindings", inheritBindings);
      }
      if (extendz != null)
      {
         BeanMetaDataUtil.setSimpleProperty(domain, "extends", extendz);
      }
      util.setAspectManagerProperty(domain, "manager");
      result.add(domain);
      
      for (BeanMetaData child : childBeans)
      {
         if (child instanceof BeanMetaDataFactory)
         {
            result.addAll(((BeanMetaDataFactory)child).getBeans());
         }
         else
         {
            result.add(child);
         }
      }
      
      return result;
   }
}
