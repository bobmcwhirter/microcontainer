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
package org.jboss.beans.metadata.plugins.policy;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.jboss.beans.metadata.plugins.AbstractArrayMetaData;
import org.jboss.beans.metadata.plugins.AbstractCollectionMetaData;
import org.jboss.beans.metadata.plugins.AbstractInjectionValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractListMetaData;
import org.jboss.beans.metadata.plugins.AbstractMapMetaData;
import org.jboss.beans.metadata.plugins.AbstractSetMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueFactoryMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractSearchValueMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.plugins.ThisValueMetaData;
import org.jboss.beans.metadata.plugins.ValueMetaDataAware;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.policy.BindingMetaData;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Meta data for bindings.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@XmlRootElement(name="binding")
@XmlType(name="bindingType")
public class AbstractBindingMetaData extends JBossObject implements BindingMetaData, ValueMetaDataAware, Serializable
{
   private static final long serialVersionUID = 2;

   private String name;
   private String type;
   private ValueMetaData value;

   public String getName()
   {
      return name;
   }

   public String getType()
   {
      return type;
   }

   public ValueMetaData getValue()
   {
      return value;
   }

   @XmlAttribute
   public void setName(String name)
   {
      this.name = name;
   }

   @XmlAttribute(name = "class")
   public void setType(String type)
   {
      this.type = type;
   }

   @XmlElements
   ({
      @XmlElement(name="array", type=AbstractArrayMetaData.class),
      @XmlElement(name="collection", type=AbstractCollectionMetaData.class),
      @XmlElement(name="inject", type=AbstractInjectionValueMetaData.class),
      @XmlElement(name="search", type= AbstractSearchValueMetaData.class),
      @XmlElement(name="list", type=AbstractListMetaData.class),
      @XmlElement(name="map", type=AbstractMapMetaData.class),
      @XmlElement(name="null", type=AbstractValueMetaData.class),
      @XmlElement(name="set", type=AbstractSetMetaData.class),
      @XmlElement(name="this", type=ThisValueMetaData.class),
      @XmlElement(name="value", type=StringValueMetaData.class),
      @XmlElement(name="value-factory", type=AbstractValueFactoryMetaData.class)
   })
   public void setValue(ValueMetaData value)
   {
      this.value = value;
   }

   @XmlAnyElement
   public void setValueObject(Object value)
   {
      if (value == null)
         setValue(null);
      else if (value instanceof ValueMetaData)
         setValue((ValueMetaData) value);
      else
         setValue(new AbstractValueMetaData(value));
   }

   @XmlValue
   public void setValueString(String value)
   {
      if (value == null)
         setValue(null);
      else
      {
         ValueMetaData valueMetaData = getValue();
         if (valueMetaData instanceof StringValueMetaData)
         {
            ((StringValueMetaData) valueMetaData).setValue(value);
         }
         else
         {
            StringValueMetaData stringValue = new StringValueMetaData(value);
            stringValue.setType(getType());
            setValue(stringValue);
         }
      }
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("name=").append(name);
      buffer.append(" value=").append(value);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(name);
      buffer.append('/');
      buffer.append(value);
   }
}
