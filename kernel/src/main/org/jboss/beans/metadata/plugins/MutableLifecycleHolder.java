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
package org.jboss.beans.metadata.plugins;

import org.jboss.beans.metadata.spi.LifecycleMetaData;

/**
 * Lifecycle metadata holder.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface MutableLifecycleHolder
{
   /**
    * Set create lifecycle metadata.
    *
    * @param create the lifecycle meta data
    */
   void setCreate(LifecycleMetaData create);
   
   /**
    * Set start lifecycle metadata.
    *
    * @param start the lifecycle meta data
    */
   void setStart(LifecycleMetaData start);

   /**
    * Set stop lifecycle metadata.
    *
    * @param stop the lifecycle meta data
    */
   void setStop(LifecycleMetaData stop);

   /**
    * Set destroy lifecycle metadata.
    *
    * @param destroy the lifecycle meta data
    */
   void setDestroy(LifecycleMetaData destroy);
}
