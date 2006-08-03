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
package org.jboss.beans.metadata.plugins;

import java.util.List;
import java.util.Set;

import org.jboss.beans.metadata.spi.LifecycleMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Metadata for lifecycle.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractLifecycleMetaData extends AbstractFeatureMetaData implements LifecycleMetaData
{
   /** The state */
   protected ControllerState state;
   
   /** The method name */
   protected String methodName;
   
   /** The paramaters List<ParameterMetaData> */
   protected List<ParameterMetaData> parameters;

   /**
    * Create a new lifecycle meta data
    */
   public AbstractLifecycleMetaData()
   {
   }

   public ControllerState getState()
   {
      return state;
   }

   public void setState(ControllerState state)
   {
      this.state = state;
   }
   
   public String getMethodName()
   {
      return methodName;
   }
   
   /**
    * Set the method name
    * 
    * @param name the factory method
    */
   public void setMethodName(String name)
   {
      this.methodName = name;
      flushJBossObjectCache();
   }
   
   public List<ParameterMetaData> getParameters()
   {
      return parameters;
   }
   
   /**
    * Set the parameters
    * 
    * @param parameters List<ParameterMetaData>
    */
   public void setParameters(List<ParameterMetaData> parameters)
   {
      this.parameters = parameters;
      flushJBossObjectCache();
   }

   public void visit(MetaDataVisitor visitor)
   {
      visitor.setContextState(state);
      super.visit(visitor);
   }
   
   protected void addChildren(Set<MetaDataVisitorNode> children)
   {
      super.addChildren(children);
      if (parameters != null)
         children.addAll(parameters);
   }
   
   public void toString(JBossStringBuilder buffer)
   {
      if (methodName != null)
         buffer.append("method=").append(methodName);
      buffer.append(" parameters=");
      JBossObject.list(buffer, parameters);
      buffer.append(" ");
      super.toString(buffer);
   }
   
   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(methodName);
   }
}
