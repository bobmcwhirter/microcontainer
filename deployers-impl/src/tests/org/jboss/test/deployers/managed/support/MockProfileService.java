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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedProperty;
import org.jboss.managed.api.annotation.ManagementObjectRef;
import org.jboss.managed.plugins.factory.AbstractManagedObjectFactory;

/**
 * Mock profile service for testing implementation details.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class MockProfileService
{
   private static final Logger log = Logger.getLogger(MockProfileService.class);

   /** id/type key to ManagedObject map */
   private Map<String, ManagedObject> moRegistry = new HashMap<String, ManagedObject>();

   private Map<String, List<ManagedProperty>> unresolvedRefs = new HashMap<String, List<ManagedProperty>>();

   /**
    * 
    * @param mo
    */
   public void processManagedObject(ManagedObject mo)
   {
      String key = mo.getName() + "/" + mo.getNameType();
      log.info("ID for ManagedObject: "+key+", attachmentName: "+mo.getAttachmentName());
      ManagedObject prevMO = moRegistry.put(key, mo);
      if( prevMO != null )
         log.warn("Duplicate mo for key: "+key+", prevMO: "+prevMO);

      // Check for unresolved refs
      List<ManagedProperty> referers =  unresolvedRefs.get(key);
      if (referers != null)
      {
         for(ManagedProperty prop : referers)
         {
            prop.setTargetManagedObject(mo);
         }
         referers.clear();
      }

      // Scan for @ManagementObjectRef
      for(ManagedProperty prop : mo.getProperties())
      {
         ManagementObjectRef ref = (ManagementObjectRef) prop.getAnnotations().get(ManagementObjectRef.class.getName());
         if ( ref != null )
         {
            String targetKey = ref.name() + "/" + ref.type();
            ManagedObject target = moRegistry.get(targetKey);
            if (target != null)
            {
               prop.setTargetManagedObject(target);
            }
            else
            {
               referers = (List<ManagedProperty>) unresolvedRefs.get(targetKey);
               if (referers == null)
               {
                  referers = new ArrayList<ManagedProperty>();
                  unresolvedRefs.put(targetKey, referers);
               }
               referers.add(prop);
            }
         }
         if (prop.getMetaType() == AbstractManagedObjectFactory.MANAGED_OBJECT_META_TYPE)
         {
            ManagedObject propMO = (ManagedObject) prop.getValue();
            processManagedObject(propMO);
         }
      }
   }

   public Map<String, List<ManagedProperty>> getUnresolvedRefs()
   {
      return unresolvedRefs;
   }
}
