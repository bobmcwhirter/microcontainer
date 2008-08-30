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
package org.jboss.test.kernel.deployment.support.container;

import java.util.ArrayList;
import java.util.List;

import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.metadata.plugins.scope.InstanceScope;
import org.jboss.metadata.spi.MetaData;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ScopedContainer
{
   private Kernel kernel;
   private MetaData metaData;

   public ScopedContainer(Kernel kernel)
   {
      if (kernel == null)
         throw new IllegalArgumentException("Null kernel.");
      this.kernel = kernel;
   }

   public void setMetaData(MetaData metaData)
   {
      this.metaData = metaData;
   }

   protected String createAnnotation(InstanceScope is)
   {
      return "@" + InstanceScope.class.getName() + "(\"" + is.value() + "\")";
   }

   public List<Object> createBeans(String baseName) throws Throwable
   {
      List<Object> result = new ArrayList<Object>();

      KernelController controller = kernel.getController();
      InstanceScope is = metaData.getAnnotation(InstanceScope.class);
      String sflAnn = createAnnotation(is);

      // bean context
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("BeanContext", BaseContext.class.getName());
      builder.addConstructorParameter(BeanContainer.class.getName(), new BeanContainer<Object>());
      builder.addAlias(baseName + "$" + "BeanContext");
      builder.addAnnotation(sflAnn);
      result.add(controller.install(builder.getBeanMetaData()).getTarget());

      // instance interceptor
      builder = BeanMetaDataBuilder.createBuilder("InstanceInterceptor", InstanceInterceptor.class.getName());
      builder.addAlias(baseName + "$" + "InstanceInterceptor");
      builder.addAnnotation(sflAnn);
      builder.addInstallWithThis("addInterceptor", "BeanContext");
      builder.addUninstallWithThis("removeInterceptor", "BeanContext");
      result.add(controller.install(builder.getBeanMetaData()).getTarget());

      return result;
   }
}
