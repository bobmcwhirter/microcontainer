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
package org.jboss.test.kernel.config.support;

import java.util.List;

import org.jboss.beans.metadata.plugins.annotations.Constructor;
import org.jboss.beans.metadata.plugins.annotations.Inject;
import org.jboss.beans.metadata.plugins.annotations.ListValue;
import org.jboss.beans.metadata.plugins.annotations.Parameter;
import org.jboss.beans.metadata.plugins.annotations.StringValue;
import org.jboss.beans.metadata.plugins.annotations.Value;
import org.jboss.beans.metadata.plugins.annotations.ValueFactory;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AnnotatedPropHolder extends PropHolder
{
   @Constructor
   public AnnotatedPropHolder(
         @ValueFactory(bean = "ldap", method = "getValue", parameter = "foo.bar.key") String constructor
   )
   {
      super(constructor);
   }

   @ValueFactory(bean = "ldap", method = "getValue", parameter = "foo.bar.key")
   public void setValue(String value)
   {
      super.setValue(value);
   }

   @ListValue(
         elementClass = "java.lang.String",
         value = {
            @Value(valueFactory = @ValueFactory(bean = "ldap", method = "getValue", parameter = "foo.bar.key")),
            @Value(valueFactory = @ValueFactory(bean = "ldap", method = "getValue", parameters = {@Parameter(string = @StringValue("foo.bar.key")), @Parameter(string = @StringValue("qaz"))})),
            @Value(valueFactory = @ValueFactory(bean = "ldap", method = "getValue", parameters = {@Parameter(string = @StringValue("xyz.key")), @Parameter(string = @StringValue("xyz")), @Parameter(inject = @Inject(bean = "t"))})),
            @Value(valueFactory = @ValueFactory(bean = "ldap", method = "getValue", defaultValue = "QWERT", parameters = {@Parameter(string = @StringValue("no.such.key"))}))
         }
   )
   public void setList(List<String> list)
   {
      super.setList(list);
   }
}
