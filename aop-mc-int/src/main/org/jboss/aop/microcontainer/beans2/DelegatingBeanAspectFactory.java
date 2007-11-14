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

import org.jboss.aop.Advisor;
import org.jboss.aop.InstanceAdvisor;
import org.jboss.aop.advice.AspectFactory;
import org.jboss.aop.joinpoint.Joinpoint;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.beans.metadata.spi.factory.BeanFactory;
import org.jboss.logging.Logger;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class DelegatingBeanAspectFactory implements AspectFactory
{
   private static final Logger log = Logger.getLogger(GenericBeanAspectFactory.class); 

   protected BeanFactory factory;

   protected String name;
   
   public DelegatingBeanAspectFactory(String name, BeanFactory factory)
   {
      this.name = name;
      this.factory = factory;
   }

   public void setBeanFactory(GenericBeanFactory factory)
   {
      this.factory = factory;
   }
   
   public String getName()
   {
      return name;
   }

   public Object createPerVM()
   {
      AspectFactory factory = doCreate();
      return factory.createPerVM();
   }

   public Object createPerClass(Advisor advisor)
   {
      AspectFactory factory = doCreate();
      return factory.createPerClass(advisor);
   }

   public Object createPerInstance(Advisor advisor, InstanceAdvisor instanceAdvisor)
   {
      AspectFactory factory = doCreate();
      return factory.createPerInstance(advisor, instanceAdvisor);
   }

   public Object createPerJoinpoint(Advisor advisor, Joinpoint jp)
   {
      AspectFactory factory = doCreate();
      return factory.createPerJoinpoint(advisor, jp);
   }

   public Object createPerJoinpoint(Advisor advisor, InstanceAdvisor instanceAdvisor, Joinpoint jp)
   {
      AspectFactory factory = doCreate();
      return factory.createPerJoinpoint(advisor, instanceAdvisor, jp);
   }

   protected AspectFactory doCreate()
   {
      try
      {
         log.debug("Creating advice " + name);
         return (AspectFactory)factory.createBean();
      }
      catch (Throwable throwable)
      {
         throw new RuntimeException(throwable);
      }
   }

}
