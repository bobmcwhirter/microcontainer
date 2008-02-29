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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.aop.microcontainer.beans.CFlowStack;
import org.jboss.aop.microcontainer.beans.CFlowStackEntry;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
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
@XmlRootElement(name="cflow")
public class CFlowStackBeanMetaDataFactory extends AspectManagerAwareBeanMetaDataFactory
   implements BeanMetaDataFactory
{
   private static final long serialVersionUID = 1L;
   
   private List<CFlowEntry> calledEntries = new ArrayList<CFlowEntry>();

   public CFlowStackBeanMetaDataFactory()
   {
      setBeanClass("IGNORED");
   }
   
   @Override
   public List<BeanMetaData> getBeans()
   {
      ArrayList<BeanMetaData> result = new ArrayList<BeanMetaData>();
      
      //Add the Aspect
      BeanMetaDataBuilder cflowStackBuilder = BeanMetaDataBuilder.createBuilder(getName(), CFlowStack.class.getName());
      cflowStackBuilder.addPropertyMetaData("name", getName());
      util.setAspectManagerProperty(cflowStackBuilder, "manager");
      result.add(cflowStackBuilder.getBeanMetaData());
      
      List<ValueMetaData> entryList = cflowStackBuilder.createList(null, ArrayList.class.getName());
      cflowStackBuilder.addPropertyMetaData("entries", entryList);
      int i = 0;
      if (calledEntries != null)
      {
         for (CFlowEntry entry : calledEntries)
         {
            String entryName = getName() + "$" + i++;
            BeanMetaDataBuilder entryBuilder = BeanMetaDataBuilder.createBuilder(entryName, CFlowStackEntry.class.getName());
            entryBuilder.addPropertyMetaData("called", entry.getCalled());
            entryBuilder.addPropertyMetaData("expr", entry.getExpr());
            ValueMetaData injectEntry = entryBuilder.createInject(entryName);
            entryList.add(injectEntry);
            result.add(entryBuilder.getBeanMetaData());
         }
      }

      return result;
   }

   public void addEntry(CFlowEntry entry)
   {
      calledEntries.add(entry);
   }

   public List<CFlowEntry> getCalledEntries()
   {
      return calledEntries;
   }

   @XmlElements({
      @XmlElement(name="called", type=CFlowEntry.class),
      @XmlElement(name="not-called", type=CFlowNotCalled.class)
   })
   public void setCalledEntries(List<CFlowEntry> calledEntries)
   {
      this.calledEntries = calledEntries;
   }
   
}
