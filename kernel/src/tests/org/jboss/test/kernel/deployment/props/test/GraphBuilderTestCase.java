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
package org.jboss.test.kernel.deployment.props.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import org.jboss.kernel.plugins.deployment.props.PropertiesGraphFactory;
import org.jboss.test.BaseTestCase;

/**
 * Test graph from properties.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class GraphBuilderTestCase extends BaseTestCase
{
   public GraphBuilderTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(GraphBuilderTestCase.class);
   }

   public void testGraphFromProperties() throws Exception
   {
      Map<String, String> properties = new HashMap<String, String>();
      properties.put("mybean.(class)", "org.jboss.acme.MyBean");
      properties.put("mybean.somenumber", "123L");
      properties.put("mybean.somenumber.type", "java.lang.Long");

      PropertiesGraphFactory propertiesGraph = new PropertiesGraphFactory(properties);
      System.out.println("propertiesGraph " + propertiesGraph);
      System.out.println("");

      System.out.println(propertiesGraph.build());

      System.out.println("");
   }
}
