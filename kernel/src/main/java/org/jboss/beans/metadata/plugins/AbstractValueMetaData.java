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

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.dependency.plugins.JMXObjectNameFix;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;
import org.jboss.xb.annotations.JBossXmlNoElements;

/**
 * Plain value.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
@XmlType
@JBossXmlNoElements
public class AbstractValueMetaData extends JBossObject
   implements ValueMetaData, TypeProvider, Serializable
{
   private static final long serialVersionUID = 2L;

   /**
    * The value
    */
   private Object value;

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
      setValue(value);
   }

   @XmlTransient
   public Object getValue()
   {
      return value;
   }

   public void setValue(Object value)
   {
      Object jmxHack = null;
      if (isUseJMXObjectNameFix())
         jmxHack = JMXObjectNameFix.needsAnAlias(value);

      if (jmxHack != null)
         this.value = jmxHack;
      else
         this.value = value;
      
      flushJBossObjectCache();
   }

   @XmlTransient
   public Object getUnderlyingValue()
   {
      return value;
   }

   /**
    * Do we use jmx object name fix.
    *
    * @return do we use jmx object name fix
    */
   protected boolean isUseJMXObjectNameFix()
   {
      return true;
   }

   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      return info != null ? info.convertValue(value) : value;
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      visitor.initialVisit(this);
   }

   public void describeVisit(MetaDataVisitor visitor)
   {
      visitor.describeVisit(this);
   }

   public TypeInfo getType(MetaDataVisitor visitor, MetaDataVisitorNode previous) throws Throwable
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

   @XmlTransient
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

   public AbstractValueMetaData clone()
   {
      AbstractValueMetaData clone = (AbstractValueMetaData)super.clone();
      if (value instanceof MetaDataVisitorNode)
         clone.setValue(CloneUtil.cloneObject((MetaDataVisitorNode)value, MetaDataVisitorNode.class));
      return clone;
   }
}
