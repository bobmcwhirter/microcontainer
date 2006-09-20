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
package org.jboss.deployers.plugins.deployers.helpers;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.util.xml.JBossEntityResolver;
import org.jboss.virtual.VirtualFile;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * SchemaResolverDeployer.
 * 
 * @param <T> the expected type 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class JAXPDeployer<T> extends AbstractParsingDeployer<T>
{
   /** Use a namespace aware parser */
   private boolean useNamespaceAwareParser = true;

   /** A flag indicating if deployment descriptors should be validated */
   private boolean validateDTDs;
   
   /** The document builder factory */
   private DocumentBuilderFactory documentBuilderFactory;
   
   /**
    * Create a new JAXPDeployer.
    * 
    * @param deploymentType the deployment type
    * @throws IllegalArgumentException for a null deployment type
    */
   public JAXPDeployer(Class<T> deploymentType)
   {
      super(deploymentType);
   }

   /**
    * Get the useNamespaceAwareParser.
    * 
    * @return the useNamespaceAwareParser.
    */
   public boolean isUseNamespaceAwareParser()
   {
      return useNamespaceAwareParser;
   }

   /**
    * Set the useNamespaceAwareParser.
    * 
    * @param useNamespaceAwareParser the useNamespaceAwareParser.
    */
   public void setUseNamespaceAwareParser(boolean useNamespaceAwareParser)
   {
      this.useNamespaceAwareParser = useNamespaceAwareParser;
   }

   /**
    * Get the validateDTDs.
    * 
    * @return the validateDTDs.
    */
   public boolean isValidateDTDs()
   {
      return validateDTDs;
   }

   /**
    * Set the validateDTDs.
    * 
    * @param validateDTDs the validateDTDs.
    */
   public void setValidateDTDs(boolean validateDTDs)
   {
      this.validateDTDs = validateDTDs;
   }

   /**
    * Get the documentBuilderFactory.
    * 
    * @return the documentBuilderFactory.
    * @throws IllegalStateException if the create method has not been invoked
    */
   protected DocumentBuilderFactory getDocumentBuilderFactory()
   {
      if (documentBuilderFactory == null)
         throw new IllegalStateException("Document builder factory has not been constructed");
      return documentBuilderFactory;
   }

   /**
    * Create lifecycle
    * 
    * @throws Exception for any problem
    */
   public void create() throws Exception
   {
      documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware(useNamespaceAwareParser);
      documentBuilderFactory.setValidating(validateDTDs);
   }

   /**
    * Destroy lifecycle
    */
   public void destroy()
   {
      documentBuilderFactory = null;
   }
   
   /**
    * Parse a deployment
    * 
    * @param unit the deployment unit
    * @param file the metadata file
    * @return the metadata
    * @throws Exception for any error
    */
   protected T parse(DeploymentUnit unit, VirtualFile file) throws Exception
   {
      Document document = doParse(unit, file);
      return parse(unit, file, document);
   }
   
   /**
    * Do the parsing
    * 
    * @param unit the deployment unit
    * @param file the metadata file
    * @return the document
    * @throws Exception for any error
    */
   protected Document doParse(DeploymentUnit unit, VirtualFile file) throws Exception
   {
      if (file == null)
         throw new IllegalArgumentException("Null file");
      
      InputStream is = file.openStream();
      try
      {
         DocumentBuilder parser = getDocumentBuilderFactory().newDocumentBuilder();
         InputSource source = new InputSource(is);
         source.setSystemId(file.toURI().toString());
         parser.setEntityResolver(new JBossEntityResolver());
         return parser.parse(is);
      }
      finally
      {
         try
         {
            is.close();
         }
         catch (Exception ignored)
         {
         }
      }
   }

   /**
    * Parse a deployment
    * 
    * @param unit the deployment unit
    * @param file the metadata file
    * @param document the document
    * @return the metadata
    * @throws Exception for any error
    */
   protected abstract T parse(DeploymentUnit unit, VirtualFile file, Document document) throws Exception;
}
