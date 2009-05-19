/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.aop.microcontainer.beans.metadata;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import org.jboss.aop.microcontainer.beans.DeclareDefinition;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.util.id.GUID;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractDeclareBeanMetaDataFactory extends AspectManagerAwareBeanMetaDataFactory
{
   String expr;
   
   String message;
   
   public String getExpr()
   {
      return expr;
   }

   @XmlAttribute
   public void setExpr(String expr)
   {
      this.expr = expr;
   }

   public String getMessage()
   {
      return message;
   }

   @XmlValue
   public void setMessage(String message)
   {
      this.message = message;
   }

   @Override
   public List<BeanMetaData> getBeans()
   {
      ArrayList<BeanMetaData> beans = new ArrayList<BeanMetaData>();
      
      if (name == null)
      {
          name = GUID.asString();
      }
      BeanMetaDataBuilder builder = AOPBeanMetaDataBuilder.createBuilder(name, DeclareDefinition.class.getName());
      builder.addPropertyMetaData("expr", expr);
      builder.addPropertyMetaData("name", name);
      builder.addPropertyMetaData("message", message);
      builder.addPropertyMetaData("warning", isWarning());
      setAspectManagerProperty(builder);
      
      beans.add(builder.getBeanMetaData());
      
      return beans;
   }

   protected abstract boolean isWarning();
}
