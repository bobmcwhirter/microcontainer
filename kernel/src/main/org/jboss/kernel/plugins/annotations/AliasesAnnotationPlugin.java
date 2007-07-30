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
package org.jboss.kernel.plugins.annotations;

import java.util.Set;

import org.jboss.beans.metadata.plugins.annotations.Aliases;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.dependency.spi.Controller;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AliasesAnnotationPlugin extends ClassAnnotationPlugin<Aliases>
{
   public AliasesAnnotationPlugin()
   {
      super(Aliases.class);
   }

   protected void internalApplyAnnotation(ClassInfo info, Aliases annotation, KernelControllerContext context) throws Throwable
   {
      BeanMetaData beanMetaData = context.getBeanMetaData();
      Set<Object> aliases = beanMetaData.getAliases();
      Controller controller = context.getController();
      for(String alias : annotation.value())
      {
         if (aliases == null || aliases.contains(alias) == false)
         {
            // alias will get removed when original will be undeployed
            controller.addAlias(alias, beanMetaData.getName());
         }
      }
   }
}