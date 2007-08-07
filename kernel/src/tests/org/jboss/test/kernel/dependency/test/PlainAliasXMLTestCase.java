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
package org.jboss.test.kernel.dependency.test;

import java.net.URL;
import java.util.Set;

import junit.framework.Test;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.dependency.spi.Controller;
import org.jboss.beans.metadata.spi.NamedAliasMetaData;

/**
 * Plain alias tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class PlainAliasXMLTestCase extends PlainAliasTestCase
{
   public PlainAliasXMLTestCase(String name)
         throws Throwable
   {
      super(name, true);
   }

   public static Test suite()
   {
      return suite(PlainAliasXMLTestCase.class);
   }

   protected void installAlias() throws Throwable
   {
      String url = getClass().getSimpleName() + "_Alias.xml";
      URL resource = getResource(url);
      if (resource == null)
         throw new IllegalArgumentException("Null resource: " + url);
      KernelDeployment deployment = getUtil().deploy(resource);
      Set<NamedAliasMetaData> aliases = deployment.getAliases();
      if (aliases != null && aliases.isEmpty() == false)
      {
         Controller controller = getUtil().getKernel().getController();
         for (NamedAliasMetaData namd : aliases)
         {
            controller.addAlias(namd.getAliasValue(), namd.getName());
         }
      }
   }
}
