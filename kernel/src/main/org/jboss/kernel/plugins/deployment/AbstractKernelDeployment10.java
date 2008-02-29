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
package org.jboss.kernel.plugins.deployment;

import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.jboss.managed.api.annotation.ManagementObject;
import org.jboss.managed.api.annotation.ManagementProperties;
import org.jboss.xb.annotations.JBossXmlSchema;

/**
 * An abstract kernel deployment.
 * 
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 69895 $
 */
@ManagementObject(properties = ManagementProperties.EXPLICIT) // TODO - explicitly add props we want to manage 
@JBossXmlSchema(namespace="urn:jboss:bean-deployer:1.0", elementFormDefault=XmlNsForm.QUALIFIED)
@XmlRootElement(name="deployment")
@XmlType(name="deploymentType", propOrder={"annotations", "classLoader", "beanFactories"})
public class AbstractKernelDeployment10 extends AbstractKernelDeployment
{
   private static final long serialVersionUID = 3l;

   /**
    * Create a new kernel deployment version 1
    */
   public AbstractKernelDeployment10()
   {
   }
}