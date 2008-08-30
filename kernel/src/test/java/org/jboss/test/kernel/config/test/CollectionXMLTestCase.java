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
package org.jboss.test.kernel.config.test;

import junit.framework.Test;

import org.jboss.test.kernel.config.support.SimpleBean;
import org.jboss.test.kernel.config.support.XMLUtil;
import org.jboss.test.kernel.config.support.UnmodifiableGetterBean;

/**
 * Collection Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class CollectionXMLTestCase extends CollectionTestCase
{
   public static Test suite()
   {
      return suite(CollectionXMLTestCase.class);
   }

   public CollectionXMLTestCase(String name)
   {
      super(name, true);
   }

   public SimpleBean simpleCollectionFromObjects() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected SimpleBean simpleCollectionFromStrings() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected SimpleBean customCollectionExplicit() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected SimpleBean customCollectionFromSignature() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected SimpleBean customCollectionPreInstantiated() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected UnmodifiableGetterBean unmodifiableCollectionPreInstantiated() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (UnmodifiableGetterBean) util.getBean("SimpleBean");
   }

   protected SimpleBean collectionWithValueTypeOverride() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected SimpleBean collectionInjectOnObject() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected SimpleBean collectionNotACollection() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected void checkCollectionNotACollectionException(Exception exception)
   {
      checkThrowable(IllegalStateException.class, exception);
   }

   protected SimpleBean collectionIsInterface() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }

   protected void checkCollectionIsInterfaceException(Exception exception)
   {
      checkThrowable(IllegalStateException.class, exception);
   }
}