/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.classloading.plugins.metadata;

import java.lang.reflect.Type;

import org.jboss.classloading.spi.version.Version;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.SimpleValue;
import org.jboss.metatype.api.values.SimpleValueSupport;
import org.jboss.metatype.spi.values.MetaMapper;

/**
 * VersionMetaMapper.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class VersionMetaMapper extends MetaMapper<Version>
{
   public Type mapToType()
   {
      return String.class;
   }

   public MetaValue createMetaValue(MetaType metaType, Version object)
   {
      return SimpleValueSupport.wrap(object.toString());
   }

   public Version unwrapMetaValue(MetaValue metaValue)
   {
      if (SimpleMetaType.STRING.equals(metaValue.getMetaType()) == false)
         throw new IllegalArgumentException("Not a string: " + metaValue);
      
      SimpleValue simple = (SimpleValue) metaValue;
      String value = (String) simple.getValue();
      return Version.parseVersion(value);
   }
}
