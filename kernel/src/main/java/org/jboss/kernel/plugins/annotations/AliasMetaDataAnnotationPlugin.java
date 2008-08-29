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

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import org.jboss.beans.metadata.api.annotations.Aliases;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.reflect.spi.ClassInfo;

/**
 * Aliases annotation plugin.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AliasMetaDataAnnotationPlugin extends ClassAnnotationPlugin<Aliases>
{
   public static final AliasMetaDataAnnotationPlugin INSTANCE = new AliasMetaDataAnnotationPlugin();

   protected AliasMetaDataAnnotationPlugin()
   {
      super(Aliases.class);
   }

   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(ClassInfo info, Aliases annotation, BeanMetaData beanMetaData) throws Throwable
   {
      String[] strings = annotation.value();
      if (strings != null && strings.length > 0)
      {
         Set<Object> aliases = beanMetaData.getAliases();
         if (aliases == null)
         {
            AbstractBeanMetaData abmd = checkIfNotAbstractBeanMetaDataSpecific(beanMetaData);
            aliases = new HashSet<Object>();
            abmd.setAliases(aliases);
         }
         aliases.addAll(Arrays.asList(strings));
      }
      return null;
   }
}