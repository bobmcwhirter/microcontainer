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
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.jboss.beans.metadata.spi.RelatedClassMetaData;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Metadata for a related classes.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
@XmlType(name="relatedClassType", propOrder={"enabled"})
public class AbstractRelatedClassMetaData extends JBossObject implements RelatedClassMetaData, Serializable
{
   private static final long serialVersionUID = 1L;

   private String className;
   private Set<Object> enabled;

   public String getClassName()
   {
      return className;
   }

   @XmlAttribute(name = "name")
   public void setClassName(String className)
   {
      this.className = className;
   }

   public Set<Object> getEnabled()
   {
      return enabled;
   }

   @XmlAnyElement
   public void setEnabled(Set<Object> enabled)
   {
      this.enabled = enabled;
   }

   @XmlValue
   public void setEnabledValue(Object value)
   {
      this.enabled = Collections.singleton(value);
   }

   public <T> T getEnabled(Class<T> type)
   {
      if (type == null)
         throw new IllegalArgumentException("Null type");

      if (enabled == null || enabled.isEmpty())
         return null;

      for (Object element : enabled)
      {
         if (type.isInstance(element))
            return type.cast(element);
      }

      return null;
   }

   protected void toString(JBossStringBuilder buffer)
   {
      buffer.append("name=").append(className);
      buffer.append(", enabled=").append(enabled);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(className);
   }

   public AbstractRelatedClassMetaData clone()
   {
      AbstractRelatedClassMetaData clone = (AbstractRelatedClassMetaData)super.clone();
      doClone(clone);
      return clone;
   }

   @SuppressWarnings("unchecked")
   protected void doClone(AbstractRelatedClassMetaData clone)
   {
      if (enabled != null)
         clone.setEnabled(new HashSet<Object>(enabled));
   }
}