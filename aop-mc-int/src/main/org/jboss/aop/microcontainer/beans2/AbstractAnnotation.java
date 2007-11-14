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
package org.jboss.aop.microcontainer.beans2;

import org.jboss.aop.AspectManager;
import org.jboss.aop.introduction.AnnotationIntroduction;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractAnnotation
{
   protected AspectManager manager;
   private String expr;
   private String annotation;
   private boolean invisible = false;
   protected AnnotationIntroduction intro;

   public AspectManager getManager()
   {
      return manager;
   }

   public void setManager(AspectManager manager)
   {
      this.manager = manager;
   }

   public String getExpr()
   {
      return expr;
   }

   public void setExpr(String expr)
   {
      this.expr = expr;
   }

   public String getAnnotation()
   {
      return annotation;
   }

   public void setAnnotation(String annotation)
   {
      this.annotation = annotation;
   }

   public boolean isInvisible()
   {
      return invisible;
   }

   public void setInvisible(boolean invisible)
   {
      this.invisible = invisible;
   }
   
   public abstract void start();
   
   protected AnnotationIntroduction validateAndCreate()
   {
      if (manager == null)
      {
         throw new IllegalArgumentException("Null manager");
      }
      if (expr == null)
      {
         throw new IllegalArgumentException("Null expr");
      }
      if (annotation == null)
      {
         throw new IllegalArgumentException("Null annotation");
      }
      
      intro = AnnotationIntroduction.createComplexAnnotationIntroduction(expr, annotation, invisible);
      return intro;
   }
   
   
   
   public abstract void stop();
}
