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

import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.TypeInfo;

/**
 * Dependency value.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ThisValueMetaData extends AbstractValueMetaData
{
   /** The controller */
   protected KernelController controller;

   /**
    * Create a new dependency value
    */
   public ThisValueMetaData()
   {
   }
   
   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      ControllerContext context = controller.getContext(value, ControllerState.INSTANTIATED);
      if (context == null)
         throw new Error("Could not deference this " + this);
      Object result = context.getTarget();
      return result;
   }

   public void visit(MetaDataVisitor visitor)
   {
      KernelControllerContext controllerContext = visitor.getControllerContext();
      controller = (KernelController) controllerContext.getController();
      value = controllerContext.getName();
      ControllerState whenRequired = visitor.getContextState();

      DependencyItem item = new AbstractDependencyItem(value, value, whenRequired, ControllerState.INSTANTIATED);
      visitor.addDependency(item);

      super.visit(visitor);
   }
}
