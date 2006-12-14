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

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.spring.metadata.AbstractConstructorArg;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementInterceptor;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ConstructorArgInterceptor extends DefaultElementInterceptor
{
   /**
    * The interceptor
    */
   public static final ConstructorArgInterceptor INTERCEPTOR = new ConstructorArgInterceptor();

   public void add(Object parent, Object child, QName name)
   {
      AbstractBeanMetaData bean = (AbstractBeanMetaData) parent;
      AbstractConstructorMetaData constructor = (AbstractConstructorMetaData) bean.getConstructor();
      if (constructor == null) {
         constructor = new AbstractConstructorMetaData();
         bean.setConstructor(constructor);
      }
      AbstractParameterMetaData parameter = (AbstractParameterMetaData) child;
      List<ParameterMetaData> parameters = constructor.getParameters();
      if (parameters == null)
      {
         parameters = new ArrayList<ParameterMetaData>();
         constructor.setParameters(parameters);
      }
      if (parameter instanceof AbstractConstructorArg && ((AbstractConstructorArg)parameter).isExplicitIndex() == false)
      {
         // actual order is done in SpringBeanHandler
         parameter.setIndex(parameters.size());
      }
      parameters.add(parameter);
   }

}
