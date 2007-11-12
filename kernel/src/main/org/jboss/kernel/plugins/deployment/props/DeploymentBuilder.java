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
package org.jboss.kernel.plugins.deployment.props;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.jboss.kernel.spi.deployment.KernelDeployment;

/**
 * Deployment builder.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class DeploymentBuilder
{
   private PropertiesGraphFactory graphFactory;

   public DeploymentBuilder(Properties properties)
   {
      buildGraph(toMap(properties));
   }

   public DeploymentBuilder(Map<String, String> properties)
   {
      buildGraph(properties);
   }

   protected static Map<String, String> toMap(Properties properties)
   {
      if (properties == null)
         return null;

      Map<String, String> map = new TreeMap<String, String>();
      for(Object key : properties.keySet())
      {
         String ks = key.toString();
         map.put(ks, properties.getProperty(ks));
      }
      return map;
   }

   /**
    * Build graph.
    *
    * @param properties the properties
    */
   protected void buildGraph(Map<String, String> properties)
   {
      graphFactory = new PropertiesGraphFactory(properties);
   }

   /**
    * Build KernelDeployment instance.
    *
    * @return KernelDeployment instance
    */
   public KernelDeployment build()
   {
      return graphFactory.build();
   }
}
