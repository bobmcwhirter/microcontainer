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
package org.jboss.deployers.spi.structure.vfs;

import java.util.Map;

/**
 * A classpath entry.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public interface ClassPathInfo
{
   /**
    * path relative to the context virtual file.
    */
   public String getPath();
   public void setPath(String path);
   /**
    * Get the options associated with the classpath entry.
    * @return A map of entry options.
    */
   public Map getOptions();
   /**
    * Set the options associated with the classpath entry.
    * @param options - A map of entry options.
    */
   public void setOptions(Map options);

   /**
    * Get a classpath entry option
    * @param key - the option key
    * @return the option if it exists, null otherwise
    */
   public Object getOption(Object key);
   /**
    * Set a classpath entry option
    * @param key - the option key
    * @param value - the option value
    */
   public void setOption(Object key, Object value);
}
