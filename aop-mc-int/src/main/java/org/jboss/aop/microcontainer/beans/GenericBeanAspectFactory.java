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
package org.jboss.aop.microcontainer.beans;

import org.jboss.aop.Advisor;
import org.jboss.aop.InstanceAdvisor;
import org.jboss.aop.advice.GenericAspectFactory;
import org.jboss.aop.joinpoint.Joinpoint;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.beans.metadata.spi.factory.BeanFactory;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelControllerContextAware;
import org.jboss.logging.Logger;
import org.jboss.util.xml.XmlLoadable;
import org.w3c.dom.Element;

/**
 * A GenericBeanAspectFactory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class GenericBeanAspectFactory extends GenericAspectFactory implements KernelControllerContextAware, BeanFactoryAwareAspectFactory
{
   private static final Logger log = Logger.getLogger(GenericBeanAspectFactory.class); 

   protected BeanFactory factory;

   protected String name;
   protected String classname;
   
   protected Element element;
   
   protected KernelControllerContext context;
   
   public GenericBeanAspectFactory(String name, BeanFactory factory, Element element)
   {
      super(null, element);
      this.name = name;
      this.element = element;
      setBeanFactory(factory);
   }

   public void setBeanFactory(BeanFactory factory)
   {
      if (factory != null && factory instanceof GenericBeanFactory)
      {
         classname = ((GenericBeanFactory)factory).getBean();
      }
      this.factory = factory;
   }
   
   public String getAspectName()
   {
      return name;
   }
   
   public String getName()
   {
      //This must return the classname of the aspect, aop depends on that
      return classname;
   }
   
   @Override
   public String getClassname()
   {
      return classname;
   }

   public Object createPerVM()
   {
      return doCreate(null, null, null);
   }

   public Object createPerClass(Advisor advisor)
   {
      return doCreate(advisor, null, null);
   }

   public Object createPerInstance(Advisor advisor, InstanceAdvisor instanceAdvisor)
   {
      return doCreate(advisor, instanceAdvisor, null);
   }

   public Object createPerJoinpoint(Advisor advisor, Joinpoint jp)
   {
      return doCreate(advisor, null, jp);
   }

   public Object createPerJoinpoint(Advisor advisor, InstanceAdvisor instanceAdvisor, Joinpoint jp)
   {
      return doCreate(advisor, instanceAdvisor, jp);
   }
   
   public void setKernelControllerContext(KernelControllerContext context)
   {
      this.context = context;
   }


   public void unsetKernelControllerContext(KernelControllerContext context) throws Exception
   {
      this.context = null;
   }
   
   protected Object doCreate(Advisor advisor, InstanceAdvisor instanceAdvisor, Joinpoint jp)
   {
      try
      {
         final ClassLoader loader = getCreatingClassLoader(advisor);
         log.debug("Creating advice " + name + " with loader " + loader);
         if (factory instanceof ClassLoaderAwareGenericBeanFactory)
         {
            ((ClassLoaderAwareGenericBeanFactory)factory).pushLoader(loader);
         }
         
         Object object = factory.createBean();
         
         if (object instanceof XmlLoadable)
         {
            ((XmlLoadable)object).importXml(element);
         }
         configureInstance(object, advisor, instanceAdvisor, jp);
         return object;
      }
      catch (Throwable throwable)
      {
         throw new RuntimeException(throwable);
      }
      finally
      {
         if (factory instanceof ClassLoaderAwareGenericBeanFactory)
         {
            ((ClassLoaderAwareGenericBeanFactory)factory).popLoader();
         }
      }
   }
   
   private ClassLoader getCreatingClassLoader(Advisor advisor)
   {
      if (advisor == null)
      {
         return getLoader();
      }
      return advisor.getClassLoader();
   }
}
