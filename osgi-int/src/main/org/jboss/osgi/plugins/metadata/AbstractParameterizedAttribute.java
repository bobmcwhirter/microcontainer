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

import java.util.Map;

import org.jboss.osgi.spi.metadata.Parameter;
import org.jboss.osgi.spi.metadata.ParameterizedAttribute;
import org.jboss.util.JBossStringBuilder;

/**
 * Parameter attribute impl.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractParameterizedAttribute extends AbstractAttributeAware implements ParameterizedAttribute
{
   private static final long serialVersionUID = 1l;
   
   protected Map<String, Parameter> parameters;

   public AbstractParameterizedAttribute(String attribute, Map<String, Parameter> parameters)
   {
      super(attribute);
      this.parameters = parameters;
   }

   public Map<String, Parameter> getParameters()
   {
      return parameters;
   }

   public Parameter getParameter(String name)
   {
      return parameters.get(name);
   }

   protected void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      buffer.append(" parameters=" + parameters);
   }
}
