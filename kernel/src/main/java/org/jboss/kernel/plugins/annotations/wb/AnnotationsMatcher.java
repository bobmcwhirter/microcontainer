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
package org.jboss.kernel.plugins.annotations.wb;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.jboss.kernel.api.dependency.ClassMatcher;

/**
 * Annotations matcher.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AnnotationsMatcher extends ClassMatcher<AnnotationsSupply> implements Serializable
{
   private static final long serialVersionUID = 1l;

   private Set<Annotation> annotations = new HashSet<Annotation>();
   private int size;

   public AnnotationsMatcher(Annotation annotation)
   {
      super(AnnotationsSupply.class);
      addAnnotation(annotation);
   }

   /**
    * Add the annotation.
    *
    * @param annotation the annotation
    */
   public void addAnnotation(Annotation annotation)
   {
      annotations.add(annotation);
      size = annotations.size();
   }

   protected boolean matchByType(AnnotationsSupply other)
   {
      Set<Annotation> otherAnnotations = other.getAnnotations();
      if (otherAnnotations.size() >= size)
      {
         otherAnnotations = new HashSet<Annotation>(otherAnnotations);
         otherAnnotations.retainAll(annotations);
         return (otherAnnotations.size() == size);
      }
      return false;
   }

   public boolean needExactMatch()
   {
      return true;
   }
}
