/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package org.osgi.service.obr;

import java.net.URL;
import java.util.Map;

import org.osgi.framework.Version;

/**
 * A resource is an abstraction of a downloadable thing, like a bundle.
 * 
 * Resources have capabilities and requirements. All a resource's requirements
 * must be satisfied before it can be installed.
 * 
 * Copyright (c) OSGi Alliance (2006). All Rights Reserved.
 * 
 * @version $Revision$
 */
public interface Resource
{
    final String LICENSE_URL = "license";

    final String DESCRIPTION = "description";

    final String DOCUMENTATION_URL = "documentation";

    final String COPYRIGHT = "copyright";

    final String SOURCE_URL = "source";

    final String SYMBOLIC_NAME = "symbolicname";

    final String PRESENTATION_NAME = "presentationname";

    final String ID = "id";

    final String VERSION = "version";

    final String URL = "url";

    final String SIZE = "size";

    final static String[] KEYS = { DESCRIPTION, SIZE, ID, LICENSE_URL,
            DOCUMENTATION_URL, COPYRIGHT, SOURCE_URL, PRESENTATION_NAME,
            SYMBOLIC_NAME, VERSION, URL };

    // get readable name

   /**
    * Get the properties.
    *
    * @return proipersties as map
    */
    Map getProperties();

   /**
    * Get the symbolic name.
    *
    * @return symbolic name
    */
    String getSymbolicName();

   /**
    * Get the presentation name.
    *
    * @return presentation name
    */
    String getPresentationName();

   /**
    * Get the version.
    *
    * @return version
    */
    Version getVersion();

   /**
    * Get the id.
    *
    * @return id
    */
    String getId();

   /**
    * Get the URL.
    *
    * @return url
    */
    URL getURL();

   /**
    * Get the requirement array.
    *
    * @return requirements
    */
    Requirement[] getRequirements();

   /**
    * Get the capabiliteis array.
    *
    * @return capabiliteis
    */
    Capability[] getCapabilities();

   /**
    * Get the categories.
    *
    * @return categories
    */
    String[] getCategories();

   /**
    * Get the repository.
    *
    * @return repository
    */
    Repository getRepository();
}