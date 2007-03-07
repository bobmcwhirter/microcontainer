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
package org.jboss.beans.metadata.spi;

import java.util.List;
import java.util.Set;

import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;

/**
 * Metadata about a bean.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface BeanMetaData extends FeatureMetaData, ValueMetaData
{
   /**
    * Get the bean.
    * 
    * @return the bean.
    */
   String getBean();

   /**
    * Get the name
    * 
    * @return the name
    */
   String getName();
   
   /**
    * Set the name
    * 
    * @param name the name
    */
   void setName(String name);

   /**
    * Get the mode
    * 
    * @return the mode
    */
   ControllerMode getMode();
   
   /**
    * Set the name
    * 
    * @param mode the mode
    */
   void setMode(ControllerMode mode);

   /**
    * Set the annotations
    *
    * @param annotations the annotations
    */
   void setAnnotations(Set<AnnotationMetaData> annotations);

   /**
    * Get the properties.
    * 
    * @return List<PropertyMetaData>.
    */
   Set<PropertyMetaData> getProperties();

   /**
    * Get the bean ClassLoader
    * 
    * @return the ClassLoader metadata
    */
   ClassLoaderMetaData getClassLoader();

   /**
    * Set the bean ClassLoader
    * 
    * @param classLoader the ClassLoader metadata
    */
   void setClassLoader(ClassLoaderMetaData classLoader);

   /**
    * Get the constructor
    * 
    * @return the constructor metadata
    */
   ConstructorMetaData getConstructor();

   /**
    * Get the create lifecycle
    * 
    * @return the create lifecycle
    */
   LifecycleMetaData getCreate();

   /**
    * Get the start lifecycle
    * 
    * @return the start lifecycle
    */
   LifecycleMetaData getStart();

   /**
    * Get the stop lifecycle
    * 
    * @return the stop lifecycle
    */
   LifecycleMetaData getStop();

   /**
    * Get the destroy lifecycle
    * 
    * @return the destroy lifecycle
    */
   LifecycleMetaData getDestroy();
   
   /**
    * Get what this bean demands.
    * 
    * @return Set<DemandMetaData>
    */
   Set<DemandMetaData> getDemands();

   /**
    * Get what this bean supplies.
    * 
    * @return Set<SupplyMetaData>
    */
   Set<SupplyMetaData> getSupplies();
   
   /**
    * Get what this bean depends.
    * 
    * @return Set<DependencyMetaData>
    */
   Set<DependencyMetaData> getDepends();

   /**
    * Get the installation oeprations.
    * 
    * @return List<InstallMetaData>
    */
   List<InstallMetaData> getInstalls();

   /**
    * Get the uninstallation operations.
    * 
    * @return List<InstallMetaData>
    */
   List<InstallMetaData> getUninstalls();
   
   /**
    * Get the lifecycle callbacks.
    * 
    * @return List<LifecycleCallbackMetaData>
    */
   List<LifecycleCallbackMetaData> getLifecycleCallbacks();
   
   /**
    * Get the lifecycle callbacks for a particular state.
    * 
    * @return List<LifecycleCallbackMetaData>
    */
   List<LifecycleCallbackMetaData> getLifecycleCallbacks(ControllerState state);
}
