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
package org.jboss.test.managed.mock;

import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.managed.api.ManagedProperty;
import org.jboss.managed.plugins.ManagedObjectImpl;
import org.jboss.managed.plugins.ManagedPropertyImpl;
import org.jboss.util.xml.DOMWriter;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * MockDataSourceManagedObject.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MockDataSourceManagedObject extends ManagedObjectImpl
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;

   /** The document */
   private Document document;
   
   /**
    * Create a new MockDataSourceManagedObject.
    */
   public MockDataSourceManagedObject()
   {
      super(new HashSet<ManagedProperty>());
      Element element;
      try
      {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder = factory.newDocumentBuilder();
         DOMImplementation impl = builder.getDOMImplementation();
         document = impl.createDocument(null, null, null);
         
         element = document.createElement("data-source");
         document.appendChild(element);
      }
      catch (RuntimeException e)
      {
         throw e;
      }
      catch (Exception e)
      {
         throw new RuntimeException("Error creating dom", e);
      }
      
      Set<ManagedProperty> properties = getProperties();
      properties.add(new ManagedPropertyImpl(this, new MockDOMFields(element, "jndi-name")));
      properties.add(new ManagedPropertyImpl(this, new MockDOMFields(element, "connection-url")));
      properties.add(new ManagedPropertyImpl(this, new MockDOMFields(element, "user")));
      properties.add(new ManagedPropertyImpl(this, new MockDOMFields(element, "password")));
   }
   
   public String prettyPrint()
   {
      return DOMWriter.printNode(document, true);
   }
}
