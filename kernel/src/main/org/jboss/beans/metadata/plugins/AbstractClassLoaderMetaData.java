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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAnyElement;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.ClassLoaderMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;
import org.jboss.managed.api.annotation.ManagementProperty;

/**
 * A classloader.
 * 
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
@XmlType(name="classloaderType", propOrder="classLoader")
public class AbstractClassLoaderMetaData extends JBossObject
   implements ClassLoaderMetaData, BeanMetaDataFactory, Serializable
{
   private static final long serialVersionUID = 2L;

   /** The classloader */
   protected ValueMetaData classloader;

   /**
    * Create a new classloader
    */
   public AbstractClassLoaderMetaData()
   {
   }

   /**
    * Create a new classloader
    * 
    * @param classloader the classloader value
    */
   public AbstractClassLoaderMetaData(ValueMetaData classloader)
   {
      this.classloader = classloader;
   }

   /**
    * Set the classloader
    * 
    * @param classloader the classloader value
    */
   @XmlElements
   ({
      @XmlElement(name="bean", type=AbstractBeanMetaData.class),
      @XmlElement(name="inject", type=AbstractDependencyValueMetaData.class),
      @XmlElement(name="null", type=AbstractValueMetaData.class)
   })
   public void setClassLoader(ValueMetaData classloader)
   {
      this.classloader = classloader;
      flushJBossObjectCache();
   }

   public ValueMetaData getClassLoader()
   {
      return classloader;
   }

   @XmlAnyElement
   @ManagementProperty(ignored = true)
   public void setClassLoaderObject(Object classloader)
   {
      if (classloader == null)
         setClassLoader(null);
      else if (classloader instanceof ValueMetaData)
         setClassLoader((ValueMetaData) classloader);
      else
         setClassLoader(new AbstractValueMetaData(classloader));
   }

   public List<BeanMetaData> getBeans()
   {
      if (classloader instanceof BeanMetaDataFactory)
      {
         return ((BeanMetaDataFactory)classloader).getBeans();
      }
      else if (classloader instanceof BeanMetaData)
      {
         return Collections.singletonList((BeanMetaData)classloader);
      }
      return new ArrayList<BeanMetaData>();
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      visitor.setContextState(ControllerState.PRE_INSTALL);
      visitor.initialVisit(this);
   }

   public void describeVisit(MetaDataVisitor vistor)
   {
      // todo - any context setting?
      vistor.describeVisit(this);
   }

   public Iterator<? extends MetaDataVisitorNode> getChildren()
   {
      if (classloader != null)
         return Collections.singletonList(classloader).iterator();
      return null;
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("classloader=").append(classloader);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(classloader);
   }
}
