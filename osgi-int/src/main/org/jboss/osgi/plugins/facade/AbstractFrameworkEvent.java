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
package org.jboss.osgi.plugins.facade;

import org.jboss.kernel.plugins.event.AbstractEvent;
import org.osgi.framework.FrameworkEvent;

/**
 * BundleEvent implementation on top of KernelEvent.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractFrameworkEvent extends AbstractEvent implements FrameworkEventAdapter
{
   // todo - framework event type, Bundle
   public AbstractFrameworkEvent(Object source, String type, long sequence, long timestamp, Object context)
   {
      super(source, type, sequence, timestamp, context);
   }

   public FrameworkEvent getEvent()
   {
      int frameworkType = 0;
      return new FrameworkEvent(frameworkType, null, null);
   }
}
