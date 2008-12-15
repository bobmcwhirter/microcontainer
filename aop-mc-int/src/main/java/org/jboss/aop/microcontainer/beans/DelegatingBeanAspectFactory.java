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

import org.jboss.aop.Advisor;
import org.jboss.aop.InstanceAdvisor;
import org.jboss.aop.advice.AspectFactory;
import org.jboss.aop.joinpoint.Joinpoint;
import org.jboss.beans.metadata.plugins.AbstractClassLoaderMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.factory.BeanFactory;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelControllerContextAware;
import org.jboss.logging.Logger;
import org.jboss.util.xml.XmlLoadable;
import org.w3c.dom.Element;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class DelegatingBeanAspectFactory implements AspectFactory, KernelControllerContextAware, BeanFactoryAwareAspectFactory
{
   private static final Logger log = Logger.getLogger(GenericBeanAspectFactory.class); 

   protected BeanFactory factory;

   protected String name;
   
   protected Element element;
   
   protected KernelControllerContext context;
   
   public DelegatingBeanAspectFactory(String name, BeanFactory factory, Element element)
   {
      this.name = name;
      this.factory = factory;
      this.element = element;
   }

   public void setKernelControllerContext(KernelControllerContext context)
   {
      this.context = context;
   }


   public void unsetKernelControllerContext(KernelControllerContext context) throws Exception
   {
      this.context = null;
   }
   
   public void setBeanFactory(BeanFactory factory)
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

         PushedClassLoaderMetaData pcmd = null;
         if (((GenericBeanFactory)factory).getClassLoader() == null)
         {
            pcmd = new PushedClassLoaderMetaData();
            ((GenericBeanFactory)factory).setClassLoader(pcmd);
         }
         
         Object object = null;
         try
         {
            //Try without looking at the context first which is what shold be used when running scoped in AS
            object = factory.createBean();
         }
         catch(Throwable t)
         {
            if (pcmd != null)
            {
               pcmd.setLookAtContext(true);
            }
            else
            {
               throw new RuntimeException(t);
            }
            object = factory.createBean();
         }

         AspectFactory fac = (AspectFactory)object;
         if (fac instanceof XmlLoadable)
         {
            ((XmlLoadable)fac).importXml(element);
         }
         return fac;
      }
      catch (Throwable throwable)
      {
         throw new RuntimeException(throwable);
      }
   }


   /**
    * Gets any classloaders for the thread
    */
   private class PushedClassLoaderMetaData extends AbstractClassLoaderMetaData
   {
      /** The serialVersionUID */
      private static final long serialVersionUID = 1L;
      
      boolean lookAtContext;
      
      void setLookAtContext(boolean look)
      {
         lookAtContext = look;
      }

      @Override
      public ValueMetaData getClassLoader()
      {
         ClassLoader loader = null; 
         if (loader == null)
         {
            if (lookAtContext && context != null)
            {
               try
               {
                  loader = context.getClassLoader();
               }
               catch (Throwable t)
               {
                  log.trace("Unable to retrieve classloader from " + context);
               }
               
               if (loader == null)
               {
                  try
                  {
                     loader = Configurator.getClassLoader(((GenericBeanFactory)factory).getClassLoader());
                  }
                  catch (Throwable e)
                  {
                     log.trace("Unable to retrieve classloader from " + factory);
                  }
               }
            }
            return loader != null ?  new AbstractValueMetaData(loader) : null;
         }
         else
         {
            return new AbstractValueMetaData(loader);
         }
      }
      
   }
}
