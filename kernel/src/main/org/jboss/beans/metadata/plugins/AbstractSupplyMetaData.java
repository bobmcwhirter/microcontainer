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

import java.beans.PropertyEditor;
import java.io.Serializable;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.XmlAttribute;

import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.SupplyMetaData;
import org.jboss.logging.Logger;
import org.jboss.util.HashCode;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;
import org.jboss.util.propertyeditor.PropertyEditors;

/**
 * A supply.
 * 
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
@XmlType(name="supplyType")
public class AbstractSupplyMetaData extends JBossObject
   implements SupplyMetaData, Serializable
{
   private static final long serialVersionUID = 2L;

   private static Logger log = Logger.getLogger(AbstractSupplyMetaData.class);

   static
   {
      try
      {
         PropertyEditors.init();
      }
      catch (Throwable t)
      {
         log.debug("Unable to initialise property editors", t);
      }
   }

   /** The supply */
   protected Object supply;

   /** The type */
   protected String type;

   /**
    * Create a new supply
    */
   public AbstractSupplyMetaData()
   {
   }

   /**
    * Create a new supply
    * 
    * @param supply the supply
    */
   public AbstractSupplyMetaData(Object supply)
   {
      this.supply = supply;
   }
   
   /**
    * Set the supply
    * 
    * @param supply the supply
    */
   @XmlValue
   public void setSupply(Object supply)
   {
      this.supply = supply;
      flushJBossObjectCache();
   }

   /**
    * Get the class type.
    *
    * @return the class type
    */
   public String getType()
   {
      return type;
   }

   /**
    * Set the class type
    *
    * @param type the type
    */
   @XmlAttribute(name="class")
   public void setType(String type)
   {
      this.type = type;
   }

   public Object getSupply()
   {
      if (supply instanceof String && type != null)
      {
         PropertyEditor editor = getPropertyEditor();
         editor.setAsText((String)supply);
         return editor.getValue();
      }
      return supply;
   }

   protected PropertyEditor getPropertyEditor()
   {
      try
      {
         return PropertyEditors.getEditor(type);
      }
      catch (ClassNotFoundException e)
      {
         throw new RuntimeException(e);
      }
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      visitor.initialVisit(this);
   }

   public void describeVisit(MetaDataVisitor vistor)
   {
      vistor.describeVisit(this);
   }

   @XmlTransient
   public Iterator<? extends MetaDataVisitorNode> getChildren()
   {
      return null;
   }
   
   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("supply=").append(supply);
      if (type != null)
         buffer.append(" class=").append(type);
   }
   
   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(supply);
   }

   public boolean equals(Object obj)
   {
      if (obj instanceof AbstractSupplyMetaData == false)
         return false;
      return equals(supply, ((AbstractSupplyMetaData)obj).supply) && equals(type, ((AbstractSupplyMetaData)obj).type);
   }

   protected int getHashCode()
   {
      return HashCode.generate(supply);
   }
}
