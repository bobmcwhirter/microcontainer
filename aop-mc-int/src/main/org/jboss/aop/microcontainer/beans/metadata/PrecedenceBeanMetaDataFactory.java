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
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.aop.microcontainer.beans.PrecedenceDef;
import org.jboss.aop.microcontainer.beans.PrecedenceDefEntry;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.util.id.GUID;
import org.jboss.xb.annotations.JBossXmlSchema;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
@JBossXmlSchema(namespace="urn:jboss:aop-beans:1.0", elementFormDefault=XmlNsForm.QUALIFIED)
@XmlRootElement(name="precedence")
public class PrecedenceBeanMetaDataFactory extends AspectManagerAwareBeanMetaDataFactory
{
   private static final long serialVersionUID = 1L;
   private List<BaseInterceptorData> entries = new ArrayList<BaseInterceptorData>();
   
   public PrecedenceBeanMetaDataFactory()
   {
   }
   
   
   @Override
   public List<BeanMetaData> getBeans()
   {
      ArrayList<BeanMetaData> result = new ArrayList<BeanMetaData>();
      
      //Add the PrecedenceDef
      if (name == null)
      {
         name = GUID.asString();
      }
      BeanMetaDataBuilder precedenceBuilder = BeanMetaDataBuilder.createBuilder(name, PrecedenceDef.class.getName());
      precedenceBuilder.addPropertyMetaData("name", getName());
      setAspectManagerProperty(precedenceBuilder);
      result.add(precedenceBuilder.getBeanMetaData());
      
      List<ValueMetaData> entryList = precedenceBuilder.createList(ArrayList.class.getName(), null);
      precedenceBuilder.addPropertyMetaData("entries", entryList);
      int i = 0;
      for (BaseInterceptorData entry : entries)
      {
         String entryName = name + "$" + i++;
         BeanMetaDataBuilder entryBuilder = BeanMetaDataBuilder.createBuilder(entryName, PrecedenceDefEntry.class.getName());
         
         entryBuilder.addPropertyMetaData("aspectName", entry.getRefName());
         if (entry instanceof AdviceOrInterceptorData)
         {
            entryBuilder.addPropertyMetaData("aspectMethod", ((AdviceOrInterceptorData)entry).getAdviceMethod());
         }
         
         ValueMetaData injectEntry = precedenceBuilder.createInject(entryName);
         entryList.add(injectEntry);
         result.add(entryBuilder.getBeanMetaData());
      }

      return result;
   }

   @XmlElements
   ({
      @XmlElement(name="advice", type=AdviceData.class),
      @XmlElement(name="interceptor-ref", type=InterceptorRefData.class)
   })
   public List<BaseInterceptorData> getEntries()
   {
      return entries;
   }

   public void setEntries(List<BaseInterceptorData> entries)
   {
      this.entries = entries;
   }
   
}
