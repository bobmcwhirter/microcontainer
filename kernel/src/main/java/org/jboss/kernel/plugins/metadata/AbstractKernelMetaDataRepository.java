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
package org.jboss.kernel.plugins.metadata;

import org.jboss.kernel.plugins.AbstractKernelObject;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;

/**
 * AbstractKernelMetaDataRepository.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public abstract class AbstractKernelMetaDataRepository extends AbstractKernelObject implements KernelMetaDataRepository
{
   /** The meta data repository */
   private MutableMetaDataRepository metaDataRepository;

   /**
    * Create a new AbstractKernelMetaDataRepository.
    */
   public AbstractKernelMetaDataRepository()
   {
   }
   
   /**
    * Create a new AbstractKernelMetaDataRepository.
    * 
    * @param metaDataRepository the meta data repository
    */
   public AbstractKernelMetaDataRepository(MutableMetaDataRepository metaDataRepository)
   {
      this.metaDataRepository = metaDataRepository;
   }

   public MutableMetaDataRepository getMetaDataRepository()
   {
      return metaDataRepository;
   }

   /**
    * Set the metaDataRepository.
    * 
    * @param metaDataRepository the metaDataRepository.
    */
   public void setMetaDataRepository(MutableMetaDataRepository metaDataRepository)
   {
      this.metaDataRepository = metaDataRepository;
   }
}
