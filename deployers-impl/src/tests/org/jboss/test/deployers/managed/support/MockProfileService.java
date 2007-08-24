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
package org.jboss.test.deployers.managed.support;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.deployers.client.spi.DeployerClient;
import org.jboss.deployers.client.spi.Deployment;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.logging.Logger;
import org.jboss.managed.api.ComponentType;
import org.jboss.managed.api.ManagedDeployment;
import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedProperty;
import org.jboss.managed.api.ManagedDeployment.DeploymentPhase;
import org.jboss.managed.api.annotation.ManagementComponent;
import org.jboss.managed.api.annotation.ManagementObjectID;
import org.jboss.managed.api.annotation.ManagementObjectRef;
import org.jboss.managed.plugins.ManagedComponentImpl;
import org.jboss.managed.plugins.ManagedDeploymentImpl;
import org.jboss.managed.plugins.factory.AbstractManagedObjectFactory;
import org.jboss.metatype.api.types.ArrayMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.values.ArrayValue;
import org.jboss.metatype.api.values.GenericValue;
import org.jboss.metatype.api.values.SimpleValue;

/**
 * Mock profile service for testing implementation details.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class MockProfileService
{
   private static final Logger log = Logger.getLogger(MockProfileService.class);

   private DeployerClient main;
   /** id/type key to ManagedObject map */
   private Map<String, ManagedObject> moRegistry = new HashMap<String, ManagedObject>();
   private Map<String, Deployment> deployments = new HashMap<String, Deployment>();
   private Map<String, ManagedDeployment> managedDeployments = new HashMap<String, ManagedDeployment>();
   private Map<String, Set<ManagedProperty>> unresolvedRefs = new HashMap<String, Set<ManagedProperty>>();

   public MockProfileService(DeployerClient main)
   {
      this.main = main;
   }

   public void addDeployment(Deployment ctx)
      throws DeploymentException
   {
      main.addDeployment(ctx);
      deployments.put(ctx.getName(), ctx);
   }
   public void process()
      throws DeploymentException
   {
      main.process();
      for(String name : deployments.keySet())
      {
         ManagedDeployment md = main.getManagedDeployment(name);
         log.info(name+" ManagedDeployment: " + md);
         Map<String, ManagedObject> mos = md.getManagedObjects();
         log.info(name+" ManagedObjects: " + mos);
         for(ManagedObject mo : mos.values())
         {
            processManagedObject(mo, md);
         }
         managedDeployments.put(name, md);
      }
   }

   public ManagedObject getManagedObject(String name)
   {
      ManagedObject mo = moRegistry.get(name);
      return mo;
   }
   public ManagedDeployment getManagedDeployment(String name)
   {
      ManagedDeployment md = managedDeployments.get(name);
      return md;
   }

   /**
    * 
    * @param mo
    * @param md - 
    */
   protected void processManagedObject(ManagedObject mo, ManagedDeployment md)
   {
      String key = mo.getName() + "/" + mo.getNameType();
      log.debug("ID for ManagedObject: "+key+", attachmentName: "+mo.getAttachmentName());
      ManagedObject prevMO = moRegistry.put(key, mo);
      if( prevMO != null )
         log.warn("Duplicate mo for key: "+key+", prevMO: "+prevMO);
      // Check for unresolved refs
      checkForReferences(key, mo);

      // Create ManagedComponents for 
      ManagementComponent mc = (ManagementComponent) mo.getAnnotations().get(ManagementComponent.class.getName());
      if (mc != null)
      {
         ComponentType type = new ComponentType(mc.type(), mc.subtype());
         ManagedComponentImpl comp = new ManagedComponentImpl(type, md, mo);
         md.addComponent(mo.getName(), comp);
      }

      // Scan for @ManagementObjectRef
      for(ManagedProperty prop : mo.getProperties().values())
      {
         log.debug("Checking property: "+prop);
         // See if this is a ManagementObjectID
         ManagementObjectID id = (ManagementObjectID) prop.getAnnotations().get(ManagementObjectID.class.getName());
         if (id != null)
         {
            SimpleValue refValue = (SimpleValue) prop.getValue();
            String refName = (String) refValue.getValue();
            if (refName == null)
               refName = id.name();
            String propKey = refName + "/" + id.type();
            log.debug("ManagedProperty level ID for ManagedObject: "+propKey+", attachmentName: "+mo.getAttachmentName());
            moRegistry.put(propKey, mo);
            checkForReferences(propKey, mo);
         }

         // See if this is a ManagementObjectRef
         ManagementObjectRef ref = (ManagementObjectRef) prop.getAnnotations().get(ManagementObjectRef.class.getName());
         if ( ref != null )
         {
            // The reference key is the prop value + ref.type()
            log.debug("Property("+prop.getName()+") references: "+ref);
            SimpleValue refValue = (SimpleValue) prop.getValue();
            String refName = (String) refValue.getValue();
            if (refName == null)
               refName = ref.name();
            String targetKey = refName + "/" + ref.type();
            ManagedObject target = moRegistry.get(targetKey);
            if (target != null)
            {
               log.debug("Resolved property("+prop.getName()+") reference to: "+targetKey);
               prop.setTargetManagedObject(target);
            }
            else
            {
               Set<ManagedProperty> referers =  unresolvedRefs.get(targetKey);
               if (referers == null)
               {
                  referers = new HashSet<ManagedProperty>();
                  unresolvedRefs.put(targetKey, referers);
               }
               referers.add(prop);
            }
         }

         MetaType propType = prop.getMetaType();
         if (propType == AbstractManagedObjectFactory.MANAGED_OBJECT_META_TYPE)
         {
            GenericValue gv = (GenericValue) prop.getValue();
            ManagedObject propMO = (ManagedObject) gv.getValue();
            processManagedObject(propMO, md);
         }
         else if (propType.isArray())
         {
            ArrayMetaType amt = (ArrayMetaType) propType;
            MetaType etype = amt.getElementType();
            if (etype == AbstractManagedObjectFactory.MANAGED_OBJECT_META_TYPE)
            {
               ArrayValue<GenericValue> avalue = (ArrayValue<GenericValue>) prop.getValue();
               for(int n = 0; n < avalue.getLength(); n ++)
               {
                  GenericValue gv = (GenericValue) avalue.getValue(n);
                  ManagedObject propMO = (ManagedObject) gv.getValue();
                  processManagedObject(propMO, md);
               }
            }
         }
      }
   }

   public Map<String, Set<ManagedProperty>> getUnresolvedRefs()
   {
      return unresolvedRefs;
   }

   protected void checkForReferences(String key, ManagedObject mo)
   {
      Set<ManagedProperty> referers =  unresolvedRefs.get(key);
      log.debug("checkForReferences, "+key+" has referers: "+referers);
      if (referers != null)
      {
         for(ManagedProperty prop : referers)
         {
            prop.setTargetManagedObject(mo);
         }
         unresolvedRefs.remove(key);
      }      
   }
}
