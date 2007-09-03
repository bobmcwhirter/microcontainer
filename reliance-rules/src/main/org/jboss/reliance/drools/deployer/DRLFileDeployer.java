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
package org.jboss.reliance.drools.deployer;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.InputStream;

import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderConfiguration;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.deployers.vfs.spi.deployer.AbstractVFSParsingDeployer;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.virtual.VirtualFile;
import org.jboss.reliance.drools.core.PackageWrapper;

/**
 * DRL file defined rules.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class DRLFileDeployer extends AbstractVFSParsingDeployer<BeanMetaData>
{
   private static final String BEANS = "-beans.";
   private static final String DSL = BEANS + ".dsl";

   private PackageBuilderConfiguration configuration;

   public DRLFileDeployer()
   {
      super(BeanMetaData.class);
      setSuffix(BEANS + getFileSuffix());
   }

   /**
    * Set the configuration.
    *
    * @param configuration the configuration
    */
   public void setConfiguration(PackageBuilderConfiguration configuration)
   {
      this.configuration = configuration;
   }

   /**
    * Get the file suffix.
    *
    * @return the file suffix
    */
   protected String getFileSuffix()
   {
      return "drl";
   }

   /**
    * Get the DRL reader.
    *
    * @param inputStream the input stream
    * @return reader
    */
   protected Reader getDRLReader(InputStream inputStream)
   {
      return new InputStreamReader(inputStream);
   }

   protected BeanMetaData parse(VFSDeploymentUnit unit, VirtualFile file, BeanMetaData root) throws Exception
   {
      String name = file.getName();
      // check for dsl - with <name>.dsl
      VirtualFile dslFile = unit.getMetaDataFile(name.replaceFirst(getSuffix(), DSL));
      PackageBuilder builder = new PackageBuilder(configuration);
      Reader drlReader = getDRLReader(file.openStream());
      try
      {
         if (dslFile != null)
         {
            Reader dslReader = new InputStreamReader(dslFile.openStream());
            try
            {
               builder.addPackageFromDrl(drlReader, dslReader);
            }
            finally
            {
               dslFile.closeStreams();
            }
         }
         else
         {
            builder.addPackageFromDrl(drlReader);
         }
      }
      finally
      {
         file.closeStreams();
      }
      BeanMetaDataBuilder beanBuilder = BeanMetaDataBuilderFactory.createBuilder(
            unit.getName() + "_PackageWrapper",
            PackageWrapper.class.getName()
      );
      beanBuilder.addCreateParameter(org.drools.rule.Package.class.getName(), builder.getPackage());
      return beanBuilder.getBeanMetaData();                                                           
   }
}
