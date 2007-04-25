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
package org.jboss.beans.metadata.plugins.annotations;

import org.jboss.beans.metadata.plugins.CallbackCreatorUtil;
import org.jboss.classadapter.spi.DependencyBuilderListItem;
import org.jboss.dependency.spi.CallbackItem;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.kernel.plugins.dependency.AttributeInfo;
import org.jboss.kernel.plugins.dependency.CallbackDependencyItem;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Callback dependency list item from Callback info.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class CallbackDependencyBuilderListItem extends JBossObject implements DependencyBuilderListItem<KernelControllerContext>
{
   protected CallbackInfo info;
   protected AttributeInfo attribute;

   protected CallbackDependencyBuilderListItem(CallbackInfo info, AttributeInfo attribute)
   {
      if (info == null)
         throw new IllegalArgumentException("Null callback info!");
      if (attribute == null)
         throw new IllegalArgumentException("Null attribute info!");
      this.info = info;
      this.attribute = attribute;
   }

   public void addDependency(KernelControllerContext ctx)
   {
      CallbackItem<Class> callback = createCallbackItem(ctx);
      if (callback != null)
      {
         DependencyInfo dependencyInfo = ctx.getDependencyInfo();
         addCallback(dependencyInfo, callback);
         if (info.getCardinality() != null)
         {
            CallbackDependencyItem item = new CallbackDependencyItem(ctx.getName(), callback.getIDependOn(), info.getWhenRequired(), info.getDependentState(), info.getCardinality());
            dependencyInfo.addIDependOn(item);
         }
      }
   }

   /**
    * Create callback item from attribute info.
    *
    * @param ctx current context
    * @return callback item
    */
   protected CallbackItem<Class> createCallbackItem(KernelControllerContext ctx)
   {
      return CallbackCreatorUtil.createCallback(ctx, attribute, info.getWhenRequired(), info.getDependentState(), info.getCardinality());
   }

   /**
    * Add install or uninstall callback item.
    *
    * @param dependencyInfo dependency info
    * @param callback callback item
    */
   protected abstract void addCallback(DependencyInfo dependencyInfo, CallbackItem<Class> callback);

   protected void toString(JBossStringBuilder buffer)
   {
      buffer.append("callback=").append(info);
      buffer.append("attribute=").append(attribute);
   }
}
