/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.deployers.plugins.structure.vfs.explicit;

import java.util.ArrayList;

import org.jboss.xb.binding.ObjectModelFactory;
import org.jboss.xb.binding.UnmarshallingContext;
import org.xml.sax.Attributes;

/**
 * An ObjectModelFactory for the jboss-structure.xml descriptor.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class StructureMetaDataObjectFactory implements ObjectModelFactory
{
   public StructureMetaData newRoot(Object root, UnmarshallingContext navigator,
         String namespaceURI, String localName, Attributes attrs)
   {
      StructureMetaData metaData = null;
      if (root != null)
         metaData = (StructureMetaData) root;
      else
         metaData = new StructureMetaData();

      return metaData;
   }

   public Object completeRoot(Object root, UnmarshallingContext ctx,
         String uri, String name)
   {
      return root;
   }

   public Object newChild(StructureMetaData parent, UnmarshallingContext navigator,
         String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;
      if(localName.equals("context"))
      {
         child = new ContextInfo();
      }
      return child;
   }
   public Object newChild(ContextInfo parent, UnmarshallingContext navigator,
         String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;
      if (localName.equals("classpath"))
         child = new ArrayList<ContextInfo.Path>();
      else if( localName.equals("path") )
      {
         String path = attrs.getValue("name");
         parent.setVfsPath(path);
      }
      else if( localName.equals("metaDataPath") )
      {
         String path = attrs.getValue("name");
         parent.setMetaDataPath(path);
      }
      return child;
   }
   public Object newChild(ArrayList<ContextInfo.Path> parent, UnmarshallingContext navigator,
         String namespaceURI, String localName, Attributes attrs)
   {
      Object child = null;
      if( localName.equals("path") )
      {
         String name = attrs.getValue("name");
         String suffixes = attrs.getValue("suffixes");
         ContextInfo.Path path = new ContextInfo.Path(name, suffixes);
         parent.add(path);
      }
      return child;
   }

   public void addChild(StructureMetaData parent, ContextInfo context,
         UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      parent.addContext(context);
   }
   public void addChild(ContextInfo context, ArrayList<ContextInfo.Path> classpath,
         UnmarshallingContext navigator, String namespaceURI, String localName)
   {
      context.setClassPath(classpath);
   }
}
