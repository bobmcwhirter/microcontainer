/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.kernel.deployment.support.container.spi;

import java.util.List;

/**
 * The component context instance. This is the bean that acts as the container
 * for the component bean instances.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public interface ComponentInstance<T>
{
   /**
    * Get the name of the bean for the component context instance
    * @return
    */
   public String getContextName();
   /**
    * Get the component bean names
    * @return
    */
   public List<String> getComponentNames();
   /**
    * Get the id associated with this component
    * @return
    */
   public long getComponentID();
   /**
    * Get the component context instance
    * @return
    */
   public T getContext();

}
