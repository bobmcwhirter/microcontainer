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
package org.jboss.kernel.plugins.dependency;

import java.util.Iterator;
import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.injection.InjectionMode;
import org.jboss.beans.metadata.injection.InjectionType;
import org.jboss.joinpoint.spi.TargettedJoinpoint;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.injection.InjectionUtil;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.*;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.ControllerContext;

/**
 * ConfigureAction.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ConfigureAction extends KernelControllerContextAction
{
   public void installAction(KernelControllerContext context) throws Throwable
   {
      KernelController controller = (KernelController) context.getController();
      Kernel kernel = controller.getKernel();
      KernelConfigurator configurator = kernel.getConfigurator();

      Object object = context.getTarget();
      BeanInfo info = context.getBeanInfo();
      // todo injectWherePossible - alesj
      BeanMetaData metaData = context.getBeanMetaData();
      Set joinPoints = configurator.getPropertySetterJoinPoints(info, metaData);
      setAttributes(context, object, joinPoints, false);
//      resolveInjections(controller, info, object);
   }

   public void uninstallAction(KernelControllerContext context)
   {
      KernelController controller = (KernelController) context.getController();
      Kernel kernel = controller.getKernel();
      KernelConfigurator configurator = kernel.getConfigurator();

      Object object = context.getTarget();
      BeanInfo info = context.getBeanInfo();
      BeanMetaData metaData = context.getBeanMetaData();
      try
      {
         Set joinPoints = configurator.getPropertyNullerJoinPoints(info, metaData);
         setAttributes(context, object, joinPoints, true);
//         nullifyInjections(controller, info, object, true);
      }
      catch (Throwable t)
      {
         log.warn("Error unconfiguring bean " + context, t);
      }
   }

   /**
    * Set the attributes
    *
    * @param context the context
    * @param target the target
    * @param joinPoints the attribute setter joinpoints
    * @param ignoreErrors whether to ignore errors
    * @throws Throwable for any unignored error
    */
   protected void setAttributes(KernelControllerContext context, Object target, Set joinPoints, boolean ignoreErrors) throws Throwable
   {
      if (joinPoints.isEmpty() == false)
      {
         boolean trace = log.isTraceEnabled();

         for (Iterator i = joinPoints.iterator(); i.hasNext();)
         {
            TargettedJoinpoint joinPoint = (TargettedJoinpoint) i.next();
            joinPoint.setTarget(target);
            try
            {
               dispatchJoinPoint(context, joinPoint);
            }
            catch (Throwable t)
            {
               if (ignoreErrors)
               {
                  if (trace)
                     log.trace("Ignored for " + joinPoint, t);
               }
               else
               {
                  throw t;
               }
            }
         }
      }
   }

   protected void resolveInjections(KernelController controller, BeanInfo info, Object target) throws Throwable
   {
      Set<PropertyInfo> propertys = info.getProperties();
      for(PropertyInfo pi : propertys)
      {
         MethodInfo setter = pi.getSetter();
         AnnotationValue annotation = setter.getAnnotation("org.jboss.beans.metadata.spi.annotations.Inject");
         if (annotation != null)
         {
            AnnotationInfo annotationInfo = annotation.getAnnotationType();
            AnnotationAttribute beanAttribute = annotationInfo.getAttribute("bean");
            AnnotationAttribute propertyAttribute = annotationInfo.getAttribute("property");
            AnnotationAttribute stateAttribute = annotationInfo.getAttribute("state");
            AnnotationAttribute modeAttribute = annotationInfo.getAttribute("mode");
            AnnotationAttribute typeAttribute = annotationInfo.getAttribute("type");
            // todo - are these right values?
            StringValue beanValue = (StringValue) beanAttribute.getDefaultValue();
            StringValue propertyValue = (StringValue) propertyAttribute.getDefaultValue();
            StringValue stateValue = (StringValue) stateAttribute.getDefaultValue();
            EnumValue modeValue = (EnumValue) modeAttribute.getDefaultValue();
            EnumValue typeValue = (EnumValue) typeAttribute.getDefaultValue();

            String value = beanValue.getValue();
            String bean = (value != null && value.length() > 0 ? value : null);
            value = propertyValue.getValue();
            String property = (value != null && value.length() > 0 ? value : null);
            ControllerState state = new ControllerState(stateValue.getValue());
            InjectionMode injectionMode = new InjectionMode(modeValue.getValue());
            InjectionType injectionType = new InjectionType(typeValue.getValue());
            Object result = null;
            if (bean != null)
            {
               ControllerContext context = controller.getContext(bean, state);
               if (context != null && context.getTarget() != null)
               {
                  result = getResult(controller, context.getTarget(), property);
               }
            }
            else
            {
               // check for property
               if (property != null)
               {
                  log.warn("Ignoring property - contextual injection: " + pi);
               }
               result = InjectionUtil.resolveInjection(
                     controller,
                     pi.getType().getType(),
                     pi.getName(),
                     state,
                     injectionMode,
                     injectionType,
                     pi
               );
            }
            setter.invoke(target, new Object[]{result});
         }
      }
   }

   protected Object getResult(KernelController controller, Object target, String property) throws Throwable
   {
      if (property != null)
      {
         KernelConfigurator configurator = controller.getKernel().getConfigurator();
         BeanInfo beanInfo = configurator.getBeanInfo(target.getClass());
         TargettedJoinpoint joinpoint = configurator.getPropertyGetterJoinPoint(beanInfo, property);
         joinpoint.setTarget(target);
         return joinpoint.dispatch();
      }
      return target;
   }

   protected void nullifyInjections(KernelController controller, BeanInfo info, Object target, boolean ignoreErrors) throws Throwable
   {
      Set<PropertyInfo> propertys = info.getProperties();
      for(PropertyInfo pi : propertys)
      {
         MethodInfo setter = pi.getSetter();
         AnnotationValue annotation = setter.getAnnotation("org.jboss.beans.metadata.spi.annotations.Inject");
         if (annotation != null)
         {
            try
            {
               setter.invoke(target, new Object[]{null});
            }
            catch (Throwable t)
            {
               if (ignoreErrors)
               {
                  if (log.isTraceEnabled())
                     log.trace("Ignored for " + pi, t);
               }
               else
               {
                  throw t;
               }
            }
         }
      }
   }

}