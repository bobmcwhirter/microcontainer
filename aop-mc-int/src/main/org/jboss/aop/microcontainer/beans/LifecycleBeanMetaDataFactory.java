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
package org.jboss.aop.microcontainer.beans;

import java.util.List;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractListMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.ValueMetaData;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class LifecycleBeanMetaDataFactory extends AspectBeanMetaDataFactory implements BeanMetaDataFactory
{
   String classes;

   public String getClasses()
   {
      return classes;
   }

   public void setClasses(String classes)
   {
      this.classes = classes;
   }
   
   public List<BeanMetaData> getBeans()
   {
      List<BeanMetaData> beans = super.getBeans();

      String aspectBindingName = name + "$IntroductionBinding";
      AbstractBeanMetaData introductionBinding = new AbstractBeanMetaData();
      introductionBinding.setName(aspectBindingName);
      introductionBinding.setBean("org.jboss.aop.microcontainer.beans.IntroductionBinding");
      introductionBinding.addProperty(getAspectManagerPropertyMetaData("manager"));
      introductionBinding.addProperty(new AbstractPropertyMetaData("interfaces", getInterfaces()));
      introductionBinding.addProperty(new AbstractPropertyMetaData("classes", getClasses()));
      beans.add(introductionBinding);
      
      return beans;
   }
   
   private ValueMetaData getInterfaces()
   {
      AbstractListMetaData interfaces = new AbstractListMetaData();
      interfaces.setElementType("java.lang.String");
      interfaces.add(new StringValueMetaData("org.jboss.kernel.spi.dependency.KernelControllerContextAware"));
      return interfaces;
   }
}
