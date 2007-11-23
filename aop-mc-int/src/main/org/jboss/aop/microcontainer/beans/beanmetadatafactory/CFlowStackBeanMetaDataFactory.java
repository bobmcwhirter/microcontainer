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
import java.util.List;

import org.jboss.aop.microcontainer.beans.CFlowStack;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractInjectionValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractListMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.PropertyMetaData;

/**
 * AspectBeanMetaDataFactory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 61194 $
 */
public class CFlowStackBeanMetaDataFactory extends AspectManagerAwareBeanMetaDataFactory
   implements BeanMetaDataFactory
{
   private static final long serialVersionUID = 1L;
   private ArrayList<AbstractBeanMetaData> entries = new ArrayList<AbstractBeanMetaData>();

   public CFlowStackBeanMetaDataFactory()
   {
      setBeanClass("IGNORED");
   }
   
   @Override
   public List<BeanMetaData> getBeans()
   {
      ArrayList<BeanMetaData> result = new ArrayList<BeanMetaData>();
      
      //Add the Aspect
      AbstractBeanMetaData cflowStack = new AbstractBeanMetaData(CFlowStack.class.getName());
      cflowStack.setName(getName());
      util.setSimpleProperty(cflowStack, "name", getName());
      util.setAspectManagerProperty(cflowStack, "manager");
      result.add(cflowStack);
      
      AbstractListMetaData lmd = new AbstractListMetaData();
      lmd.setType(ArrayList.class.getName());
      BeanMetaDataUtil.setSimpleProperty(cflowStack, "entries", lmd);
      int i = 0;
      for (AbstractBeanMetaData entry : entries)
      {
         String entryName = cflowStack.getName() + "$" + i++;
         entry.setName(entryName);
         lmd.add(new AbstractInjectionValueMetaData(entryName));
         result.add(entry);
      }

      return result;
   }

   public void addEntry(AbstractBeanMetaData entry)
   {
      entries.add(entry);
   }
}
