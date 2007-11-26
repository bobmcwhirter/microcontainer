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
import java.util.StringTokenizer;

import org.jboss.aop.microcontainer.beans.IntroductionBinding;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractInjectionValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractListMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.util.id.GUID;

/**
 * AspectBeanMetaDataFactory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 61194 $
 */
public class IntroductionBeanMetaDataFactory extends AspectManagerAwareBeanMetaDataFactory
   implements BeanMetaDataFactory
{
   private static final long serialVersionUID = 1L;

   private String clazz;
   private String expr;
   private String interfaces;
   private ArrayList<AbstractBeanMetaData> mixins = new ArrayList<AbstractBeanMetaData>();
   
   public IntroductionBeanMetaDataFactory()
   {
      setBeanClass("IGNORED");
   }
   
   public String getClazz()
   {
      return clazz;
   }

   public void setClazz(String clazz)
   {
      this.clazz = clazz;
   }

   public String getExpr()
   {
      return expr;
   }

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

   public void addMixinEntry(AbstractBeanMetaData mixin)
   {
      mixins.add(mixin);
   }
   
   @Override
   public List<BeanMetaData> getBeans()
   {
      ArrayList<BeanMetaData> result = new ArrayList<BeanMetaData>();
      
      //Add the Aspect
      AbstractBeanMetaData introduction = new AbstractBeanMetaData(IntroductionBinding.class.getName());
      String name = getName();
      if (name == null)
      {
         name = GUID.asString();
      }
      introduction.setName(name);
      BeanMetaDataUtil.setSimpleProperty(introduction, "name", name);
      util.setAspectManagerProperty(introduction, "manager");
      if (clazz != null)
      {
         BeanMetaDataUtil.setSimpleProperty(introduction, "classes", clazz);
      }
      if (expr != null)
      {
         BeanMetaDataUtil.setSimpleProperty(introduction, "expr", expr);
      }

      result.add(introduction);

      if (interfaces != null)
      {
         addInterfaces(introduction, "interfaces", interfaces);
      }
      if (mixins != null)
      {
         addMixins(introduction, result);
      }
      
      return result;
   }
   
   public static void addInterfaces(AbstractBeanMetaData introduction, String propertyName, String interfaces)
   {
      AbstractListMetaData lmd = new AbstractListMetaData();
      lmd.setType(ArrayList.class.getName());
      lmd.setElementType(String.class.getName());
      BeanMetaDataUtil.setSimpleProperty(introduction, propertyName, lmd);
      
      StringTokenizer tok = new StringTokenizer(interfaces, ",");
      while (tok.hasMoreTokens())
      {
         String token = tok.nextToken();
         lmd.add(new StringValueMetaData(token.trim()));
      }
   }
   
   private void addMixins(AbstractBeanMetaData introduction, List<BeanMetaData> result)
   {
      AbstractListMetaData lmd = new AbstractListMetaData();
      lmd.setType(ArrayList.class.getName());
      BeanMetaDataUtil.setSimpleProperty(introduction, "mixins", lmd);
      int i = 0;
      for (AbstractBeanMetaData mixin : mixins)
      {
         String name = introduction.getName() + "$" + i++;
         mixin.setName(name);
         result.add(mixin);

         lmd.add(new AbstractInjectionValueMetaData(name));
      }
   }
}
