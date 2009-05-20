/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.aop.microcontainer.integration;

import org.jboss.aop.microcontainer.annotations.DisableAOP;
import org.jboss.aop.microcontainer.annotations.DisabledType;
import org.jboss.metadata.spi.MetaData;

/**
 * Diable AOP helper.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class DisableAOPHelper
{
   /**
    * Is AOP disabled for this metadata instance.
    *
    * @param metaData the metadata instance
    * @return true if AOP is disabled, false otherwise
    * @deprecated use the method with a constraint
    */
   @Deprecated
   public static boolean isAOPDisabled(MetaData metaData)
   {
      return isAOPDisabled(metaData, DisabledType.ALL);
   }

   /**
    * Is AOP disabled for this metadata instance.
    *
    * @param metaData the metadata instance
    * @param constraint the constraint
    * @return true if AOP is disabled, false otherwise
    */
   public static boolean isAOPDisabled(MetaData metaData, DisabledType constraint)
   {
      if (metaData != null)
      {
         DisableAOP aop = metaData.getAnnotation(DisableAOP.class);
         if (aop != null)
            return DisabledType.isDisabled(aop.value(), constraint);
      }
      return false;
   }
}
