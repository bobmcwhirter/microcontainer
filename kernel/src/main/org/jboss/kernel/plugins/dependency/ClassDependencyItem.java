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
package org.jboss.kernel.plugins.dependency;

import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.util.JBossStringBuilder;

/**
 * A class dependant dependencyItem.
 *
 * @author <a href="ales.justin@gmail.com">Ales Justin</a>
 */
public abstract class ClassDependencyItem extends AbstractDependencyItem
{
   public ClassDependencyItem(Object name, Class demandClass, ControllerState whenRequired, ControllerState dependentState)
   {
      super(name, demandClass, whenRequired, dependentState);
   }

   protected Class getDemandClass()
   {
      return (Class)getIDependOn();
   }

   public void unresolved(Controller controller)
   {
      setIDependOn(null);
      super.unresolved(controller);
   }

   public void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      buffer.append(" demandClass=").append(getIDependOn());
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(getName()).append(" demands ").append(getIDependOn());
   }

}
