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
package org.jboss.beans.metadata.plugins;

import java.util.Map;
import java.util.Properties;

/**
 * Properties metadata.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractPropertiesMetaData extends AbstractMapMetaData
{
   private static final long serialVersionUID = 1L;

   protected Map<Object, Object> getDefaultMapInstance() throws Throwable
   {
      return new Properties();
   }

   protected Class<? extends Map> expectedMapClass()
   {
      return Properties.class;
   }

   public String getKeyType()
   {
      return String.class.getName();
   }

   public void setKeyType(String keyType)
   {
      throw new IllegalArgumentException("Illegal call to set properties key type!");
   }

   public String getValueType()
   {
      return String.class.getName();
   }

   public void setValueType(String valueType)
   {
      throw new IllegalArgumentException("Illegal call to set properties value type!");
   }

}
