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
package org.jboss.deployers.spi.attachments;

import java.util.Map;

/**
 * Attachments
 * 
 * Represents a set of attachments
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface Attachments
{
   /**
    * Get all the attachments
    * 
    * @return the unmodifiable attachments
    */
   Map<String, Object> getAttachments();

   /**
    * Add attachment
    *
    * @param name the name of the attachment
    * @param attachment the attachment
    * @return any previous attachment
    * @throws IllegalArgumentException for a null name or attachment
    */
   Object addAttachment(String name, Object attachment);

   /**
    * Add attachment
    *
    * @param <T> the expected type
    * @param name the name of the attachment
    * @param attachment the attachment
    * @param expectedType the expected type
    * @return any previous attachment
    * @throws IllegalArgumentException for a null name, attachment or expectedType
    */
   <T> T addAttachment(String name, T attachment, Class<T> expectedType);

   /**
    * Add attachment
    *
    * @param <T> the expected type
    * @param attachment the attachment
    * @param type the type
    * @return any previous attachment
    * @throws IllegalArgumentException for a null name, attachment or type
    */
   <T> T addAttachment(Class<T> type, T attachment);

   /**
    * Get attachment
    * 
    * @param name the name of the attachment
    * @return the attachment or null if not present
    * @throws IllegalArgumentException for a null name
    */
   Object getAttachment(String name);

   /**
    * Get attachment
    * 
    * @param <T> the expected type
    * @param name the name of the attachment
    * @param expectedType the expected type
    * @return the attachment or null if not present
    * @throws IllegalArgumentException for a null name or expectedType
    */
   <T> T getAttachment(String name, Class<T> expectedType);

   /**
    * Get attachment
    * 
    * @param <T> the expected type
    * @param type the type
    * @return the attachment or null if not present
    * @throws IllegalArgumentException for a null name or type
    */
   <T> T getAttachment(Class<T> type);
   
   /**
    * Is the attachment present
    * 
    * @param name the name of the attachment
    * @return true when the attachment is present
    * @throws IllegalArgumentException for a null name
    */
   boolean isAttachmentPresent(String name);
   
   /**
    * Is the attachment present
    * 
    * @param name the name of the attachment
    * @param expectedType the expected type
    * @return true when the attachment is present
    * @throws IllegalArgumentException for a null name or expectedType
    */
   boolean isAttachmentPresent(String name, Class<?> expectedType);
   
   /**
    * Is the attachment present
    * 
    * @param type the type
    * @return true when the attachment is present
    * @throws IllegalArgumentException for a null name or type
    */
   boolean isAttachmentPresent(Class<?> type);

   /**
    * Remove attachment
    * 
    * @param name the name of the attachment
    * @return the attachment or null if not present
    * @throws IllegalArgumentException for a null name
    */
   Object removeAttachment(String name);

   /**
    * Remove attachment
    * 
    * @param <T> the expected type
    * @param name the name of the attachment
    * @return the attachment or null if not present
    * @param expectedType the expected type
    * @throws IllegalArgumentException for a null name or expectedType
    */
   <T> T removeAttachment(String name, Class<T> expectedType);

   /**
    * Remove attachment
    * 
    * @param <T> the expected type
    * @return the attachment or null if not present
    * @param type the type
    * @throws IllegalArgumentException for a null name or type
    */
   <T> T removeAttachment(Class<T> type);
   
   /**
    * Clear the attachments
    * 
    * @throws UnsupportedOperationException when not supported by the implementation
    */
   void clear();

   /**
    * Are there any attachments
    * @return true if there are any attachments, false otherwise.
    */
   public boolean hasAttachments();

   /**
    * Get the number of changes that have happened. 
    * @return Number of adds/removes that have happened since
    * creation or clearChangeCount.
    */
   public int getChangeCount();

   /**
    * Reset the change count to zero.
    */
   public void clearChangeCount();
}
