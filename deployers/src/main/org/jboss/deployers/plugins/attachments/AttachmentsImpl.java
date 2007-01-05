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
package org.jboss.deployers.plugins.attachments;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Attachments implementation
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class AttachmentsImpl extends AbstractAttachments
{
   private static final long serialVersionUID = 1;

   /** The attachments */
   private Map<String, Object> attachments = new ConcurrentHashMap<String, Object>();

   public Map<String, Object> getAttachments()
   {
      return Collections.unmodifiableMap(attachments);
   }
   public void setAttachments(Map<String, Object> map)
   {
      attachments.clear();
      attachments.putAll(map);
   }

   public Object addAttachment(String name, Object attachment)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      if (attachment == null)
         throw new IllegalArgumentException("Null attachment");
      return attachments.put(name, attachment);
   }

   public Object getAttachment(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      return attachments.get(name);
   }

   public boolean isAttachmentPresent(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      return attachments.containsKey(name);
   }

   public Object removeAttachment(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      return attachments.remove(name);
   }

   public void clear()
   {
      attachments.clear();
   }

   public boolean hasAttachments()
   {
      return attachments.isEmpty() == false;
   }
}
