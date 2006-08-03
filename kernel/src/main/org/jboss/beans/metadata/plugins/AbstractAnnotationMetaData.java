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

import java.lang.annotation.Annotation;
import java.util.Iterator;

import org.jboss.annotation.factory.AnnotationCreator;
import org.jboss.annotation.factory.ast.TokenMgrError;
import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Metadata for an annotation.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractAnnotationMetaData extends JBossObject implements AnnotationMetaData
{
   public String annotation;

   protected Annotation ann;
   
   /**
    * Create a new annotation meta data
    */
   public AbstractAnnotationMetaData()
   {
      super();
   }

   public String getAnnotation()
   {
      return annotation;
   }

   public void setAnnotation(String annotation)
   {
      this.annotation = annotation;
   }

   public Annotation getAnnotationInstance()
   {
      try
      {
         //FIXME [JBMICROCONT-99] [JBAOP-278] Use the loader for the bean?
         ann = (Annotation)AnnotationCreator.createAnnotation(annotation, Thread.currentThread().getContextClassLoader());
      }
      catch (Exception e)
      {
         throw new RuntimeException("Error creating annotation for " + annotation, e);
      }
      catch(TokenMgrError e)
      {
         throw new RuntimeException("Error creating annotation for " + annotation, e);
      }
      
      return ann;
   }
   
   public void visit(MetaDataVisitor visitor)
   {
      visitor.visit(this);
   }
   
   public Iterator<? extends MetaDataVisitorNode> getChildren()
   {
      return null;
   }
   
   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("expr=").append(ann);
   }
   
   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(ann);
   }
}
