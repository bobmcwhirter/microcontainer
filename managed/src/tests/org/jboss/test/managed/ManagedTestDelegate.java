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
package org.jboss.test.managed;

import java.net.URL;

import org.jboss.aop.AspectXmlLoader;
import org.jboss.net.protocol.URLStreamHandlerFactory;
import org.jboss.test.AbstractTestDelegate;

/**
 * 
 * ManagedTestDelegate.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 58245 $
 */
public class ManagedTestDelegate extends AbstractTestDelegate
{
   /** The AOP URL used */
   private URL aopURL;
   
   /**
    * Create a new ManagedTestDelegate.
    * 
    * @param clazz the class
    * @throws Exception for any error
    */
   public ManagedTestDelegate(Class clazz) throws Exception
   {
      super(clazz);
   }

   public void setUp() throws Exception
   {
      super.setUp();
      if (deployAOP(clazz) == false)
         deployAOP(ManagedTest.class);
   }

   public void tearDown() throws Exception
   {
      super.tearDown();
      undeployAOP();
   }

   protected boolean deployAOP(Class referenceClass) throws Exception
   {
      String testName = referenceClass.getName();
      testName = testName.replace('.', '/') + "-aop.xml";
      URL url = clazz.getClassLoader().getResource(testName);
      if (url != null)
      {
         log.debug("Deploying " + url);
         aopURL = url;
         try
         {
            initURLHandlers();
            AspectXmlLoader.deployXML(aopURL);
         }
         catch (Throwable t)
         {
            throw new RuntimeException("Error deploying: " + url, t);
         }
         return true;
      }
      else
      {
         log.debug("No test specific deployment " + testName);
         return false;
      }
   }

   protected void undeployAOP()
   {
      if (aopURL == null)
         return;
      try
      {
         log.debug("Undeploying " + aopURL);
         AspectXmlLoader.undeployXML(aopURL);
      }
      catch (Exception e)
      {
         log.warn("Ignored error undeploying " + aopURL, e);
      }
   }

   private void initURLHandlers()
   {
      try
      {
         // Install a URLStreamHandlerFactory that uses the TCL
         URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory());
   
         // Preload JBoss URL handlers
         URLStreamHandlerFactory.preload();
      }
      catch (Error error)
      { //very naughty but we HAVE to do this or
         //we'll fail if we ever try to do this again
         log.warn("Caught Throwable Error, this probably means " +
            "we've already set the URLStreamHAndlerFactory before"); 
         //Sys.out because we don't have logging yet
      }

      // Include the default JBoss protocol handler package
      String handlerPkgs = System.getProperty("java.protocol.handler.pkgs");
      if (handlerPkgs != null)
      {
         handlerPkgs += "|org.jboss.net.protocol";
      }
      else
      {
         handlerPkgs = "org.jboss.net.protocol";
      }
      System.setProperty("java.protocol.handler.pkgs", handlerPkgs);
   }
}
