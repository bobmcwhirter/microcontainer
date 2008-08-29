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

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.util.id.GUID;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractAnnotationBeanMetaDataFactory extends AspectManagerAwareBeanMetaDataFactory
{
   private static final long serialVersionUID = 1L;

   String expr;
   
   boolean invisible;
   
   String annotation;

   public String getExpr()
   {
      return expr;
   }

   @XmlAttribute
   public void setExpr(String expr)
   {
      this.expr = expr;
   }

   public boolean isInvisible()
   {
      return invisible;
   }

   @XmlAttribute
   public void setInvisible(boolean invisible)
   {
      this.invisible = invisible;
   }

   public String getAnnotation()
   {
      return annotation;
   }

   @XmlValue
   public void setAnnotation(String annotation)
   {
      this.annotation = annotation;
   }

   @Override
   public List<BeanMetaData> getBeans()
   {
      ArrayList<BeanMetaData> result = new ArrayList<BeanMetaData>();
      if (name == null)
      {
         name = GUID.asString();
      }
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder(name, getBeanClassName());
      builder.addPropertyMetaData("invisible", invisible);
      builder.addPropertyMetaData("expr", expr);
      builder.addPropertyMetaData("annotation", annotation);
      
      setAspectManagerProperty(builder);
      
      result.add(builder.getBeanMetaData());
      
      return result;
   }

   
   protected abstract String getBeanClassName();
}
