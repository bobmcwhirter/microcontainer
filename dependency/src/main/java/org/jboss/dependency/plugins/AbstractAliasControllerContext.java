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
package org.jboss.dependency.plugins;

import org.jboss.dependency.spi.ControllerContextActions;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.util.JBossStringBuilder;

/**
 * Abstract alias controller context.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractAliasControllerContext extends AbstractControllerContext implements AliasControllerContext
{
   private Object alias;
   private Object original;

   public AbstractAliasControllerContext(Object alias, String id, Object original, ControllerContextActions actions)
   {
      super(alias + "_Alias_" + id, actions);
      if (alias == null)
         throw new IllegalArgumentException("Null alias");
      if (id == null)
         throw new IllegalArgumentException("Null id");
      if (original == null)
         throw new IllegalArgumentException("Null original");

      this.alias = alias;
      this.original = original;
      DependencyInfo info = getDependencyInfo();
      info.addIDependOn(new AbstractDependencyItem(getName(), original, ControllerState.INSTALLED, ControllerState.INSTANTIATED));
   }

   public Object getAlias()
   {
      return alias;
   }

   public Object getOriginal()
   {
      return original;
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("alias=").append(alias);
      buffer.append(" original=").append(original).append(" ");
      super.toString(buffer);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append("alias=").append(alias);
      buffer.append(" original=").append(original).append(" ");
      super.toShortString(buffer);
   }
}