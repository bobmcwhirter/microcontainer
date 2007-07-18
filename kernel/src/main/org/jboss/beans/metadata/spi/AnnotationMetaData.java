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

import java.lang.annotation.Annotation;

import org.jboss.util.JBossInterface;

/**
 * Metadata about an annotation attribute.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface AnnotationMetaData extends JBossInterface, MetaDataVisitorNode
{
   /**
    * Get the annotation using passed in classloader
    * @param classloader The classloader
    * @return The annotation instance
    */
   Annotation getAnnotationInstance(ClassLoader classloader);

   /**
    * Get the annotation using the thread context classloader
    * @return The annotation instance
    */
   Annotation getAnnotationInstance();
}
