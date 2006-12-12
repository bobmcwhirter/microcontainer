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

import javax.xml.namespace.QName;

import java.util.ArrayList;
import java.util.List;

import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementInterceptor;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.spring.deployment.AbstractConstructorArg;
import org.jboss.spring.deployment.AbstractSpringDeployment;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SpringBeansInterceptor extends DefaultElementInterceptor
{
   /**
    * The interceptor
    */
   public static final SpringBeansInterceptor INTERCEPTOR = new SpringBeansInterceptor();

   public void add(Object parent, Object child, QName name)
   {
      AbstractSpringDeployment deployment = (AbstractSpringDeployment) parent;
      AbstractBeanMetaData bean = (AbstractBeanMetaData) child;
      List<BeanMetaDataFactory> beans = deployment.getBeanFactories();
      if (beans == null)
      {
         beans = new ArrayList<BeanMetaDataFactory>();
         deployment.setBeanFactories(beans);
      }
      // set deployment defaults, if not already set per bean
      if (bean.getCreate() == null && deployment.getCreate() != null)
      {
         bean.setCreate(deployment.getCreate());
      }
      if (bean.getDestroy() == null && deployment.getDestroy() != null)
      {
         bean.setDestroy(deployment.getDestroy());
      }
      beans.add(bean);
   }

}
