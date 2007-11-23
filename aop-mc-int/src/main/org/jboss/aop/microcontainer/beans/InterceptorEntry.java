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
package org.jboss.aop.microcontainer.beans;

import org.jboss.aop.advice.AdviceFactory;
import org.jboss.aop.advice.AdviceType;
import org.jboss.aop.advice.InterceptorFactory;
import org.jboss.aop.advice.ScopedInterceptorFactory;

/**
 * An interceptor-ref or advice entry. Installs an InterceptorFactory into its binding and into aop
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class InterceptorEntry extends BindingEntry implements Cloneable
{
   Aspect aspect;
   String aspectMethod;
   InterceptorFactory interceptorFactory;
   boolean forStack;
   AdviceType type = AdviceType.AROUND;
   
   public AdviceType getType()
   {
      return type;
   }

   public void setType(AdviceType type)
   {
      this.type = type;
   }

   public Aspect getAspect(Aspect aspect)
   {
      return aspect;
   }
   
   public void setAspect(Aspect aspect)
   {
      this.aspect = aspect;
   }
   
   public void setForStack(boolean forStack)
   {
      this.forStack = forStack;
   }
   
   public String getAspectMethod()
   {
      return aspectMethod;
   }
   
   public void setAspectMethod(String aspectMethod)
   {
      this.aspectMethod = aspectMethod;
   }

   public InterceptorFactory[] getInterceptorFactories()
   {
      return new InterceptorFactory[] {interceptorFactory};
   }
   
   public InterceptorFactory getInterceptorFactory()
   {
      return interceptorFactory;
   }
   
   public void start()
   {
      if (manager == null)
      {
         throw new IllegalArgumentException("Null manager");
      }
      if (aspectBinding == null && !forStack)
      {
         throw new IllegalArgumentException("Null aspect binding");
      }
      if (aspect == null)
      {
         throw new IllegalArgumentException("Null aspect");
      }
      interceptorFactory = (aspectMethod == null) ? 
            new ScopedInterceptorFactory(aspect.getDefinition()) : new AdviceFactory(aspect.getDefinition(), aspectMethod);
            
      manager.addInterceptorFactory(name, interceptorFactory);
      if (aspectBinding != null)
      {
         aspect.addAspectBinding(aspectBinding);
      }
   }
   
   public void stop()
   {
      manager.removeInterceptorFactory(name);
      if (aspectBinding != null)
      {
         aspect.removeAspectBinding(aspectBinding);
      }
      interceptorFactory = null;
   }
   
   public Object clone()
   {
      InterceptorEntry entry = new InterceptorEntry();
      entry.manager = manager;
      entry.aspectBinding = aspectBinding;
      entry.aspect = aspect;
      entry.aspectMethod = aspectMethod;
      return entry;
   }
}
