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
package org.jboss.beans.metadata.plugins.factory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.ClassLoaderMetaData;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.beans.metadata.spi.LifecycleMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.joinpoint.spi.Joinpoint;
import org.jboss.joinpoint.spi.JoinpointException;
import org.jboss.joinpoint.spi.MethodJoinpoint;
import org.jboss.joinpoint.spi.TargettedJoinpoint;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.config.KernelConfigurator;

/**
 * Bean factory metadata.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class GenericBeanFactory
{
   /** The configurator */
   protected KernelConfigurator configurator;
   
   /** The bean class name */
   protected String bean;
   
   /** The classloader */
   protected ClassLoaderMetaData classLoader;
   
   /** The constructor metadata */
   protected ConstructorMetaData constructor;
   
   /** The properties Map<propertyName, ValueMetaData> */
   protected Map properties;

   /** The create lifecycle method */
   protected LifecycleMetaData create;

   /** The start lifecycle method */
   protected LifecycleMetaData start;
   
   /**
    * Create a new generic bean factory
    * 
    * @param configurator the configurator
    */
   public GenericBeanFactory(KernelConfigurator configurator)
   {
      this.configurator = configurator;
   }
   
   /**
    * Create a new bean
    *
    * @return the bean
    * @throws Throwable for any error
    */
   public Object createBean() throws Throwable
   {
      ClassLoader cl = Configurator.getClassLoader(classLoader);
      BeanInfo info = configurator.getBeanInfo(bean, cl);
      Joinpoint joinpoint = configurator.getConstructorJoinPoint(info, constructor, null);
      Object result = joinpoint.dispatch();
      if (properties != null && properties.size() > 0)
      {
         for (Iterator i = properties.entrySet().iterator(); i.hasNext();)
         {
            Map.Entry entry = (Map.Entry) i.next();
            String property = (String) entry.getKey();
            ValueMetaData vmd = (ValueMetaData) entry.getValue();
            TargettedJoinpoint jp = configurator.getPropertySetterJoinPoint(info, property, cl, vmd);
            jp.setTarget(result);
            jp.dispatch();
         }
      }
      invokeLifecycle("create", create, info, cl, result);
      invokeLifecycle("start", start, info, cl, result);
      return result;
   }
   
   /**
    * Get the bean name
    * 
    * @return the bean
    */
   public String getBean()
   {
      return bean;
   }
   
   /**
    * Set the bean name
    * 
    * @param bean the bean name
    */
   public void setBean(String bean)
   {
      this.bean = bean;
   }
   
   /**
    * Get the classLoader.
    * 
    * @return the classLoader.
    */
   public ClassLoaderMetaData getClassLoader()
   {
      return classLoader;
   }

   /**
    * Set the classLoader.
    * 
    * @param classLoader the classLoader.
    */
   public void setClassLoader(ClassLoaderMetaData classLoader)
   {
      this.classLoader = classLoader;
   }

   /**
    * Get the constructor metadata
    * 
    * @return the contructor metadata
    */
   public ConstructorMetaData getConstructor()
   {
      return constructor;
   }
   
   /**
    * Set the constructor metadata
    * 
    * @param constructor the constructor metadata
    */
   public void setConstructor(ConstructorMetaData constructor)
   {
      this.constructor = constructor;
   }
   
   /**
    * Get the properties
    * 
    * @return the properties Map<propertyName, ValueMetaData>
    */
   public Map getProperties()
   {
      return properties;
   }
   
   /**
    * Set the properties
    * 
    * @param properties the properties Map<propertyName, ValueMetaData>
    */
   public void setProperties(Map properties)
   {
      this.properties = properties;
   }

   /**
    * Get the create.
    * 
    * @return the create.
    */
   public LifecycleMetaData getCreate()
   {
      return create;
   }

   /**
    * Set the create.
    * 
    * @param create the create.
    */
   public void setCreate(LifecycleMetaData create)
   {
      this.create = create;
   }

   /**
    * Get the start.
    * 
    * @return the start.
    */
   public LifecycleMetaData getStart()
   {
      return start;
   }

   /**
    * Set the start.
    * 
    * @param start the start.
    */
   public void setStart(LifecycleMetaData start)
   {
      this.start = start;
   }

   /**
    * Invoke a lifecycle method
    * 
    * @param methodName the default method name
    * @param lifecycle the lifecycle
    * @param info the bean info
    * @param cl the classloader
    * @param target the target
    * @throws Throwable for any error
    */
   protected void invokeLifecycle(String methodName, LifecycleMetaData lifecycle, BeanInfo info, ClassLoader cl, Object target) throws Throwable
   {
      String method = methodName;
      if (lifecycle != null && lifecycle.getMethodName() != null)
         method = lifecycle.getMethodName();
      List<ParameterMetaData> parameters = null;
      if (lifecycle != null)
         parameters = lifecycle.getParameters();
      MethodJoinpoint joinpoint;
      try
      {
         joinpoint = configurator.getMethodJoinPoint(info, cl, method, parameters, false, true);
      }
      catch (JoinpointException ignored)
      {
         return;
      }
      joinpoint.setTarget(target);
      joinpoint.dispatch();
   }
}