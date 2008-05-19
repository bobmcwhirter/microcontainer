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
package org.jboss.kernel.plugins.annotations.wb;

import java.lang.annotation.Annotation;

import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.TypeInfo;

/**
 * Web beans injection metadata value.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class WBInjectionValueMetaData extends AbstractValueMetaData
{
   /**
    * The context
    */
   protected transient KernelControllerContext context;

   private Class<?> type;
   private Annotation[] annotations;

   public WBInjectionValueMetaData(Class<?> type, Annotation[] annotations)
   {
      this.type = type;
      this.annotations = annotations;
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      context = visitor.getControllerContext();

      visitor.addDependency(new WBInjectionDependencyItem(context.getName(), visitor.getContextState(), type, annotations));
      type = null; // nullify it

      super.initialVisit(visitor);
   }

   @SuppressWarnings("deprecation")
   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      KernelController controller = context.getKernel().getController();
      KernelControllerContext result = WBInjectionResolver.resolve(controller, info.getType(), annotations);
      if (result != null)
         return result.getTarget();
      else
         throw new IllegalArgumentException("Should not be here, dependency not resolved: " + toString());
   }
}