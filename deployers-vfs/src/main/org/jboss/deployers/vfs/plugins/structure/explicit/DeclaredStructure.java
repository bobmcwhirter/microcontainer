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
package org.jboss.deployers.vfs.plugins.structure.explicit;

import java.io.IOException;
import java.net.URL;

import org.jboss.deployers.spi.structure.StructureMetaData;
import org.jboss.deployers.vfs.spi.structure.VFSStructuralDeployers;
import org.jboss.deployers.vfs.spi.structure.helpers.AbstractStructureDeployer;
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
   /**
    * Set the relative order to 0 by default.
    */
   public DeclaredStructure()
   {
      setRelativeOrder(0);
   }

   public boolean determineStructure(VirtualFile root, VirtualFile parent, VirtualFile file, StructureMetaData metaData, VFSStructuralDeployers deployers)
   {
      try
      {
         boolean trace = log.isTraceEnabled();
         if (isLeaf(file) == false)
         {
            boolean isJBossStructure = false;
            if (trace)
               log.trace(file + " is not a leaf");
            try
            {
               VirtualFile jbossStructure = file.findChild("META-INF/jboss-structure.xml");
               log.trace("... context has a META-INF/jboss-structure.xml");
               URL url = jbossStructure.toURL();
               UnmarshallerFactory factory = UnmarshallerFactory.newInstance();
               Unmarshaller unmarshaller = factory.newUnmarshaller();
               StructureMetaDataObjectFactory ofactory = new StructureMetaDataObjectFactory();
               unmarshaller.unmarshal(url.toString(), ofactory, metaData);
               isJBossStructure = true;
            }
            catch (IOException e)
            {
               if (trace)
                  log.trace("... no META-INF/jboss-structure.xml.");
            }
            if (trace)
               log.trace(file + " isJBossStructure: " + isJBossStructure);
            return isJBossStructure;
         }
      }
      catch (Exception e)
      {
         log.warn("Error determining structure: " + file.getName(), e);
      }
      return false;
   }
}
