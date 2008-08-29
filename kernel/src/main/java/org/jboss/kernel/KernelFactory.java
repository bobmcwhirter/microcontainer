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
package org.jboss.kernel;

import org.jboss.kernel.spi.bootstrap.KernelInitializer;
import org.jboss.kernel.spi.config.KernelConfig;
import org.jboss.logging.Logger;

/**
 * A <code>KernelFactory</code> is an implementation of the Factory
 * design pattern that can construct new instances of {@link Kernel Kernel}
 * objects.
 *
 * <p>If no <code>KernelConfig</code> is given to the <code>KernelFactory</code>,
 * the <code>KernelFactory</code> will attempt to construct a
 * <code>KernelConfig</code> object by using System properties
 * (i.e. <code>System.getProperties()</code>).
 *
 * @author <a href="mailto:les.hazlewood@jboss.org">Les A. Hazlewood</a>
 * @version $Revision$
 */
public class KernelFactory
{
   /** The singleton factory */
   private static final KernelFactory singleton = new KernelFactory();

   /** The logger */
   protected final Logger log = Logger.getLogger(getClass());

   /**
    * No public construction
    */
   private KernelFactory()
   {
   }

   /**
    * Returns a new instance of a Kernel based on the specified
    * <code>KernelConfig</code> parameter.
    *
    * @param cfg the configuration used to construct a new
    *            <code>Kernel</code> object.
    * @return a new <code>Kernel</code> instance constructed according to the
    *         specified configuration.
    */
   public static Kernel newInstance(KernelConfig cfg)
   {
      return singleton.assembleNewKernel(cfg);
   }

   /**
    * Instantiates, configures, and initializes a new <code>Kernel</code>
    * instance according to the specified <code>KernelConfig</code> parameter.
    *
    * @param cfg the KernelConfig that will be used in constructing a new
    * Kernel.
    * @return a newly constructed Kernel object configured according to the
    * specified <code>KernelConfig</code> parameter.
    * @throws RuntimeException
    */
   protected Kernel assembleNewKernel(KernelConfig cfg)
      throws RuntimeException
   {
      long begin = 0;
      long end = 0;

      log.debug("Starting JBoss Kernel construction...");
      begin = System.currentTimeMillis();

      //local caching for easy reference:
      boolean trace = log.isTraceEnabled();

      if (trace)
         log.trace("Using KernelConfig: " + cfg);

      Kernel kernel = createKernel();
      if (trace)
         log.trace("Using Kernel: " + kernel);

      KernelInitializer initializer = createKernelInitializer(cfg);
      if (trace)
         log.trace("Using KernelInitializer: " + initializer);

      configureKernel(kernel, cfg);
      if (trace)
         log.trace("Configured kernel from KernelConfig");

      initializeKernel(kernel, initializer);
      if (trace)
         log.trace("Kernel instance initialzed");

      end = System.currentTimeMillis();
      log.debug("Completed JBoss Kernel construction.  Duration: " + (end - begin) + " milliseconds");

      return kernel;
   }

   /**
    * Constructs and returns a new, unconfigured, uninitialized
    * <code>Kernel</code> instance.  Configuration and initialization will be
    * done explicitly in the construction process via the
    * {@link #configureKernel(Kernel, KernelConfig) configureKernel} and
    * {@link #initializeKernel(Kernel, KernelInitializer) initializeKernel}
    * methods, respectively.
    *
    * @return An unconfigured, uninitialized <code>Kernel</code> instance.
    */
   protected Kernel createKernel()
   {
      return new Kernel();
   }

   /**
    * Constructs a <code>KernelInitializer</code> based on the
    * specified <code>KernelConfig</code> parameter.  This initializer
    * is used in the
    * {@link #initializeKernel(Kernel, KernelInitializer) initializeKernel}
    * method during Kernel construction.
    *
    * @param config the configuration with which to construct a new
    *               <code>KernelInitializer</code> instance.
    * @return a new <code>KernelInitializer</code> instance based on the
    *         specified <code>KernelConfig</code>.
    * @throws RuntimeException if the <code>config</code> object
    *                          cannot properly construct a new <code>KernelInitializer</code>
    */
   protected KernelInitializer createKernelInitializer(KernelConfig config)
   {
      try
      {
         return config.createKernelInitializer();
      }
      catch (Throwable t)
      {
         String msg = "Unable to create a KernelInitializer based on the " +
            "specified KernelConfig";
         throw new RuntimeException(msg, t);
      }
   }

   /**
    * Configures the specified Kernel according to the specified
    * <code>KernelConfig</code> parameter.
    * 
    * @param kernel a new, unconfigured <code>Kernel</code> instance.
    * @param cfg the <code>KernelConfig</code> to use to configure the
    * specified <code>Kernel</code> parameter.
    * @throws RuntimeException if the <code>KernelConfig</code> cannot be
    * updated with the Kernel object.
    */
   protected void configureKernel(Kernel kernel, KernelConfig cfg)
   {
      kernel.setConfig(cfg);
      try
      {
         cfg.setKernel(kernel);
      }
      catch (Throwable t)
      {
         String msg = "Unable to update the KernelConfig with the specified Kernel";
         throw new RuntimeException(msg, t);
      }
   }

   /**
    * Initializes the specified Kernel according to the specified
    * <code>KernelInitializer</code> parameter.
    * 
    * @param kernel a new, uninitialized <code>Kernel</code> instance.
    * @param initializer the <code>KernelInitializer</code> to use to
    * initialize the specified <code>Kernel</code> parameter.
    * @throws RuntimeException if the <code>KernelInitializer</code> cannot
    * initialize the given <code>Kernel</code> parameter.
    */
   protected void initializeKernel(Kernel kernel, KernelInitializer initializer)
   {
      try
      {
         initializer.initKernel(kernel);
      }
      catch (Throwable t)
      {
         String msg = "Unable to properly initialize the Kernel";
         throw new RuntimeException(msg, t);
      }
   }
}
