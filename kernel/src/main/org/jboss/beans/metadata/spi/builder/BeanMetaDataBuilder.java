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
package org.jboss.beans.metadata.spi.builder;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.dependency.spi.ControllerMode;

/**
 * BeanMetaDataBuilder contract.
 * TODO - javadocs
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface BeanMetaDataBuilder
{
   BeanMetaData getBeanMetaData();

   BeanMetaDataBuilder setMode(String modeString);

   BeanMetaDataBuilder setMode(ControllerMode mode);

   BeanMetaDataBuilder setClassLoader(Object classLoader);

   BeanMetaDataBuilder setConstructorValue(Object value);

   BeanMetaDataBuilder setConstructorValue(ValueMetaData value);

   BeanMetaDataBuilder addConstructorParameter(String type, Object value);

   BeanMetaDataBuilder addConstructorParameter(String type, ValueMetaData value);

   BeanMetaDataBuilder addPropertyMetaData(String name, Object value);

   BeanMetaDataBuilder addPropertyMetaData(String name, String value);

   BeanMetaDataBuilder addPropertyMetaData(String name, ValueMetaData value);

   BeanMetaDataBuilder setCreate(String methodName);

   BeanMetaDataBuilder addCreateParameter(String type, Object value);

   BeanMetaDataBuilder addCreateParameter(String type, ValueMetaData value);

   BeanMetaDataBuilder setStart(String methodName);

   BeanMetaDataBuilder addStartParameter(String type, Object value);

   BeanMetaDataBuilder addStartParameter(String type, ValueMetaData value);

   BeanMetaDataBuilder setStop(String methodName);

   BeanMetaDataBuilder addStopParameter(String type, Object value);

   BeanMetaDataBuilder addStopParameter(String type, ValueMetaData value);

   BeanMetaDataBuilder setDestroy(String methodName);

   BeanMetaDataBuilder addDestroyParameter(String type, Object value);

   BeanMetaDataBuilder addDestroyParameter(String type, ValueMetaData value);

   BeanMetaDataBuilder addSupply(Object supply);

   BeanMetaDataBuilder addDemand(Object demand);

   BeanMetaDataBuilder addDependency(Object dependency);

   BeanMetaDataBuilder addInstall(String methodName);

   BeanMetaDataBuilder addInstall(String methodName, String bean);

   BeanMetaDataBuilder addInstall(String methodName, String type, Object value);

   BeanMetaDataBuilder addInstall(String methodName, String bean, String type, Object value);

   BeanMetaDataBuilder addInstall(String methodName, String[] types, Object[] values);

   BeanMetaDataBuilder addInstall(String methodName, String bean, String[] types, Object[] values);

   BeanMetaDataBuilder addUninstall(String methodName);

   BeanMetaDataBuilder addUninstall(String methodName, String type, Object value);

   BeanMetaDataBuilder addUninstall(String methodName, String[] types, Object[] values);

   BeanMetaDataBuilder addUninstall(String methodName, String bean);

   BeanMetaDataBuilder addUninstall(String methodName, String bean, String type, Object value);

   BeanMetaDataBuilder addUninstall(String methodName, String bean, String[] types, Object[] values);
}
