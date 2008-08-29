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
package org.jboss.test.kernel.event.support;

import org.jboss.kernel.spi.event.KernelEvent;

/**
 * An expected event.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class TestEvent implements KernelEvent
{
   /** The source */
   public Object source;
   
   /** The type */
   public String type;
   
   /** The sequence */
   public long sequence;
   
   /** The context */
   public Object context;
   
   /** The handback */
   public Object handback;

   public TestEvent(Object source, String type, long sequence, Object context, Object handback)
   {
      this.source = source;
      this.type = type;
      this.sequence = sequence;
      this.context = context;
      this.handback = handback;
   }
   
   public TestEvent(KernelEvent event, Object handback)
   {
      this.source = event.getSource();
      this.type = event.getType();
      this.sequence = event.getSequence();
      this.context = event.getContext();
      this.handback = handback;
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
      return 0;
   }

   public String getType()
   {
      return type;
   }

   public boolean equals(Object object)
   {
      TestEvent other = (TestEvent) object;
      if (source.equals(other.source) == false)
         return false;
      if (type.equals(other.type) == false)
         return false;
      if (sequence != other.sequence)
         return false;
      if (context == null && other.context != null)
         return false;
      if (context != null && context.equals(other.context) == false)
         return false;
      if (handback == null && other.handback != null)
         return false;
      if (handback != null && handback.equals(other.handback) == false)
         return false;
      return true;
   }
   
   public String toString()
   {
      StringBuffer buffer = new StringBuffer();
      buffer.append("(source=").append(source);
      buffer.append(" type=").append(type);
      buffer.append(" seqn=").append(sequence);
      buffer.append(" ctx=").append(context);
      buffer.append(" handback=").append(handback);
      buffer.append(")");
      return buffer.toString();
   }
}