/*
* JBoss, Home of Professional Open Source
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
package org.jboss.test.kernel.controller.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.ProtectionDomain;

import org.jboss.test.AbstractTestCaseWithSetup;

/**
 * SimpleScopedClassLoader.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class SimpleScopedClassLoader extends ClassLoader
{
   @Override
   protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
   {
      if (name.startsWith("org.jboss.test.kernel"))
      {
         SecurityManager sm = AbstractTestCaseWithSetup.suspendSecurity();
         try
         {
            String resourceName = name.replace('.', '/') + ".class";
            URL url = SimpleScopedClassLoader.class.getClassLoader().getResource(resourceName);
            InputStream is = null;
            byte[] bytes = null;
            try
            {
               is = url.openStream();
               ByteArrayOutputStream baos = new ByteArrayOutputStream();
               byte[] tmp = new byte[1024];
               int read = 0;
               while ( (read = is.read(tmp)) >= 0 )
                  baos.write(tmp, 0, read);
               bytes = baos.toByteArray();
            }
            catch (IOException e)
            {
               throw new RuntimeException("Unable to load class byte code " + name, e);
            }
            finally
            {
               try
               {
                  if (is != null)
                     is.close();
               }
               catch (IOException e)
               {
                  // pointless
               }
            }
            ProtectionDomain pd = AbstractTestCaseWithSetup.class.getProtectionDomain();
            return defineClass(name, bytes, 0, bytes.length, pd);
         }
         finally
         {
            AbstractTestCaseWithSetup.resumeSecurity(sm);
         }
      }
      return super.loadClass(name, resolve);
   }
}
