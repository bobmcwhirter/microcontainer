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

import org.jboss.aop.microcontainer.beans.PrecedenceDef;
import org.jboss.aop.microcontainer.beans.PrecedenceDefEntry;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractInjectionValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractListMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.util.id.GUID;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class PrecedenceBeanMetaDataFactory extends AspectManagerAwareBeanMetaDataFactory
{
   private static final long serialVersionUID = 1L;
   private ArrayList<BaseInterceptorData> entries = new ArrayList<BaseInterceptorData>();
   
   public PrecedenceBeanMetaDataFactory()
   {
      setBeanClass("IGNORED");
   }
   
   
   @Override
   public List<BeanMetaData> getBeans()
   {
      ArrayList<BeanMetaData> result = new ArrayList<BeanMetaData>();
      
      //Add the PrecedenceDef
      AbstractBeanMetaData precedence = new AbstractBeanMetaData(PrecedenceDef.class.getName());
      String name = getName();
      if (name == null)
      {
         name = GUID.asString();
      }
      precedence.setName(name);
      BeanMetaDataUtil.setSimpleProperty(precedence, "name", getName());
      util.setAspectManagerProperty(precedence, "manager");
      result.add(precedence);
      
      AbstractListMetaData lmd = new AbstractListMetaData();
      lmd.setType(ArrayList.class.getName());
      BeanMetaDataUtil.setSimpleProperty(precedence, "entries", lmd);
      int i = 0;
      for (BaseInterceptorData entry : entries)
      {
         String entryName = name + "$" + i++;
         AbstractBeanMetaData entryBean = new AbstractBeanMetaData(PrecedenceDefEntry.class.getName());
         entryBean.setName(entryName);
         BeanMetaDataUtil.setSimpleProperty(entryBean, "aspectName", entry.getRefName());
         if (entry instanceof AdviceData)
         {
            BeanMetaDataUtil.setSimpleProperty(entryBean, "aspectMethod", ((AdviceData)entry).getAdviceMethod());
         }
         lmd.add(new AbstractInjectionValueMetaData(entryName));
         result.add(entryBean);
      }

      return result;
   }

   public void addEntry(BaseInterceptorData interceptor)
   {
      entries.add(interceptor);
   }
   
}
