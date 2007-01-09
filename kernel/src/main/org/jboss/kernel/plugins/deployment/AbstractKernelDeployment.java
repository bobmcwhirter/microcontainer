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
package org.jboss.kernel.plugins.deployment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.ClassLoaderMetaData;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * An abstract kernel deployment.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractKernelDeployment extends JBossObject
   implements KernelDeployment, Serializable
{
   private static final long serialVersionUID = 1;

   /** The name of the deployment */
   protected String name;

   /** Whether it is installed */
   protected boolean installed;

   /** The installed contexts */
   protected transient List<KernelControllerContext> installedContexts = new CopyOnWriteArrayList<KernelControllerContext>();

   /** The beans List<BeanMetaDataFactory> */
   protected List<BeanMetaDataFactory> beanFactories;

   /** The ClassLoader */
   protected ClassLoaderMetaData classLoader;

   /**
    * Create a new kernel deployment
    */
   public AbstractKernelDeployment()
   {
   }

   /**
    * Set the bean factories.
    *
    * @deprecated use setBeanFactories
    * @param beans a List<BeanMetaData>.
    */
   @SuppressWarnings("unchecked")
   public void setBeans(List beans)
   {
      this.beanFactories = beans;
      flushJBossObjectCache();
   }

   /**
    * Set the bean factories.
    * 
    * @param beanFactories a List<BeanMetaDataFactory>.
    */
   public void setBeanFactories(List<BeanMetaDataFactory> beanFactories)
   {
      this.beanFactories = beanFactories;
      flushJBossObjectCache();
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
      flushJBossObjectCache();
   }

   public boolean isInstalled()
   {
      return installed;
   }

   public void setInstalled(boolean installed)
   {
      this.installed = installed;
      flushJBossObjectCache();
   }

   public void addInstalledContext(KernelControllerContext context)
   {
      installedContexts.add(context);
      flushJBossObjectCache();
   }

   public List<KernelControllerContext> getInstalledContexts()
   {
      return installedContexts;
   }

   public void removeInstalledContext(KernelControllerContext context)
   {
      installedContexts.remove(context);
      flushJBossObjectCache();
   }

   public List<BeanMetaData> getBeans()
   {
      if (beanFactories == null || beanFactories.size() == 0)
         return null;
      List<BeanMetaData> result = new ArrayList<BeanMetaData>(beanFactories.size());
      for (int i = 0; i < beanFactories.size(); ++i)
      {
         BeanMetaDataFactory factory = beanFactories.get(i);
         List<BeanMetaData> beans = factory.getBeans();
         result.addAll(beans);
      }
      return result;
   }

   public List<BeanMetaDataFactory> getBeanFactories()
   {
      return beanFactories;
   }

   public ClassLoaderMetaData getClassLoader()
   {
      return classLoader;
   }

   /**
    * Set the classloader
    * 
    * @param classLoader the classloader metadata
    */
   public void setClassLoader(ClassLoaderMetaData classLoader)
   {
      this.classLoader = classLoader;
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("name=").append(name);
      buffer.append(" installed=").append(installed);
      if (classLoader != null)
         buffer.append(" classLoader=").append(classLoader);
      if (beanFactories != null)
         buffer.append(" beanFactories=").append(beanFactories);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(name);
   }
}