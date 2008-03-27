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
package org.jboss.test.kernel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.plugins.registry.AbstractKernelRegistryEntry;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.util.NestedRuntimeException;

/**
 * An abstract kernel test.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractKernelTest extends AbstractTestCaseWithSetup
{
   private static DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.US);
   private Locale locale;

   public AbstractKernelTest(String name)
   {
      super(name);
   }

   protected void setUp() throws Exception
   {
      // set locales
      locale = Locale.getDefault();
      setLocale(Locale.US);

      super.setUp();
   }

   protected void setLocale(Locale locale)
   {
      SecurityManager sm = suspendSecurity();
      try
      {
         Locale.setDefault(locale);
      }
      finally
      {
         resumeSecurity(sm);
      }
   }

   protected void tearDown() throws Exception
   {
      super.tearDown();

      // reset locale
      setLocale(locale);
      locale = null;
   }

   protected Kernel bootstrap() throws Throwable
   {
      BasicBootstrap bootstrap = new BasicBootstrap();
      bootstrap.run();
      configureLoggingAfterBootstrap();
      return bootstrap.getKernel();
   }
   
   protected static void assertEqualsRegistryTarget(KernelRegistryEntry e1, KernelRegistryEntry e2)
   {
      assertEquals(e1.getTarget(), e2.getTarget());
   }

   protected static KernelRegistryEntry makeEntry(Object o)
   {
      return new AbstractKernelRegistryEntry(o);
   }

   protected Date createDate(String date)
   {
      Locale locale = Locale.getDefault();
      try
      {
         setLocale(Locale.US);
         return dateFormat.parse(date);
      }
      catch (Exception e)
      {
         throw new NestedRuntimeException(e);
      }
      finally
      {
         setLocale(locale);   
      }
   }

   protected Date createDate(int year, int month, int day)
   {
      Calendar calender = Calendar.getInstance();
      calender.clear();
      calender.set(year, month-1, day, 0, 0, 0);
      return calender.getTime();
   }

   protected void configureLoggingAfterBootstrap()
   {
   }
   
   /**
    * Default setup with security manager enabled
    * 
    * @param clazz the class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      AbstractTestDelegate delegate = new AbstractTestDelegate(clazz);
      delegate.enableSecurity = true;
      return delegate;
   }
}
