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
package org.jboss.deployers.vfs.plugins.structure.war;

import org.jboss.deployers.spi.structure.ContextInfo;
import org.jboss.deployers.spi.structure.StructureMetaData;
import org.jboss.virtual.VirtualFile;

/**
 * ExplicitOrderWARStructure.
 * By default we put war context at the end.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class ExplicitOrderWARStructure extends WARStructure
{
   private int contextOrder = Integer.MAX_VALUE;

   protected ContextInfo createContext(VirtualFile root, String metaDataPath, StructureMetaData structureMetaData)
   {
      ContextInfo contextInfo = super.createContext(root, metaDataPath, structureMetaData);
      contextInfo.setRelativeOrder(contextOrder);
      return contextInfo;
   }

   public void setContextOrder(int contextOrder)
   {
      this.contextOrder = contextOrder;
   }
}
