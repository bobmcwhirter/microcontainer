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
package org.jboss.beans.metadata.plugins.policy;

import java.io.Serializable;
import java.util.Iterator;

import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.policy.ScopeMetaData;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Meta data for scope.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractScopeMetaData extends JBossObject implements ScopeMetaData, Serializable
{
   private static final long serialVersionUID = 1;

   private String level;
   private String qualifier;

   public String getLevel()
   {
      return level;
   }

   public String getQualifier()
   {
      return qualifier;
   }

   public void initialVisit(MetaDataVisitor vistor)
   {
      vistor.initialVisit(this);
   }

   public void describeVisit(MetaDataVisitor vistor)
   {
      vistor.describeVisit(this);
   }

   public Iterator<? extends MetaDataVisitorNode> getChildren()
   {
      return null;
   }

   public void setLevel(String level)
   {
      this.level = level;
   }

   public void setQualifier(String qualifier)
   {
      this.qualifier = qualifier;
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("level=").append(level);
      buffer.append(" qualifier=").append(qualifier);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(level);
      buffer.append('/');
      buffer.append(qualifier);
   }

}
