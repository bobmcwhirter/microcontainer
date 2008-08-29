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
import java.lang.annotation.Annotation;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;

import org.jboss.annotation.factory.AnnotationCreator;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.policy.ScopeMetaData;
import org.jboss.metadata.spi.scope.ScopeFactory;
import org.jboss.metadata.spi.scope.ScopeFactoryLookup;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.metadata.spi.scope.ScopeLevel;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Meta data for scope.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@XmlRootElement(name="scope")
@XmlType(name="scopeType")
public class AbstractScopeMetaData extends JBossObject implements ScopeMetaData, Serializable
{
   private static final long serialVersionUID = 2;

   private String scope;
   private String level;
   private String qualifier;

   @XmlTransient
   public Object getUnderlyingValue()
   {
      return scope;
   }

   @SuppressWarnings("unchecked")
   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      if (scope != null)
      {
         Annotation annotation = (Annotation)AnnotationCreator.createAnnotation(scope, cl);
         ScopeFactoryLookup scopeFactoryLookup = annotation.getClass().getAnnotation(ScopeFactoryLookup.class);
         if (scopeFactoryLookup != null)
         {
            // TODO do we really want to create a new factory for every annotation we look at?
            // We could use some kind of soft reference map here (per classloader)
            // Class<?> clazz = scopeFactoryLookup.value();
            // WeakHashMap.put(clazz.getClassLoader(), new SoftMap())
            // SoftValueHashMap.put(clazz.getName(), instance)
            // If this is done, it should be moved to a common helper class
            // that does generic construction from a factory given on the meta annotations
            ScopeFactory scopeFactory = scopeFactoryLookup.value().newInstance();
            return scopeFactory.create(annotation);
         }
         else
         {
            // todo - annotation.value();
            return createScopeKey(annotation.getClass().getSimpleName(), annotation.toString());
         }
      }
      else
      {
         return createScopeKey(level, qualifier);
      }
   }

   private ScopeKey createScopeKey(String name, String qualifier)
   {
      int level = ScopeLevel.getScopeLevel(name);
      return new ScopeKey(new ScopeLevel(level, name), qualifier);
   }

   public void initialVisit(MetaDataVisitor vistor)
   {
      vistor.initialVisit(this);
   }

   public void describeVisit(MetaDataVisitor vistor)
   {
      vistor.describeVisit(this);
   }

   @XmlTransient
   public Iterator<? extends MetaDataVisitorNode> getChildren()
   {
      return null;
   }

   @XmlTransient
   public String getScope()
   {
      return scope;
   }

   public String getLevel()
   {
      return level;
   }

   public String getQualifier()
   {
      return qualifier;
   }

   public void setScope(String scope)
   {
      this.scope = scope;
   }

   @XmlAttribute
   public void setLevel(String level)
   {
      this.level = level;
   }

   @XmlAttribute
   public void setQualifier(String qualifier)
   {
      this.qualifier = qualifier;
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("scope=").append(scope);
      buffer.append(" level=").append(level);
      buffer.append(" qualifier=").append(qualifier);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(scope);
      buffer.append('/');
      buffer.append(level);
      buffer.append('/');
      buffer.append(qualifier);
   }
}
