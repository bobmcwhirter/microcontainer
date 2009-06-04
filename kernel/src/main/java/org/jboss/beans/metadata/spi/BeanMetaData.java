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

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ErrorHandlingMode;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.beans.metadata.api.model.AutowireType;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.beans.info.spi.BeanAccessMode;

/**
 * Metadata about a bean. This is the main source of information about a bean, and is the result of parsing a 
 * <code>-beans.xml</code> or reading the bean class annotation. It can also be constructed programatically using 
 * {@link BeanMetaDataBuilder}. The MC will translate the bean metadata into a 
 * {@link KernelControllerContext} that is put into the {@link Controller} to install the bean.
 *  
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @version $Revision$
 */
public interface BeanMetaData extends FeatureMetaData, ValueMetaData
{
   /**
    * Get the bean's classname.
    * 
    * @return the bean's classname.
    */
   String getBean();

   /**
    * Get the name of the bean. This is the name it will be registered under in the {@link Controller}.
    * In the case of a hierarchy of controllers this must be unique across all the controllers.
    * 
    * @return the name
    */
   String getName();

   /**
    * Get the related classes.
    *
    * @return the related classes
    */
   Set<RelatedClassMetaData> getRelated();

   /**
    * Set the name of the bean. This is the name it will be registered under in the {@link Controller}.
    * In the case of a hierarchy of controllers this must be unique across all the controllers.
    * 
    * @param name the name
    */
   void setName(String name);

   /**
    * The aliases. An alias is an alternative name for the bean that is local to a particular 
    * {@link Controller}. Beans can express dependencies on other beans via their alias or
    * their names.
    *
    * @return the aliases or null if there are no aliases
    */
   Set<Object> getAliases();

   /**
    * Get the parent.
    *
    * @return the parent
    */
   String getParent();

   /**
    * Is abstract metadata.
    *
    * @return is abstract
    */
   boolean isAbstract();

   /**
    * Gets the autowire type. If autowiring is enabled then 
    * {@link ControllerContext}s wanting to inject the bean resulting from this bean metadata
    * do not need to specify the name of the {@link ControllerContext}. This specifies how
    * to lookup the autowired beans.
    *  
    * @return the autowire type, or null if we don't want injection by autowiring
    */
   AutowireType getAutowireType();

   /**
    * Get the controller mode to be used when installing this bean.
    * 
    * @return the mode
    */
   ControllerMode getMode();
   
   /**
    * Set the controller mode to be used when installing this bean.
    *
    * @param mode the mode
    */
   void setMode(ControllerMode mode);

   /**
    * Get error handling mode to be used when installing this bean.
    *
    * @return the error handling mode
    */
   ErrorHandlingMode getErrorHandlingMode();

   /**
    * Get the access mode for this bean.
    *
    * @return the access mode
    */
   BeanAccessMode getAccessMode();

   /**
    * Is this bean is a candidate for
    * getting injected via contextual matching
    * or callback resolution. 
    * If autowiring is enabled then 
    * {@link ControllerContext}s wanting to inject the bean resulting from this bean metadata
    * do not need to specify the name of the {@link ControllerContext}. Instead a match between the type of the target property/
    * parameter and my bean type can be used.
    * 
    * @return true (default) if used for autowiring
    */
   boolean isAutowireCandidate();

   /**
    * Get the annotations for this bean. They will eventually end up in the meta data repository for the 
    * resulting {@link ControllerContext}.
    * 
    * @return the bean annotations
    */
   Set<AnnotationMetaData> getAnnotations();
   /**
    * Set the annotations. They will eventually end up in the meta data repository for the 
    * resulting {@link ControllerContext}.
    *
    * @param annotations the annotations
    */
   void setAnnotations(Set<AnnotationMetaData> annotations);

   /**
    * Get the properties for the bean. This will contain the values for each property
    * and can include injections of other beans.
    * 
    * @return List<PropertyMetaData>.
    */
   Set<PropertyMetaData> getProperties();

   /**
    * Get the bean ClassLoader. This is the classloader to use when constructing the bean. If not
    * set the {@link KernelDeployment#getClassLoader()} will be used instead.
    * 
    * @return the ClassLoader metadata
    */
   ClassLoaderMetaData getClassLoader();

   /**
    * Set the bean ClassLoader. This is the classloader to use when constructing the bean. If not
    * set the {@link KernelDeployment#getClassLoader()} will be used instead.
    * 
    * @param classLoader the ClassLoader metadata
    */
   void setClassLoader(ClassLoaderMetaData classLoader);

   /**
    * Get how the bean should be constructed. If null the default constructor will be used.
    * 
    * @return the constructor metadata, or null for the default constructor
    */
   ConstructorMetaData getConstructor();

   /**
    * Get the create lifecycle method.
    * 
    * @return the create lifecycle method
    */
   LifecycleMetaData getCreate();

   /**
    * Get the start lifecycle method.
    * 
    * @return the start lifecycle method
    */
   LifecycleMetaData getStart();

   /**
    * Get the stop lifecycle method.
    * 
    * @return the stop lifecycle method
    */
   LifecycleMetaData getStop();

   /**
    * Get the destroy lifecycle method.
    * 
    * @return the destroy lifecycle method
    */
   LifecycleMetaData getDestroy();
   
   /**
    * Get what this bean demands for dependencies not specified using injections.
    * 
    * @return Set<DemandMetaData>
    */
   Set<DemandMetaData> getDemands();

   /**
    * Get what this bean supplies for dependencies not specified using injections.
    * 
    * @return Set<SupplyMetaData>
    */
   Set<SupplyMetaData> getSupplies();
   
   /**
    * Get what other beans this bean depends on.
    * 
    * @return Set<DependencyMetaData>
    */
   Set<DependencyMetaData> getDepends();

   /**
    * Get the installation lifecycle methods.
    * 
    * @return List<InstallMetaData>
    */
   List<InstallMetaData> getInstalls();

   /**
    * Get the uninstallation lifecycle methods.
    * 
    * @return List<InstallMetaData>
    */
   List<InstallMetaData> getUninstalls();
   
   /**
    * Get the install callbacks that are registered for this bean.
    *
    * @return List<InstallMetaData>
    */
   List<CallbackMetaData> getInstallCallbacks();

   /**
    * Get the uninstall callbacks that are registered for this bean.
    *
    * @return List<InstallMetaData>
    */
   List<CallbackMetaData> getUninstallCallbacks();
}
