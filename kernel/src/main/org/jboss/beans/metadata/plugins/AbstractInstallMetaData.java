/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAttribute;

import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.util.JBossStringBuilder;

/**
 * Metadata for installation.
 * 
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
@XmlType(name="installType", propOrder={"annotations", "parameters"})
public class AbstractInstallMetaData extends AbstractLifecycleMetaData
   implements InstallMetaData, Serializable
{
   private static final long serialVersionUID = 2L;

   /** The bean name */
   protected String bean;

   /** The required state of the dependency or null to look in the registry */
   protected ControllerState dependentState = ControllerState.INSTALLED;

   /**
    * Create a new install meta data
    */
   public AbstractInstallMetaData()
   {
      setState(ControllerState.INSTALLED);
   }

   public String getBean()
   {
      return bean;
   }

   /**
    * Set the bean
    * 
    * @param bean the bean
    */
   @XmlAttribute
   public void setBean(String bean)
   {
      this.bean = bean;
   }

   /**
    * Set the required state of the dependency
    * 
    * @param dependentState the required state or null if it must be in the registry
    */
   @XmlAttribute(name="state")
   public void setDependentState(ControllerState dependentState)
   {
      this.dependentState = dependentState;
      flushJBossObjectCache();
   }

   public ControllerState getDependentState()
   {
      return dependentState;
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      if (getMethodName() == null)
         throw new IllegalArgumentException("Install/uninstall should have method attribute.");

      KernelControllerContext context = visitor.getControllerContext();
      if (bean != null)
      {
         DependencyItem item = new InstallationDependencyItem(context.getName());
         visitor.addDependency(item);
      }
      super.initialVisit(visitor);
   }

   protected ClassInfo getClassInfo(KernelControllerContext context) throws Throwable
   {
      if (bean != null)
      {
         KernelController controller = (KernelController) context.getController();
         ControllerContext beanContext = controller.getContext(bean, ControllerState.INSTANTIATED);
         if (beanContext != null)
         {
            KernelConfigurator configurator = controller.getKernel().getConfigurator();
            Object target = beanContext.getTarget();
            return configurator.getClassInfo(target.getClass());
         }
         else
         {
            throw new IllegalArgumentException("Cannot determine install bean class: " + this);
         }
      }
      return super.getClassInfo(context);
   }

   public void toString(JBossStringBuilder buffer)
   {
      if (bean != null)
         buffer.append("bean=").append(bean);
      if (ControllerState.INSTALLED.equals(dependentState) == false)
         buffer.append(" dependentState=" + dependentState);
      buffer.append(" ");
      super.toString(buffer);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      if (bean != null)
      {
         buffer.append(bean).append(".");
      }
      if (methodName != null)
         buffer.append(methodName);
   }

   /**
    * An InstallationDependencyItem.
    */
   public class InstallationDependencyItem extends AbstractDependencyItem
   {
      /**
       * Create a new InstallationDependencyItem.
       * 
       * @param name the name
       */
      public InstallationDependencyItem(Object name)
      {
         super(name, bean, state, dependentState);
      }
   }
}
