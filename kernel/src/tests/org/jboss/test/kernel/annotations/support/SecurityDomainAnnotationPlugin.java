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
package org.jboss.test.kernel.annotations.support;

import java.util.List;

import org.jboss.beans.metadata.api.annotations.Dependency;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.kernel.plugins.annotations.ClassAnnotationPlugin;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.metadata.spi.MetaData;
import org.jboss.reflect.spi.ClassInfo;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SecurityDomainAnnotationPlugin extends ClassAnnotationPlugin<SecurityDomain>
{
   public SecurityDomainAnnotationPlugin()
   {
      super(SecurityDomain.class);
   }

   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(ClassInfo info, MetaData retrieval, SecurityDomain annotation, KernelControllerContext context) throws Throwable
   {
      Dependency dependency = annotation.annotationType().getAnnotation(Dependency.class);
      if (dependency == null)
         throw new IllegalArgumentException("Null @Dependency.");

      DependencyInfo dependencies = context.getDependencyInfo();

      SecurityDomainDependencyFactory factory = null;
      // try to find existing security domain dependency factory
      // or what ever kind of lookup
      KernelController controller = context.getKernel().getController();
      ControllerContext smCC = controller.getInstalledContext(annotation.securityManagerName());
      if (smCC != null)
      {
         Object target = smCC.getTarget();
         if (target != null && target instanceof SecurityDomainDependencyFactory)
            factory = SecurityDomainDependencyFactory.class.cast(target);
      }

      if (factory == null)
         factory = (SecurityDomainDependencyFactory)dependency.factory().newInstance();

      DependencyItem item = factory.createDependencyItem(annotation, dependency);
      dependencies.addIDependOn(item);

      return null;
   }
}
