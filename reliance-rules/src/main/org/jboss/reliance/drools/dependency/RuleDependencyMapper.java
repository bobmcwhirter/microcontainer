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
package org.jboss.reliance.drools.dependency;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.dependency.spi.DependencyItem;

/**
 * Applys rule dependency to injected ControllerContext.
 *
 * @param <T> exact controller context type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class RuleDependencyMapper<T extends ControllerContext>
{
   private DependencyItemFactory<T> factory;

   public void create()
   {
      if (factory == null)
         throw new IllegalArgumentException("DependencyFactory is null!");
   }

   public void install(T context)
   {
      DependencyInfo dependencyInfo = context.getDependencyInfo();
      DependencyItem item = factory.createDependencyItem(context);
      dependencyInfo.addIDependOn(item);
   }

   public void uninstall(T context)
   {      
   }

   public void setFactory(DependencyItemFactory<T> factory)
   {
      this.factory = factory;
   }
}
