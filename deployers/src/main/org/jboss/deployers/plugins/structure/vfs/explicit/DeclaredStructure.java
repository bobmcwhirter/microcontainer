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
package org.jboss.deployers.plugins.structure.vfs.explicit;

import java.io.IOException;
import java.net.URL;

import org.jboss.deployers.plugins.structure.vfs.AbstractStructureDeployer;
import org.jboss.deployers.spi.structure.vfs.StructureMetaData;
import org.jboss.deployers.spi.structure.vfs.StructuredDeployers;
import org.jboss.virtual.VirtualFile;
import org.jboss.xb.binding.Unmarshaller;
import org.jboss.xb.binding.UnmarshallerFactory;

/**
 * A structural deployer that looks for a jboss-structure.xml descriptor as
 * the defining structure.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.1 $
 */
public class DeclaredStructure extends AbstractStructureDeployer
{
   private static ThreadLocal<StructureMetaData> activeMetaData
      = new ThreadLocal<StructureMetaData>();

   /**
    * Set the relative order to 0 by default.
    */
   public DeclaredStructure()
   {
      setRelativeOrder(0);
   }

   public boolean determineStructure(VirtualFile root, StructureMetaData metaData, StructuredDeployers deployers)
   {
      try
      {
         if( root.isLeaf() == false )
         {
            try
            {
               VirtualFile jbossStructure = root.findChild("META-INF/jboss-structure.xml");
               log.trace("... context has a META-INF subdirectory");
               URL url = jbossStructure.toURL();
               UnmarshallerFactory factory = UnmarshallerFactory.newInstance();
               Unmarshaller unmarshaller = factory.newUnmarshaller();
               StructureMetaDataObjectFactory ofactory = new StructureMetaDataObjectFactory();
               unmarshaller.unmarshal(url.toString(), ofactory, metaData);
               activeMetaData.set(metaData);
            }
            catch (IOException e)
            {
               log.trace("... no META-INF subdirectory.");
               return false;
            }
            return true;
         }
      }
      catch (Exception e)
      {
         log.warn("Error determining structure: " + root.getName(), e);
      }
      return false;
   }
}
