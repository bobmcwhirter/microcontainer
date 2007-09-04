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
package org.jboss.kernel.plugins.annotations;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.plugins.annotations.JavaBeanValue;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.config.plugins.property.PropertyConfiguration;
import org.jboss.config.spi.Configuration;

/**
 * JavaBean value annotation plugin.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class JavaBeanValueAnnotationPlugin extends PropertyAnnotationPlugin<JavaBeanValue>
{
   static JavaBeanValueAnnotationPlugin INSTANCE = new JavaBeanValueAnnotationPlugin();

   /** The configuration */
   private static Configuration configuration;

   static
   {
      // get Configuration instance
      configuration = AccessController.doPrivileged(new PrivilegedAction<Configuration>()
      {
         public Configuration run()
         {
            return new PropertyConfiguration();
         }
      });
   }

   public JavaBeanValueAnnotationPlugin()
   {
      super(JavaBeanValue.class);
   }

   public ValueMetaData createValueMetaData(JavaBeanValue annotation)
   {
      String className = annotation.value();
      if (isAttributePresent(className) == false)
         throw new IllegalArgumentException("Javabean class must be set: " + annotation);

      try
      {
         BeanInfo beanInfo = configuration.getBeanInfo(className, null);
         return new AbstractValueMetaData(beanInfo.newInstance());
      }
      catch (Throwable t)
      {
         throw new IllegalArgumentException("Exception while creating javabean: " + t);
      }
   }
}
