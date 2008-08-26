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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.jboss.annotation.factory.AnnotationCreator;
import org.jboss.annotation.factory.ast.TokenMgrError;
import org.jboss.beans.metadata.spi.CachingAnnotationMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;
import org.jboss.util.StringPropertyReplacer;

/**
 * Metadata for an annotation.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @version $Revision$
 */
@XmlType(name="annotationType", propOrder={"annotation"})
public class AbstractAnnotationMetaData extends JBossObject implements CachingAnnotationMetaData, Serializable
{
   private static final long serialVersionUID = 2L;

   public String annotation;
   private Annotation ann;
   private boolean replace = true;

   /**
    * Create a new annotation meta data
    */
   public AbstractAnnotationMetaData()
   {
      super();
   }

   public AbstractAnnotationMetaData(String annotation)
   {
      this.annotation = annotation;
   }

   public String getAnnotation()
   {
      if (annotation == null)
         throw new IllegalArgumentException("Null string annotation value, illegal xml form?");

      return annotation;
   }

   @XmlValue
   public void setAnnotation(String annotation)
   {
      this.annotation = annotation;
      flushJBossObjectCache();
   }

   public boolean isReplace()
   {
      return replace;
   }

   @XmlAttribute(name="replace")
   public void setReplace(boolean replace)
   {
      this.replace = replace;
   }

   @XmlTransient
   public Annotation getAnnotationInstance()
   {
      return getAnnotationInstance(null);
   }   
   
   public Annotation getAnnotationInstance(ClassLoader cl)
   {
      if (ann != null)
         return ann;

      String annString = getAnnotation();
      try
      {
         if (replace)
         {
            annString = StringPropertyReplacer.replaceProperties(annString);
         }
         if (cl == null) 
         {
            cl = Thread.currentThread().getContextClassLoader();
         }
         ann = (Annotation)AnnotationCreator.createAnnotation(annString, cl);
         flushJBossObjectCache();
      }
      catch(TokenMgrError e)
      {
         throw new RuntimeException("Error creating annotation for " + annotation, e);
      }
      catch (Throwable e)
      {
         throw new RuntimeException("Error creating annotation for " + annotation, e);
      }

      return ann;
   }

   public Annotation removeAnnotation()
   {
      Annotation result = ann;
      ann = null;
      flushJBossObjectCache();
      return result;
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      String ann = getAnnotation().trim();
      if (ann == null || ann.length() == 0)
         throw new IllegalArgumentException("Empty annotation content");
      if (ann.startsWith("@") == false)
         throw new IllegalArgumentException("Annotation content must be a fully qualified annotation type name prefixed with '@'");

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
      buffer.append("expr=");
      toShortString(buffer);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      if (ann == null)
         buffer.append(annotation);
      else
         buffer.append(ann);
   }

   protected int getHashCode()
   {
      return getAnnotation().hashCode();
   }

   public boolean equals(Object object)
   {
      if (object == null || object instanceof AbstractAnnotationMetaData == false)
         return false;

      AbstractAnnotationMetaData amd = (AbstractAnnotationMetaData)object;
      // this is what we probably want? - never saw duplicate annotation on a bean/prop/...
      return (replace == amd.replace) && getAnnotation().equals(amd.getAnnotation());
   }

   public AbstractAnnotationMetaData clone()
   {
      return (AbstractAnnotationMetaData)super.clone();
   }
}
