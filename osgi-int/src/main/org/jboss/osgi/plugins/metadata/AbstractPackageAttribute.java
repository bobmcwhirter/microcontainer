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
package org.jboss.osgi.plugins.metadata;

import java.io.Serializable;
import java.util.Map;

import org.jboss.osgi.spi.metadata.PackageAttribute;
import org.jboss.osgi.spi.metadata.Parameter;
import org.jboss.osgi.spi.metadata.VersionRange;
import org.jboss.reflect.plugins.PackageInfoImpl;
import org.jboss.reflect.spi.PackageInfo;
import org.osgi.framework.Constants;

/**
 * Package attribute impl.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractPackageAttribute extends AbstractParameterizedAttribute implements PackageAttribute, Serializable
{
   private static final long serialVersionUID = 1l;

   protected PackageInfo packageInfo;
   protected VersionRange versionRange;

   public AbstractPackageAttribute(String attribute, Map<String, Parameter> parameters)
   {
      super(attribute, parameters);
      packageInfo = new PackageInfoImpl(attribute);
   }

   public PackageInfo getPackageInfo()
   {
      return packageInfo;
   }

   public VersionRange getVersion()
   {
      if (versionRange == null)
      {
         Parameter parameter = getParameter(Constants.VERSION_ATTRIBUTE);
         if (parameter != null)
         {
            if (parameter.isCollection())
               throw new IllegalArgumentException("Duplicate version parameter.");
            Object value = parameter.getValue();
            if (value != null)
            {
               versionRange = AbstractVersionRange.parseRangeSpec(value.toString());
            }
         }
      }
      return versionRange;
   }
}
