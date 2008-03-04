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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.aop.microcontainer.beans.AspectBinding;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.util.id.GUID;
import org.jboss.xb.annotations.JBossXmlSchema;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
@JBossXmlSchema(namespace="urn:jboss:aop-beans:1.0", elementFormDefault=XmlNsForm.QUALIFIED)
@XmlRootElement(name="bind")
public class BindBeanMetaDataFactory extends AspectManagerAwareBeanMetaDataFactory
{
   private static final long serialVersionUID = 1L;
   private String pointcut;
   private String cflow;
   private List<BaseInterceptorData> interceptors = new ArrayList<BaseInterceptorData>();

   public BindBeanMetaDataFactory()
   {
   }

   @XmlAttribute
   public void setPointcut(String pointcut)
   {
      this.pointcut = pointcut;
   }
   
   @XmlAttribute
   public void setCflow(String cflow)
   {
      this.cflow = cflow;
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
      BeanMetaDataBuilder bindingBuilder = BeanMetaDataBuilder.createBuilder(name, AspectBinding.class.getName());
      bindingBuilder.addPropertyMetaData("name", name);
      if (cflow != null)
      {
         bindingBuilder.addPropertyMetaData("cflow", cflow);
      }
      bindingBuilder.addPropertyMetaData("pointcut", pointcut);
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

   
   @XmlElements
   ({
      @XmlElement(name="advice", type=AdviceData.class),
      @XmlElement(name="around", type=AdviceData.class),
      @XmlElement(name="before", type=BeforeAdviceData.class),
      @XmlElement(name="after", type=AfterAdviceData.class),
      @XmlElement(name="throwing", type=ThrowingAdviceData.class),
      @XmlElement(name="finally", type=FinallyAdviceData.class),
      @XmlElement(name="interceptor-ref", type=InterceptorRefData.class),
      @XmlElement(name="stack-ref", type=StackRefData.class)
   })
   public List<BaseInterceptorData> getInterceptors()
   {
      return interceptors;
   }

   public void setInterceptors(List<BaseInterceptorData> interceptors)
   {
      this.interceptors = interceptors;
   }
   
   
}
