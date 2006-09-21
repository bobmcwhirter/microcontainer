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
package org.jboss.deployers.spi;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * IncompleteDeployments.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class IncompleteDeployments
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1433292979582684692L;

   /** Deployments in error */
   private Map<String, Throwable> deploymentsInError;

   /** Deployments missing deployer */
   private Collection<String> deploymentsMissingDeployer;
   
   /** Contexts in error */
   private Map<String, Throwable> contextsInError;
   
   /** Contexts missing dependencies */
   private Map<String, Set<MissingDependency>> contextsMissingDependencies;

   /**
    * Create a new IncompleteDeploymentException.
    * 
    * @param deploymentsInError
    * @param deploymentsMissingDeployer
    * @param contextsInError
    * @param contextsMissingDependencies
    */
   public IncompleteDeployments(Map<String, Throwable> deploymentsInError, Collection<String> deploymentsMissingDeployer, Map<String, Throwable> contextsInError, Map<String, Set<MissingDependency>> contextsMissingDependencies)
   {
      if (deploymentsInError != null && deploymentsInError.isEmpty() == false)
      {
         this.deploymentsInError = new TreeMap<String, Throwable>();
         this.deploymentsInError.putAll(deploymentsInError);
      }
      if (deploymentsMissingDeployer != null && deploymentsMissingDeployer.isEmpty() == false)
      {
         this.deploymentsMissingDeployer = new TreeSet<String>();
         this.deploymentsMissingDeployer.addAll(deploymentsMissingDeployer);
      }
      if (contextsInError != null && contextsInError.isEmpty() == false)
      {
         this.contextsInError = new TreeMap<String, Throwable>();
         this.contextsInError.putAll(contextsInError);
      }
      if (contextsMissingDependencies != null && contextsMissingDependencies.isEmpty() == false)
      {
         this.contextsMissingDependencies = new TreeMap<String, Set<MissingDependency>>();
         this.contextsMissingDependencies.putAll(contextsMissingDependencies);
      }
   }

   /**
    * Whether it is incomplete
    * 
    * @return true when incomplete
    */
   public boolean isIncomplete()
   {
      if (deploymentsInError != null)
         return true;
      if (deploymentsMissingDeployer != null )
         return true;
      if (contextsInError != null)
         return true;
      if (contextsMissingDependencies != null)
         return true;
      return false;
   }
   
   /**
    * Get the contextsInError.
    * 
    * @return the contextsInError.
    */
   public Map<String, Throwable> getContextsInError()
   {
      if (contextsInError == null)
         return Collections.emptyMap();
      else
         return Collections.unmodifiableMap(contextsInError);
   }

   /**
    * Get the contextsMissingDependencies.
    * 
    * @return the contextsMissingDependencies.
    */
   public Map<String, Set<MissingDependency>> getContextsMissingDependencies()
   {
      if (contextsMissingDependencies == null)
         return Collections.emptyMap();
      else
         return Collections.unmodifiableMap(contextsMissingDependencies);
   }

   /**
    * Get the deploymentsInError.
    * 
    * @return the deploymentsInError.
    */
   public Map<String, Throwable> getDeploymentsInError()
   {
      if (deploymentsInError == null)
         return Collections.emptyMap();
      else
         return Collections.unmodifiableMap(deploymentsInError);
   }

   /**
    * Get the deploymentsMissingDeployer.
    * 
    * @return the deploymentsMissingDeployer.
    */
   public Collection<String> getDeploymentsMissingDeployer()
   {
      if (deploymentsMissingDeployer == null)
         return Collections.emptySet();
      else
         return Collections.unmodifiableCollection(deploymentsMissingDeployer);
   }
}
