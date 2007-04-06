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
package org.jboss.deployers.plugins.metadata;

import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.jboss.deployers.spi.metadata.ManifestMetaData;

/**
 * Abstract manifest meta data.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractManifestMetaData implements ManifestMetaData, Externalizable
{
   private static final long serialVersionUID = 1L;

   protected Manifest manifest;

   public AbstractManifestMetaData()
   {
   }

   public AbstractManifestMetaData(Manifest manifest)
   {
      if (manifest == null)
         throw new IllegalArgumentException("Null manifest!");
      this.manifest = manifest;
   }

   protected Manifest getManifest()
   {
      if (manifest == null)
         throw new IllegalArgumentException("Null manifest!");
      return manifest;
   }

   protected String get(Attributes attributes, String name)
   {
      return attributes != null ? attributes.getValue(name) : null;
   }

   public String getMainAttribute(String name)
   {
      return get(getManifest().getMainAttributes(), name);
   }

   public String getAttribute(String attributesName, String name)
   {
      return get(getManifest().getAttributes(attributesName), name);
   }

   public String getEntry(String entryName, String name)
   {
      Map<String,Attributes> entries = getManifest().getEntries();
      if (entries != null)
      {
         return get(entries.get(entryName), name);
      }
      return null;
   }

   public void writeExternal(ObjectOutput out) throws IOException
   {
      OutputStream os = null; // todo
      getManifest().write(os);
   }

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
   {
      InputStream is = null; // todo
      manifest = new Manifest(is);
   }
}
