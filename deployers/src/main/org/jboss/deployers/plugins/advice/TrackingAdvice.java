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
package org.jboss.deployers.plugins.advice;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.aop.microcontainer.aspects.util.ProxyUtils;
import org.jboss.deployers.spi.attachments.Attachments;

/**
 * An advice for capturing changes made to an Attachments.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class TrackingAdvice
{
   private static ConcurrentHashMap<Object, Map<String, Object>> attachmentsByTarget =
      new ConcurrentHashMap<Object, Map<String, Object>>();

   public static Attachments wrapAttachments(Attachments attachments)
   {
      return ProxyUtils.createProxy(attachments, Attachments.class);
   }

   public static Object invoke(Invocation inv)
      throws Throwable
   {
      return inv.invokeNext();
   }

   public static Object addAttachment(MethodInvocation mi)
      throws Throwable
   {
      Object target = mi.getTargetObject();
      Object[] args = mi.getArguments();
      Object value = mi.invokeNext();
      // check for legal args length
      if (args == null || args.length < 2)
         throw new IllegalArgumentException("Illegal method invocation, possibly not adding attachment?");

      String name;
      Object attachment;
      // addAttachment(Class<T> type, T attachment)
      if( args[0] instanceof Class )
      {
         Class c = Class.class.cast(args[0]);
         name = c.getName();
         attachment = args[1];
      }
      // addAttachment(String name, T attachment, Class<T> expectedType)
      // addAttachment(String name, T attachment)
      else
      {
         name = String.class.cast(args[0]);
         attachment = args[1];
      }
      addAttachment(target, name, attachment);
      return value;
   }

   public static Object removeAttachment(MethodInvocation mi)
      throws Throwable
   {
      Object target = mi.getTargetObject();
      Object[] args = mi.getArguments();
      Object value = mi.invokeNext();
      // check for legal args length
      if (args == null || args.length < 1)
         throw new IllegalArgumentException("Illegal method invocation, possibly not removing attachment?");

      String name;
      // removeAttachment(Class<T> type)
      if( args[0] instanceof Class )
      {
         Class c = Class.class.cast(args[0]);
         name = c.getName();
      }
      // removeAttachment(String name, Class<T> expectedType)
      // removeAttachment(String name)
      else
      {
         name = String.class.cast(args[0]);
      }
      removeAttachment(target, name);
      return value;
   }

   public static Map<String, Object> getAttachmentsForTarget(Object key)
   {
      return attachmentsByTarget.get(key);
   }

   public static Map<String, Object> clearAttachmentsForTarget(Object key)
   {
      return attachmentsByTarget.remove(key);
   }

   private static void addAttachment(Object target, String name, Object attachment)
   {
      Map<String, Object> attachments = attachmentsByTarget.get(target);
      if( attachments == null )
      {
         attachments = new HashMap<String, Object>();
         attachmentsByTarget.put(target, attachments);
      }
      attachments.put(name, attachment);
   }

   private static void removeAttachment(Object target, String name)
   {
      Map<String, Object> attachments = attachmentsByTarget.get(target);
      if( attachments != null )
      {
         attachments.remove(name);
         if( attachments.size() == 0 )
            attachmentsByTarget.remove(target);
      }
   }
}
