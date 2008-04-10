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
package org.jboss.test.kernel.deployment.support.container;

import java.util.Set;

/**
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class TestInjectionMetaData
{
   /** The mapped name */
   private String mappedName;
   private String resolvedJndiName;
   private boolean ignoreDependency;

   /** The injection targets */
   private Set<TestInjectionTargetMetaData> injectionTargets;
   
   /**
    * Create a new ResourceInjectionMetaData.
    */
   public TestInjectionMetaData()
   {
      // For serialization
   }

   /**
    * Get the jndiName.
    * 
    * @return the jndiName.
    */
   public String getJndiName()
   {
      return getMappedName();
   }

   /**
    * Set the jndiName.
    * 
    * @param jndiName the jndiName.
    * @throws IllegalArgumentException for a null jndiName
    */
   public void setJndiName(String jndiName)
   {
      setMappedName(jndiName);
   }
   
   /**
    * Get the mappedName.
    * 
    * @return the mappedName.
    */
   public String getMappedName()
   {
      return mappedName;
   }

   /**
    * Set the mappedName.
    * 
    * @param mappedName the mappedName.
    * @throws IllegalArgumentException for a null mappedName
    */
   public void setMappedName(String mappedName)
   {
      if (mappedName == null)
         throw new IllegalArgumentException("Null mappedName");
      this.mappedName = mappedName;
   }

   /**
    * An unmanaged runtime jndi name for the resource. Used by deployers to
    * propagate resolved resource location.
    * 
    * @return the resolved jndi name
    */
   public String getResolvedJndiName()
   {
      return resolvedJndiName;
   }
   public void setResolvedJndiName(String resolvedJndiName)
   {
      this.resolvedJndiName = resolvedJndiName;
   }

   /**
    * Get the injectionTargets.
    * 
    * @return the injectionTargets.
    */
   public Set<TestInjectionTargetMetaData> getInjectionTargets()
   {
      return injectionTargets;
   }

   /**
    * Set the injectionTargets.
    * 
    * @param injectionTargets the injectionTargets.
    * @throws IllegalArgumentException for a null injectionTargets
    */
   public void setInjectionTargets(Set<TestInjectionTargetMetaData> injectionTargets)
   {
      if (injectionTargets == null)
         throw new IllegalArgumentException("Null injectionTargets");
      this.injectionTargets = injectionTargets;
   }

   /**
    * Get the ignoreDependency.
    * 
    * @return the ignoreDependency.
    */
   public boolean getIgnoreDependency()
   {
      return ignoreDependency;
   }

   /**
    * Set the ignoreDependency.
    * 
    * @param flag the ignoreDependency.
    * @throws IllegalArgumentException for a null ignoreDependency
    */
   public void setIgnoreDependency(boolean flag)
   {
      this.ignoreDependency = flag;
   }

   /**
    * Get whether the dependency is ignored
    * 
    * @return true when the dependency is ignored
    */
   public boolean isDependencyIgnored()
   {
      return ignoreDependency;
   }
}
