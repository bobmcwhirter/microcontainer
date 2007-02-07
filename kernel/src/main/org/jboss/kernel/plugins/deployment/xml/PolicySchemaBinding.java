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
package org.jboss.kernel.plugins.deployment.xml;

import javax.xml.namespace.QName;

import org.jboss.xb.binding.sunday.unmarshalling.SchemaBinding;
import org.jboss.xb.binding.sunday.unmarshalling.TypeBinding;

/**
 * The policy schema binding.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class PolicySchemaBinding
{
   /** The namespace */
   public static final String POLICY_NS = "urn:jboss:policy:1.0";

   /** The policy binding */
   public static final QName policyTypeQName = new QName(POLICY_NS, "policyType");

   /** The scope binding */
   public static final QName scopeTypeQName = new QName(POLICY_NS, "scopeType");

   /** The scope element name */
   public static final QName scopeQName = new QName(POLICY_NS, "scope");

   /** The binding binding */
   public static final QName bindingTypeQName = new QName(POLICY_NS, "bindingType");

   /** The binding element name */
   public static final QName bindingQName = new QName(POLICY_NS, "binding");

   /** The annotation element name */
   public static final QName annotationQName = new QName(BeanSchemaBinding20.BEAN_DEPLOYER_NS, "annotation");

   /**
    * Initialize the schema binding
    *
    * @param schemaBinding the schema binding
    */
   public static void init(SchemaBinding schemaBinding)
   {
      // ignore XB property replacement
      schemaBinding.setReplacePropertyRefs(false);
      // init
      TypeBinding policyType = schemaBinding.getType(policyTypeQName);
      PolicySchemaBindingHelper.initPolicyHandlers(policyType);

      TypeBinding scopeType = schemaBinding.getType(scopeTypeQName);
      PolicySchemaBindingHelper.initScopeHandlers(scopeType);

      TypeBinding bindingsType = schemaBinding.getType(bindingTypeQName);
      PolicySchemaBindingHelper.initBindingHandlers(bindingsType);

      BeanSchemaBinding20.initArtifacts(schemaBinding);
   }

}
