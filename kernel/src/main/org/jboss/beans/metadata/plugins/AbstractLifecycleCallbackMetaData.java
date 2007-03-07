/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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

import java.util.Iterator;

import org.jboss.beans.metadata.spi.LifecycleCallbackMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Metadata about lifecycle callbacks that should be invoked once a 
 * bean reaches a certain lifecycle state
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class AbstractLifecycleCallbackMetaData extends JBossObject 
implements LifecycleCallbackMetaData, MetaDataVisitorNode
{
   String bean;
   ControllerState whenRequired;
   ControllerState dependentState;
   String installMethod;
   String uninstallMethod;
   
   public AbstractLifecycleCallbackMetaData(String bean, ControllerState whenRequired, ControllerState dependentState, String installMethod, String uninstallMethod)
   {
      super();
      this.bean = bean;
      this.whenRequired  = whenRequired;
      this.dependentState = dependentState;
      this.installMethod = installMethod;
      this.uninstallMethod = uninstallMethod;
   }

   public String getBean()
   {
      return bean;
   }

   public ControllerState getDependentState()
   {
      return dependentState;
   }

   public ControllerState getWhenRequired()
   {
      return whenRequired;
   }

   public String getInstallMethod()
   {
      return installMethod;
   }

   public String getUninstallMethod()
   {
      return uninstallMethod;
   }

   public void describeVisit(MetaDataVisitor visitor)
   {
      visitor.describeVisit(this);
   }

   public Iterator<? extends MetaDataVisitorNode> getChildren()
   {
      return null;
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      visitor.initialVisit(this);
   }

   public String toShortString()
   {
      JBossStringBuilder buffer = new JBossStringBuilder();
      toShortString(buffer);
      return buffer.toString();
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      if (bean != null)
         buffer.append(bean);
      if (whenRequired != null)
         buffer.append("." + whenRequired.toString());
   }
   
   public String toString()
   {
      return toShortString();
   }
}
