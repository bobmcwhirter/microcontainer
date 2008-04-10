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


/**
 * A factory for creating a collection of related mc beans based on a
 * template of BeanMetaData[] from a BeanMetaDataFactory.
 * 
 * @param <T> the instance type
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public interface ComponentFactory<T>
{
   /**
    * the factory which defines template BeanMetaData[] for the components
    * @return the BeanMetaDataFactory defining the component beans
    */
   public ComponentBeanMetaDataFactory getFactory();

   /**
    * Install a collection of mc beans based on the factory metadata.
    * 
    * @param baseName - the base bean name used in conjuction wth the factory.getBeans()
    *    BeanMetaData instances getName() to build the unique bean name:
    *    baseName + bmd.getName() + "#" + compID;
    * @return the component context instance information.
    * @throws Throwable - on failure to install the component beans
    */
   public ComponentInstance<T> createComponents(String baseName)
      throws Throwable;

   /**
    * Extract the unique component id from a component bean name.
    * @param name - a name previously returned from createComponents.
    * @return the component id portion of the name
    * @throws NumberFormatException - if name is not a valild bean component
    *    name with a component id.
    */
   public long getComponentID(String name) throws NumberFormatException;

   /**
    * Uninstall the component beans for the given instance
    * @param instance - the ComponentInstance previously returned from createComponents
    * @throws Exception - on failure to uninstall the component beans
    */
   public void destroyComponents(ComponentInstance<T> instance)
      throws Exception;
}
