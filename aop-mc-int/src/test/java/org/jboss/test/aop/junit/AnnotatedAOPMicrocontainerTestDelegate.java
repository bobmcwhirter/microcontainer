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
package org.jboss.test.aop.junit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.aop.AspectAnnotationLoader;
import org.jboss.aop.AspectManager;
import org.jboss.aop.microcontainer.beans.metadata.AspectManagerAwareBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.metadata.MicrocontainerAnnotationLoaderStrategy;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.kernel.junit.MicrocontainerTestDelegate;
import org.jboss.util.file.ArchiveBrowser;
import org.jboss.util.file.ClassFileFilter;
import org.jboss.util.file.Files;

/**
 * 
 * An AOPMicrocontainerTestDelegate.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 75380 $
 */
public class AnnotatedAOPMicrocontainerTestDelegate extends MicrocontainerTestDelegate
{
   private static final String BOOTSTRAP_FILE = "org/jboss/test/microcontainer/annotatedaop/test/BootstrapAspectManagerForAnnotationTests.xml";
   
   private File tempDir;
   
   private List<KernelControllerContext> deployedBeans;
   
   
   
   /**
    * Create a new AOPMicrocontainerTestDelegate.
    * 
    * @param clazz the class
    * @throws Exception for any error
    */
   public AnnotatedAOPMicrocontainerTestDelegate(Class<?> clazz) throws Exception
   {
      super(clazz);
   }

   public void setUp() throws Exception
   {
      super.setUpLogging();

      //Create a temporary directory with the classes listed in the file before we turn on security
      String testName = clazz.getName();
      testName = testName.replace('.', '/') + "-classes.txt";
      
      int slash = testName.lastIndexOf('/');
      slash = testName.lastIndexOf('/', slash - 1);
      String annotatedClassesFolder = testName.substring(0, slash + 1);
      
      URL url = clazz.getClassLoader().getResource(testName);
      if (url == null)
      {
         log.debug("No test specific file containing classes with annoations " + testName);
      }
      else
      {
         tempDir = getTemporaryDirectory();
         
         File file = new File(url.toURI());

         copyAnnotatedClassesToTempDir(annotatedClassesFolder, file);
      }
      
      super.setUp();
      log.debug("Security enabled: " + enableSecurity);
   }

   private File getTemporaryDirectory() throws IOException
   {
      File dir = File.createTempFile("annotated", "aop");
      if (dir.exists() && !dir.delete())
         throw new IOException("Could not delete temp file " + dir);
      if (!dir.mkdir())
         throw new IOException("Could not create temporary directory " + dir);
      dir.deleteOnExit();
      return dir;
   }
   
   private void copyAnnotatedClassesToTempDir(String annotatedClassesFolder, File inputFile) throws IOException,  URISyntaxException
   {
      log.debug("Copying annotated classes from  " + annotatedClassesFolder + " to " + tempDir);
      BufferedReader reader = new BufferedReader(new FileReader(inputFile));
      String clazz = reader.readLine();
      while (clazz != null)
      {
         clazz = clazz.trim();
         if (clazz.length() == 0)
         {
            continue;
         }
         log.debug("Copying " + clazz);
         String path = annotatedClassesFolder + clazz + ".class";
         URL url = getClass().getClassLoader().getResource(path);
         File src = new File(url.toURI());
         File dest = new File(tempDir + "/" + clazz + ".class");
         Files.copy(src, dest);
         clazz = reader.readLine();
      }
   }
   
   protected void deploy() throws Exception
   {
      URL url = getTestResource(BOOTSTRAP_FILE);
      deploy(url);
      
      if (tempDir != null)
         deployAOP();
      else
         log.debug("No test specific deployment tempDir");

      super.deploy();
   }

   protected void undeploy()
   {
      super.undeploy();
      if (tempDir != null)
         undeployAOP();
   }
   
   
   /**
    * Deploy the aop config
    *
    * @throws Exception for any error
    */
   @SuppressWarnings("unchecked")
   protected void deployAOP() throws Exception
   {
      log.debug("Deploying annotations from " + tempDir);
      MicrocontainerAnnotationLoaderStrategy strategy = new MicrocontainerAnnotationLoaderStrategy();
      AspectAnnotationLoader loader = new AspectAnnotationLoader(AspectManager.instance(), strategy);
      URL url = tempDir.toURL();
      Iterator<InputStream> it = ArchiveBrowser.getBrowser(url, new ClassFileFilter());
      loader.deployInputStreamIterator(it);
      List<AspectManagerAwareBeanMetaDataFactory> factories = strategy.getFactories();
      
      if (factories != null && factories.size() > 0)
      {
         deployedBeans = new ArrayList<KernelControllerContext>();
         for (AspectManagerAwareBeanMetaDataFactory factory : factories)
         {
            for (BeanMetaData bean : factory.getBeans())
            {
               deployedBeans.add(deploy(bean));
            }
         }
      }
   }

   /**
    * Undeploy the aop config
    */
   protected void undeployAOP()
   {
      if (deployedBeans != null && deployedBeans.size() > 0)
      {
         for (KernelControllerContext bean : deployedBeans)
         {
            undeploy(bean);
         }
      }
   }
}
