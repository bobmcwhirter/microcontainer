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
package org.jboss.test.kernel.config.support;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SimpleLifecycleBean
{
   private String create;
   private String start;
   private String stop;
   private String destroy;

   private String install;

   public void create(String param)
   {
      create = param;
   }

   public void doStart(String param)
   {
      start = param;
   }

   public void doStop(String param)
   {
      stop = param;
   }

   public void destroy(String param)
   {
      destroy = param;
   }

   public String getCreate()
   {
      return create;
   }

   public String getStart()
   {
      return start;
   }

   public String getStop()
   {
      return stop;
   }

   public String getDestroy()
   {
      return destroy;
   }

   public String getInstall()
   {
      return install;
   }

   public void installParam(String param)
   {
      install = param;
   }

   public void uninstallParam(String param)
   {
      install = param;      
   }

}
