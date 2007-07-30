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

import org.jboss.beans.metadata.plugins.AbstractSetMetaData;
import org.jboss.beans.metadata.plugins.annotations.SetValue;
import org.jboss.beans.metadata.plugins.annotations.Value;
import org.jboss.beans.metadata.spi.ValueMetaData;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SetValueAnnotationPlugin extends CollectionsAnnotationPlugin<SetValue>
{
   static SetValueAnnotationPlugin INSTANCE = new SetValueAnnotationPlugin();

   public SetValueAnnotationPlugin()
   {
      super(SetValue.class);
   }

   public ValueMetaData createValueMetaData(SetValue annotation)
   {
      AbstractSetMetaData set = new AbstractSetMetaData();
      if (isAttributePresent(annotation.clazz()))
         set.setType(annotation.clazz());
      if (isAttributePresent(annotation.elementClass()))
         set.setElementType(annotation.elementClass());
      for(Value value : annotation.value())
         set.add(createValueMetaData(value));
      return set;
   }
}
