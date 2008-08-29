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
package org.jboss.test.kernel.dependency.test;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.test.kernel.dependency.support.RegexpDemander;
import org.jboss.test.kernel.dependency.support.RegexpSupplier;
import org.jboss.test.kernel.dependency.support.IntervalDemander;
import org.jboss.test.kernel.dependency.support.IntervalSupplier;
import org.jboss.test.kernel.dependency.support.CustomDemander;
import org.jboss.test.kernel.dependency.support.CustomSupplier;
import org.jboss.dependency.spi.ControllerState;

/**
 * Test matcher demand/supply.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class MatcherDemandSupplyAnnotationTestCase extends MatcherDemandSupplyTestCase
{
   public MatcherDemandSupplyAnnotationTestCase(String name)
         throws Throwable
   {
      super(name, false);
   }

   public static Test suite()
   {
      return suite(MatcherDemandSupplyAnnotationTestCase.class);
   }

   protected ControllerState getState()
   {
      return ControllerState.DESCRIBED;
   }

   protected void setBeanMetaDatas() throws Throwable
   {
      BeanMetaDataBuilder b0 = BeanMetaDataBuilderFactory.createBuilder("regexpDemander", RegexpDemander.class.getName());
      BeanMetaDataBuilder b1 = BeanMetaDataBuilderFactory.createBuilder("regexpSupplier", RegexpSupplier.class.getName());
      BeanMetaDataBuilder b2 = BeanMetaDataBuilderFactory.createBuilder("intervalDemander", IntervalDemander.class.getName());
      BeanMetaDataBuilder b3 = BeanMetaDataBuilderFactory.createBuilder("intervalSupplier", IntervalSupplier.class.getName());
      BeanMetaDataBuilder b4 = BeanMetaDataBuilderFactory.createBuilder("customDemander", CustomDemander.class.getName());
      BeanMetaDataBuilder b5 = BeanMetaDataBuilderFactory.createBuilder("customSupplier", CustomSupplier.class.getName());

      setBeanMetaDatas(new BeanMetaData[]
            {
               b0.getBeanMetaData(),
               b1.getBeanMetaData(),
               b2.getBeanMetaData(),
               b3.getBeanMetaData(),
               b4.getBeanMetaData(),
               b5.getBeanMetaData(),
            }
      );
   }
}
