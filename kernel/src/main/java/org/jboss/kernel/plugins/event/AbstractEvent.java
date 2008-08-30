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
package org.jboss.kernel.plugins.event;

import org.jboss.kernel.spi.event.KernelEvent;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Abstract Event.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractEvent extends JBossObject implements KernelEvent
{
   /** The source */
   protected Object source;
   
   /** The type */
   protected String type;
   
   /** The sequence */
   protected long sequence;
   
   /** The timestamp */
   protected long timestamp;
   
   /** The context */
   protected Object context;

   /**
    * Create a new abstract event
    * 
    * @param source the source
    * @param type the type
    * @param sequence the sequeunce
    * @param timestamp the timestamp
    * @param context the context
    */
   public AbstractEvent(Object source, String type, long sequence, long timestamp, Object context)
   {
      this.source = source;
      this.type = type;
      this.sequence = sequence;
      this.timestamp = timestamp;
      this.context = context;
   }

   public Object getContext()
   {
      return context;
   }
   
   public long getSequence()
   {
      return sequence;
   }
   
   public Object getSource()
   {
      return source;
   }
   
   public long getTimestamp()
   {
      return timestamp;
   }
   
   public String getType()
   {
      return type;
   }
   
   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("source=").append(source);
      buffer.append(" type=").append(type);
      buffer.append(" seq=").append(sequence);
      buffer.append(" time=").append(timestamp);
      buffer.append(" context=").append(context);
   }
   
   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(type);
   }
}
