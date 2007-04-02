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
package org.jboss.osgi.plugins.dependency;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;

import org.jboss.dependency.plugins.AbstractControllerContext;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;
import org.jboss.osgi.spi.metadata.ServiceMetaData;
import org.jboss.osgi.spi.metadata.ServiceMetaDataVisitor;
import org.jboss.osgi.spi.metadata.ServiceMetaDataVisitorNode;
import org.osgi.framework.BundleContext;

/**
 * OSGi service controller context
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class OSGiServiceReferenceContext extends AbstractControllerContext implements InvokeDispatchContext
{
   /** The meta data */
   private ServiceMetaData serviceMetaData;

   /** The Bundle Context */
   private BundleContext bundleContext;

   public OSGiServiceReferenceContext(ServiceMetaData metaData, BundleContext bundleContext)
   {
      super(metaData.getId(), OSGiServiceReferenceContextActions.getInstance());
      this.bundleContext = bundleContext;
   }

   public Object get(String name) throws Throwable
   {
      return null; // todo
   }

   public void set(String name, Object value) throws Throwable
   {
   }

   public Object invoke(String name, Object parameters[], String[] signature) throws Throwable
   {
      return null; // todo
   }

   public ClassLoader getClassLoader() throws Throwable
   {
      return null; // todo get Bundle CL
   }

   /**
    * Get the serviceMetaData.
    *
    * @return the serviceMetaData.
    */
   public ServiceMetaData getServiceMetaData()
   {
      return serviceMetaData;
   }

   /**
    * Set the serviceMetaData.
    *
    * @param serviceMetaData the serviceMetaData.
    */
   public void setServiceMetaData(ServiceMetaData serviceMetaData)
   {
      this.serviceMetaData = serviceMetaData;
   }

   /**
    * Get Bundle Context
    *
    * @return the bundle context
    */
   public BundleContext getBundleContext()
   {
      return bundleContext;
   }

   /**
    * Get the service proxy
    *
    * @return the service proxy
    * @throws Exception for any error
    */
   public Object getServiceProxy() throws Exception
   {
      return null; // todo
   }

   public void setController(Controller controller)
   {
      super.setController(controller);
      if (controller != null)
      {
         preprocessMetaData();
      }
   }

   /**
    * Preprocess the metadata for this context
    */
   protected void preprocessMetaData()
   {
      if (serviceMetaData == null)
         return;
      PreprocessMetaDataVisitor visitor = new PreprocessMetaDataVisitor();
      AccessController.doPrivileged(visitor);
   }

   /**
    * A visitor for the metadata that looks for dependencies.
    */
   protected class PreprocessMetaDataVisitor implements ServiceMetaDataVisitor, PrivilegedAction<Object>
   {
      /** The current context for when the dependencies are required */
      private ControllerState contextState = ControllerState.INSTANTIATED;

      /**
       * Visit the bean metadata node, this is the starting point
       */
      public Object run()
      {
         serviceMetaData.visit(this);
         return null;
      }

      /**
       * Visit a node
       *
       * @param node the node
       */
      public void visit(ServiceMetaDataVisitorNode node)
      {
         boolean trace = log.isTraceEnabled();
         if (trace)
            log.trace("Visit node " + node);

         // Visit the children of this node
         Iterator<? extends ServiceMetaDataVisitorNode> children = node.getChildren();
         if (children != null)
         {
            ControllerState restoreState = contextState;
            while (children.hasNext())
            {
               ServiceMetaDataVisitorNode child = children.next();
               try
               {
                  child.visit(this);
               }
               finally
               {
                  contextState = restoreState;
               }
            }
         }
      }

      public OSGiServiceReferenceContext getControllerContext()
      {
         return OSGiServiceReferenceContext.this;
      }

      public ControllerState getContextState()
      {
         return contextState;
      }

      public void addDependency(DependencyItem dependency)
      {
         getDependencyInfo().addIDependOn(dependency);
      }

      public void setContextState(ControllerState contextState)
      {
         this.contextState = contextState;
      }
   }


}
