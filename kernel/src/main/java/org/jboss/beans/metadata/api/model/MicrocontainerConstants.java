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
package org.jboss.beans.metadata.api.model;

/**
 * Microcontainer constants.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface MicrocontainerConstants
{
   // Controller states
   // POJO
   String NOT_INSTALLED = "Not Installed";
   String PRE_INSTALL = "PreInstall";
   String DESCRIBED = "Described";
   String INSTANTIATED = "Instantiated";
   String CONFIGURED = "Configured";
   String CREATE = "Create";
   String START = "Start";
   String INSTALLED = "Installed";
   // Deployment stages
   String PARSE = "Parse";
   String POST_PARSE = "PostParse";
   String PRE_DESCRIBE = "PreDescribe";
   String CLASSLOADER = "ClassLoader";
   String POST_CLASSLOADER = "PostClassLoader";
   String PRE_REAL = "PreReal";
   String REAL = "Real";

   // Inject Option
   String STRICT = "Strict";
   String CALLBACK = "Callback";

   // Autowire
   String NONE = "None";
   String BY_CLASS = "ByClass";
   String BY_NAME = "ByName";
   String CONSTRUCTOR = "Constructor";
   String AUTO = "Auto";

   // FromContext
   String NAME = "name";
   String ALIASES = "aliases";
   String METADATA = "metadata";
   String BEANINFO = "beaninfo";
   String SCOPE = "scope";
   String STATE = "state";
   String ID = "id";
   String CONTEXT = "context";   
}
