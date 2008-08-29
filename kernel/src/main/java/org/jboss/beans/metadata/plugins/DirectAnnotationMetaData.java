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
import java.lang.annotation.Annotation;
import java.util.Iterator;

import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Metadata for an annotation.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class DirectAnnotationMetaData extends JBossObject implements AnnotationMetaData, Serializable
{
   private static final long serialVersionUID = 1L;

   private Annotation annotation;

   public DirectAnnotationMetaData(Annotation annotation)
   {
      if (annotation == null)
         throw new IllegalArgumentException("Null annotation");

      this.annotation = annotation;
   }

   public Annotation getAnnotationInstance()
   {
      return annotation;
   }

   public Annotation getAnnotationInstance(ClassLoader cl)
   {
      return annotation;
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      visitor.initialVisit(this);
   }

   public void describeVisit(MetaDataVisitor vistor)
   {
      vistor.describeVisit(this);
   }

   public Iterator<? extends MetaDataVisitorNode> getChildren()
   {
      return null;
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("annotationr=").append(annotation);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(annotation);
   }

   protected int getHashCode()
   {
      return annotation.hashCode();
   }

   public boolean equals(Object object)
   {
      if (object == null || object instanceof DirectAnnotationMetaData == false)
         return false;

      DirectAnnotationMetaData damd = (DirectAnnotationMetaData)object;
      return annotation.equals(damd.annotation);
   }

   public DirectAnnotationMetaData clone()
   {
      return (DirectAnnotationMetaData)super.clone();
   }
}