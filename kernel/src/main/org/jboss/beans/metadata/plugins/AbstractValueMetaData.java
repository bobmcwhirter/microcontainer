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

import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Plain value.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractValueMetaData extends JBossObject implements ValueMetaData, TypeProvider
{
   /**
    * The value
    */
   protected Object value;

   /**
    * Create a new plain value
    */
   public AbstractValueMetaData()
   {
   }

   /**
    * Create a new plain value
    *
    * @param value the value
    */
   public AbstractValueMetaData(Object value)
   {
      this.value = value;
   }

   public Object getValue()
   {
      return value;
   }

   public void setValue(Object value)
   {
      this.value = value;
      flushJBossObjectCache();
   }

   public Object getUnderlyingValue()
   {
      return value;
   }

   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      return info != null ? info.convertValue(value) : value;
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      visitor.initialVisit(this);
   }

   public void describeVisit(MetaDataVisitor vistor)
   {
      vistor.describeVisit(this);
   }

   public Class getType(MetaDataVisitor visitor, MetaDataVisitorNode previous) throws Throwable
   {
      Stack<MetaDataVisitorNode> visitorNodeStack = visitor.visitorNodeStack();
      // see AbstractInjectionValueMetaData.describeVisit
      MetaDataVisitorNode node = visitorNodeStack.pop();
      try
      {
         if (node instanceof TypeProvider)
         {
            TypeProvider typeProvider = (TypeProvider) node;
            return typeProvider.getType(visitor, this);
         }
         else
         {
            throw new IllegalArgumentException(TypeProvider.ERROR_MSG);
         }
      }
      finally
      {
         visitorNodeStack.push(node);
      }
   }

   public Iterator<? extends MetaDataVisitorNode> getChildren()
   {
      if (value instanceof MetaDataVisitorNode)
         return Collections.singletonList((MetaDataVisitorNode) value).iterator();
      return null;
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("value=").append(value);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(value);
   }
}
