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

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.plugins.dependency.ClassDependencyItem;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * Web beans injection dependency item.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class WBInjectionDependencyItem extends ClassDependencyItem
{
   private Annotation[] annotations;

   public WBInjectionDependencyItem(Object name, ControllerState whenRequired, Class<?> type, Annotation[] annotations)
   {
      super(name, type, whenRequired, ControllerState.INSTALLED);
      this.annotations = annotations;
   }

   public boolean resolve(Controller controller)
   {
      if (controller instanceof KernelController == false)
         throw new IllegalArgumentException("Can only handle kernel controller: " + controller);

      KernelController kernelController = (KernelController)controller;
      KernelControllerContext context = WBInjectionResolver.resolve(kernelController, getDemandClass(), annotations);
      setResolved(context != null);
      return isResolved();
   }
}