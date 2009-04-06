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
package org.jboss.test.spring.test;

import org.jboss.kernel.plugins.deployment.xml.BasicXMLDeployer;
import org.jboss.spring.deployment.xml.SpringSchemaInitializer;
import org.jboss.test.kernel.junit.MicrocontainerTestDelegate;
import org.jboss.xb.binding.resolver.MutableSchemaResolver;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBindingResolver;
import org.jboss.xb.binding.sunday.unmarshalling.SingletonSchemaResolverFactory;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class TempSpringMicrocontainerTestDelegate extends MicrocontainerTestDelegate
{
   public TempSpringMicrocontainerTestDelegate(Class<?> clazz) throws Exception
   {
      super(clazz);
   }

   public void setUp() throws Exception
   {
      SchemaBindingResolver resolver = SingletonSchemaResolverFactory.getInstance().getSchemaBindingResolver();
      MutableSchemaResolver defaultSchemaResolver = (MutableSchemaResolver) resolver;
      defaultSchemaResolver.mapSchemaInitializer("urn:jboss:spring-beans:2.0", new SpringSchemaInitializer());
      defaultSchemaResolver.mapSchemaLocation("urn:jboss:spring-beans:2.0", "mc-spring-beans_2_0.xsd");
      defaultSchemaResolver.setParseXSDAnnotations("urn:jboss:spring-beans:2.0", Boolean.FALSE);
      super.setUp();
   }

   protected BasicXMLDeployer createDeployer()
   {
      return new AliasBeanXMLDeployer(kernel);
   }
}
