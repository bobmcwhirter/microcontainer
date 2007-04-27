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

/**
 * This service interface allows third parties to provide capabilities that are
 * present on the system but not encoded in the bundle's manifests. For example,
 * a capability provider could provide:
 * <ol>
 * <li>A Set of certificates</li>
 * <li>Dimensions of the screen</li>
 * <li>Amount of memory</li>
 * <li>...</li>
 * </ol>
 * 
 * Copyright (c) OSGi Alliance (2006). All Rights Reserved.
 * 
 * @version $Revision$
 */
public interface CapabilityProvider
{
    /**
     * Return a set of capabilities.
     * 
     * These capabilities are considered part of the platform. Bundles can
     * require these capabilities during selection. All capabilities from
     * different providers are considered part of the platform.
     * 
     * @return Set of capabilities
     */
    Capability[] getCapabilities();
}