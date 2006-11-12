/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.deployers.plugins.structure;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.jboss.deployers.spi.structure.vfs.ClassPathInfo;

/**
 * A representation of a classpath element. This is a vfs relative path to
 * the root of the deployment its associated with.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class ClassPathInfoImpl
   implements ClassPathInfo, Serializable
{
   private static final long serialVersionUID = 1;

   private String path;
   private HashMap options = new HashMap();

   public ClassPathInfoImpl()
   {
      this(null);
   }
   public ClassPathInfoImpl(String path)
   {
      this.path = path;
   }

   public String getPath()
   {
      return path;
   }
   public void setPath(String path)
   {
      this.path = path;
   }

   public Object getOption(Object key)
   {
      return options.get(key);
   }

   public Map getOptions()
   {
      return options;
   }

   public void setOption(Object key, Object value)
   {
      options.put(key, value);
   }

   public void setOptions(Map options)
   {
      this.options.clear();
      this.options.putAll(options);
   }

   public String toString()
   {
      StringBuilder tmp = new StringBuilder();
      tmp.append("ClassPathInfo(");
      tmp.append(path);
      tmp.append(')');
      return tmp.toString();
   }
}
