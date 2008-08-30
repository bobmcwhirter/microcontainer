/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.kernel.spi.event;

/**
 * A listener
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface KernelEvent
{
   /**
    * Get the source of the event
    * 
    * @return the source
    */
   Object getSource();

   /**
    * Get the event type
    * 
    * @return the type
    */
   String getType();

   /**
    * Get the sequence
    * 
    * @return the sequence
    */
   long getSequence();

   /**
    * Get the timestamp
    * 
    * @return the timestamp
    */
   long getTimestamp();

   /**
    * Get the context
    * 
    * @return the context
    */
   Object getContext();
}
