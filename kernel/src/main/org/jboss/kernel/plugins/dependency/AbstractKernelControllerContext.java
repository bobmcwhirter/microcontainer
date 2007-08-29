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
package org.jboss.kernel.plugins.dependency;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.plugins.AbstractControllerContext;
import org.jboss.dependency.plugins.AbstractDependencyInfo;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.util.JBossStringBuilder;

/**
 * Controller context.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractKernelControllerContext extends AbstractControllerContext implements KernelControllerContext
{
   /** The default actions */
   private static final KernelControllerContextActions actions = KernelControllerContextActions.getInstance();

   /** The no instantiate actions */
   private static final KernelControllerContextActions noInstantiate = KernelControllerContextActions.getNoInstantiate();

   /** The BeanInfo */
   protected BeanInfo info;

   /** The meta data */
   protected BeanMetaData metaData;

   /** The access control context */
   protected AccessControlContext accessContext;

   /** Did we do a initialVisit */
   protected boolean isInitialProcessed;

   /** Did we do a describeVisit */
   protected boolean isDescribeProcessed;

   /**
    * Create an abstract controller context
    *
    * @param info     the bean info
    * @param metaData the meta data
    * @param target   any target object
    */
   public AbstractKernelControllerContext(BeanInfo info, BeanMetaData metaData, Object target)
   {
      super(metaData.getName(), metaData.getAliases(), target == null ? actions : noInstantiate, new AbstractDependencyInfo(), target);
      this.info = info;
      this.metaData = metaData;
      ControllerMode mode = metaData.getMode();
      if (mode != null)
         setMode(mode);
      boolean autowireCandidate = metaData.isAutowireCandidate();
      getDependencyInfo().setAutowireCandidate(autowireCandidate);
      if (System.getSecurityManager() != null)
         accessContext = AccessController.getContext();
      initKernelScopeInfo();
   }

   public Kernel getKernel()
   {
      KernelController controller = (KernelController) getController();
      if (controller == null)
         throw new IllegalStateException("Context is not installed in controller");
      return controller.getKernel();
   }

   public BeanInfo getBeanInfo()
   {
      return info;
   }

   /**
    * Set the bean info
    *
    * @param info the bean info
    */
   public void setBeanInfo(BeanInfo info)
   {
      this.info = info;
      infoprocessMetaData();
      flushJBossObjectCache();
   }

   public BeanMetaData getBeanMetaData()
   {
      return metaData;
   }

   public void toString(JBossStringBuilder buffer)
   {
      if (metaData != null)
         buffer.append(" metadata=").append(metaData);
      super.toString(buffer);
   }

   public void setController(Controller controller)
   {
      super.setController(controller);
      preprocessMetaData();
   }

   @Override
   protected void initScopeInfo()
   {
      // nothing
   }

   protected void initKernelScopeInfo()
   {
      String className = null;
      Object target = getTarget();
      if (target != null)
         className = target.getClass().getName();
      BeanMetaData bmd = getBeanMetaData();
      if (bmd != null)
      {
         String bean = bmd.getBean();
         if (bean != null)
            className = bean;
      }
      setScopeInfo(new KernelScopeInfo(getName(), className));
   }

   /**
    * Preprocess the metadata for this context
    */
   protected void preprocessMetaData()
   {
      if (metaData == null)
         return;
      if (isInitialProcessed) return;
      PreprocessMetaDataVisitor visitor = new PreprocessMetaDataVisitor(metaData, this);
      AccessController.doPrivileged(visitor);
      isInitialProcessed = true;
   }

   /**
    * Preprocess the metadata for this context
    */
   protected void infoprocessMetaData()
   {
      if (info == null)
      {
         removeClassContextReference();
         return;
      }
      if (isDescribeProcessed) return;
      DescribedMetaDataVisitor visitor = new DescribedMetaDataVisitor(metaData, this);
      AccessController.doPrivileged(visitor);
      isDescribeProcessed = true;
   }

   /**
    * Get the access control context of the code that created this context.<p>
    * <p/>
    * This will be null when there is no security manager.
    *
    * @return any access control context
    */
   protected AccessControlContext getAccessControlContext()
   {
      return accessContext;
   }

   private void removeClassContextReference()
   {
      DependencyInfo dependencyInfo = getDependencyInfo();
      if (dependencyInfo != null)
      {
         // remove all dependency items that hold class ref
         Set<DependencyItem> dependencys = dependencyInfo.getIDependOn(ClassContextDependencyItem.class);
         dependencys.addAll(dependencyInfo.getIDependOn(CallbackDependencyItem.class));
         for (DependencyItem di : dependencys)
         {
            // can cast because of getIDepend method impl
            ClassDependencyItem cdi = (ClassDependencyItem)di;
            cdi.clear(getController());
         }
      }
   }

   public Object get(final String name) throws Throwable
   {
      if (info == null)
         throw new IllegalArgumentException("Null BeanInfo");
      return info.getProperty(getTarget(), name);
   }

   public void set(final String name, final Object value) throws Throwable
   {
      if (info == null)
         throw new IllegalArgumentException("Null BeanInfo");
      info.setProperty(getTarget(), name, value);
   }

   public Object invoke(final String name, final Object[] parameters, final String[] signature) throws Throwable
   {
      if (info == null)
         throw new IllegalArgumentException("Null BeanInfo");
      return info.invoke(getTarget(), name, signature, parameters);
   }

   public ClassLoader getClassLoader() throws Throwable
   {
      return Configurator.getClassLoader(getBeanMetaData());
   }
}