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

/**
 * Provides centralized access to the distributed repository.
 * 
 * A repository contains a set of <i>resources</i>. A resource contains a
 * number of fixed attributes (name, version, etc) and sets of:
 * <ol>
 * <li>Capabilities - Capabilities provide a named aspect: a bundle, a display,
 * memory, etc.</li>
 * <li>Requirements - A named filter expression. The filter must be satisfied
 * by one or more Capabilties with the given name. These capabilities can come
 * from other resources or from the platform. If multiple resources provide the
 * requested capability, one is selected. (### what algorithm? ###)</li>
 * <li>Requests - Requests are like requirements, except that a request can be
 * fullfilled by 0..n resources. This feature can be used to link to resources
 * that are compatible with the given resource and provide extra functionality.
 * For example, a bundle could request all its known fragments. The UI
 * associated with the repository could list these as optional downloads.</li>
 * 
 * Copyright (c) OSGi Alliance (2006). All Rights Reserved.
 * 
 * @version $Revision$
 */
public interface RepositoryAdmin
{
    /**
     * Discover any resources that match the given filter.
     * 
     * This is not a detailed search, but a first scan of applicable resources.
     * 
     * ### Checking the capabilities of the filters is not possible because that
     * requires a new construct in the filter.
     * 
     * The filter expression can assert any of the main headers of the resource.
     * The attributes that can be checked are:
     * 
     * <ol>
     * <li>name</li>
     * <li>version (uses filter matching rules)</li>
     * <li>description</li>
     * <li>category</li>
     * <li>copyright</li>
     * <li>license</li>
     * <li>source</li>
     * </ol>
     * 
     * @param filterExpr
     *            A standard OSGi filter
     * @return List of resources matching the filters.
     */
    Resource[] discoverResources(String filterExpr);

    /**
     * Create a resolver.
     * 
     * @return resolver
     */
    Resolver resolver();

    /**
     * Add a new repository to the federation.
     * 
     * The url must point to a repository XML file.
     * 
     * @param repository respository's url
     * @return repository
     * @throws Exception for any exception
     */
    Repository addRepository(URL repository) throws Exception;

   /**
    * Remove the repository under repository param.
    *
    * @param repository the respoitory url
    * @return true if removed, false otherwise
    */
    boolean removeRepository(URL repository);

    /**
     * List all the repositories.
     * 
     * @return repositories array
     */
    Repository[] listRepositories();

   /**
    * Get repository by id.
    *
    * @param respositoryId repository id
    * @return the respository by id
    */
    Resource getResource(String respositoryId);
}