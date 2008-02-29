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

import org.jboss.aop.microcontainer.beans.ArrayBinding;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.util.id.GUID;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ArrayBindBeanMetaDataFactory extends AspectManagerAwareBeanMetaDataFactory
{

   private static final long serialVersionUID = 1L;
   
   String name;
   
   String type;

   private List<BaseInterceptorData> interceptors = new ArrayList<BaseInterceptorData>();
   
   public ArrayBindBeanMetaDataFactory()
   {
      setBeanClass("IGNORED");
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public void setType(String type)
   {
      this.type = type;
   }
   
   @Override
   public List<BeanMetaData> getBeans()
   {
      ArrayList<BeanMetaData> result = new ArrayList<BeanMetaData>();

      //Create AspectBinding
      if (name == null)
      {
         name = GUID.asString();
      }
      BeanMetaDataBuilder bindingBuilder = BeanMetaDataBuilder.createBuilder(name, ArrayBinding.class.getName());
      bindingBuilder.addPropertyMetaData("name", name);
      bindingBuilder.addPropertyMetaData("type", type);
      util.setAspectManagerProperty(bindingBuilder, "manager");
      result.add(bindingBuilder.getBeanMetaData());
      
      
      if (interceptors.size() > 0)
      {
         List<ValueMetaData> bindingInterceptors = bindingBuilder.createList();
         int i = 0;
         for (BaseInterceptorData interceptor : interceptors)
         {
            String intName = name + "$" + i++; 
            BeanMetaDataBuilder interceptorBuilder = BeanMetaDataBuilder.createBuilder(intName, interceptor.getBeanClassName());
            util.setAspectManagerProperty(interceptorBuilder, "manager");
            ValueMetaData injectBinding = interceptorBuilder.createInject(name, null, null, ControllerState.INSTANTIATED);
            interceptorBuilder.addPropertyMetaData("binding", injectBinding);
            
            if (interceptor instanceof AdviceOrInterceptorData)
            {
               ValueMetaData injectAspect = interceptorBuilder.createInject(interceptor.getRefName());
               interceptorBuilder.addPropertyMetaData("aspect", injectAspect);
               if (((AdviceOrInterceptorData)interceptor).getAdviceMethod() != null)
               {
                  interceptorBuilder.addPropertyMetaData("aspectMethod", ((AdviceOrInterceptorData)interceptor).getAdviceMethod());
               }
               interceptorBuilder.addPropertyMetaData("type", ((AdviceOrInterceptorData)interceptor).getType());
            }
            else
            {
               ValueMetaData injectStack = interceptorBuilder.createInject(interceptor.getRefName());
               interceptorBuilder.addPropertyMetaData("stack", injectStack);
            }
            result.add(interceptorBuilder.getBeanMetaData());
            ValueMetaData injectInterceptor = bindingBuilder.createInject(intName);
            bindingInterceptors.add(injectInterceptor);
         }         
         bindingBuilder.addPropertyMetaData("advices", bindingInterceptors);
      }
      
      return result;
   }
   
   public void addInterceptor(BaseInterceptorData interceptorData)
   {
      interceptors.add(interceptorData);
   }
}
