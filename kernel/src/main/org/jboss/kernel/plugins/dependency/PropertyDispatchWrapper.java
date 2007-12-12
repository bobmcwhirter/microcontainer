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
package org.jboss.kernel.plugins.dependency;

import java.security.PrivilegedExceptionAction;

import org.jboss.logging.Logger;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.info.plugins.BeanInfoUtil;

/**
 * PropertyDispatchWrapper.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
class PropertyDispatchWrapper extends ExecutionWrapper
{
   private static Logger log = Logger.getLogger(ConfigureAction.class);

   private PropertyMetaData property;
   private boolean nullify;
   private BeanInfo beanInfo;
   private Object target;
   private ClassLoader cl;

   public PropertyDispatchWrapper(PropertyMetaData property, boolean nullify, BeanInfo beanInfo, Object target, ClassLoader cl)
   {
      if (property == null)
         throw new IllegalArgumentException("Null property");
      this.property = property;
      this.nullify = nullify;
      if (beanInfo == null)
         throw new IllegalArgumentException("Null bean info");
      this.beanInfo = beanInfo;
      if (target == null)
         throw new IllegalArgumentException("Null target");
      this.target = target;
      this.cl = cl;
   }

   public Object execute() throws Throwable
   {
      String name = property.getName();
      if (nullify)
      {
         try
         {
            beanInfo.setProperty(target, name, null);
         }
         catch (Throwable t)
         {
            if (log.isTraceEnabled())
               log.trace("Ignored for " + target + "." + name, t);
         }
      }
      else
      {
         PropertyInfo propertyInfo = BeanInfoUtil.getPropertyInfo(beanInfo, target, name);
         ValueMetaData valueMetaData = property.getValue();
         Object value = valueMetaData.getValue(propertyInfo.getType(), cl);
         beanInfo.setProperty(target, name, value);
      }
      return null;
   }

   protected PrivilegedExceptionAction<Object> getAction()
   {
      return new PrivilegedExceptionAction<Object>()
      {
         public Object run() throws Exception
         {
            try
            {
               return execute();
            }
            catch (RuntimeException e)
            {
               throw e;
            }
            catch (Exception e)
            {
               throw e;
            }
            catch (Error e)
            {
               throw e;
            }
            catch (Throwable t)
            {
               throw new RuntimeException(t);
            }
         }
      };
   }
}
