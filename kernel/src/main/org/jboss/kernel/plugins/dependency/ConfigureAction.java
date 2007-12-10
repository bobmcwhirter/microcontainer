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

import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.info.plugins.BeanInfoUtil;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * New ConfigureAction.
 * @see org.jboss.kernel.plugins.dependency.OldConfigureAction
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class ConfigureAction extends AbstractConfigureAction
{
   protected void installActionInternal(KernelControllerContext context) throws Throwable
   {
      Object object = context.getTarget();
      BeanInfo info = context.getBeanInfo();
      BeanMetaData metaData = context.getBeanMetaData();
      setAttributes(object, info, metaData, false);

      installKernelControllerContextAware(context);
   }

   protected void uninstallActionInternal(KernelControllerContext context)
   {
      uninstallKernelControllerContextAware(context);

      Object object = context.getTarget();
      BeanInfo info = context.getBeanInfo();
      BeanMetaData metaData = context.getBeanMetaData();
      try
      {
         setAttributes(object, info, metaData, true);
      }
      catch (Throwable t)
      {
         log.warn("Error unconfiguring bean " + context, t);
      }
   }

   /**
    * Set attributes/properties.
    *
    * @param target the target
    * @param info the bean info
    * @param metaData the bean metadata
    * @param nullyfy should we nullyfy attributes/properties
    * @throws Throwable for any error
    */
   protected void setAttributes(Object target, BeanInfo info, BeanMetaData metaData, boolean nullyfy) throws Throwable
   {
      Set<PropertyMetaData> propertys = metaData.getProperties();
      if (propertys != null && propertys.isEmpty() == false)
      {
         ClassLoader cl = null;
         if (nullyfy == false)
            cl = Configurator.getClassLoader(metaData);

         for(PropertyMetaData property : propertys)
         {
            dispatchSetProperty(property, nullyfy, info, target, cl);
         }
      }
   }

   /**
    * Dispatch property set
    *
    * @param property the property
    * @param nullyfy should we nullyfy
    * @param info the bean info
    * @param target the target
    * @param cl classloader
    * @throws Throwable for any error
    */
   // TODO - wrap with MetaDataStack push and ContextCL change?
   protected void dispatchSetProperty(PropertyMetaData property, boolean nullyfy, BeanInfo info, Object target, ClassLoader cl)
         throws Throwable
   {
      String name = property.getName();
      if (nullyfy)
      {
         info.setProperty(target, name, null);
      }
      else
      {
         PropertyInfo propertyInfo = BeanInfoUtil.getPropertyInfo(info, target, name);
         ValueMetaData valueMetaData = property.getValue();
         Object value = valueMetaData.getValue(propertyInfo.getType(), cl);
         info.setProperty(target, name, value);
      }
   }
}