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
package org.jboss.beans.metadata.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.LazyMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.xb.annotations.JBossXmlSchema;

/**
 * Lazy metadata.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@JBossXmlSchema(namespace="urn:jboss:bean-deployer:2.0", elementFormDefault=XmlNsForm.QUALIFIED)
@XmlRootElement(name="lazy")
@XmlType(propOrder={"interfaces"})
public class AbstractLazyMetaData extends AbstractBeanMetaData implements LazyMetaData
{
   private static final long serialVersionUID = 2L;

   private String beanName;
   private boolean exposeClass;
   private Set<String> interfaces;

   private String factoryClassName;

   public AbstractLazyMetaData()
   {
      setAutowireCandidate(false);
      setFactoryClassName("org.jboss.kernel.spi.lazy.LazyProxyFactory");
   }

   public AbstractLazyMetaData(String beanName)
   {
      this();
      this.beanName = beanName;
      setName(beanName + "Proxy");
   }

   public Object getUnderlyingValue()
   {
      return beanName;
   }

   public void initialVisit(MetaDataVisitor vistor)
   {
      if (beanName == null)
         throw new IllegalArgumentException("Null bean name.");

      KernelController controller = (KernelController)vistor.getControllerContext().getController();
      Kernel kernel = controller.getKernel();
      AbstractConstructorMetaData constructor = new AbstractConstructorMetaData();
      constructor.setFactoryClass(factoryClassName);
      constructor.setFactoryMethod("getProxy");
      List<ParameterMetaData> parameters = new ArrayList<ParameterMetaData>();
      parameters.add(new AbstractParameterMetaData(kernel));
      parameters.add(new AbstractParameterMetaData(beanName));
      parameters.add(new AbstractParameterMetaData(Boolean.TYPE.getName(), exposeClass));
      parameters.add(new AbstractParameterMetaData(new AbstractValueMetaData(interfaces)));
      constructor.setParameters(parameters);
      setConstructor(constructor);

      vistor.addDependency(new AbstractDependencyItem(getName(), beanName, ControllerState.INSTANTIATED, ControllerState.DESCRIBED));
      super.initialVisit(vistor);
   }

   public Iterator<? extends MetaDataVisitorNode> getChildren()
   {
      return null;
   }

   public List<BeanMetaData> getBeans()
   {
      return Collections.singletonList((BeanMetaData)this);
   }

   protected void setFactoryClassName(String factoryClassName)
   {
      this.factoryClassName = factoryClassName;
   }

   public String getBeanName()
   {
      return beanName;
   }

   @XmlAttribute(name="bean")
   public void setBeanName(String beanName)
   {
      this.beanName = beanName;
      if (getName() == null)
         setName(beanName + "Proxy");
   }

   public boolean isExposeClass()
   {
      return exposeClass;
   }

   @XmlAttribute
   public void setExposeClass(boolean exposeClass)
   {
      this.exposeClass = exposeClass;
   }

   public Set<String> getInterfaces()
   {
      return interfaces;
   }

   @XmlElement(name="interface", type=String.class)
   public void setInterfaces(Set<String> interfaces)
   {
      this.interfaces = interfaces;
   }
}
