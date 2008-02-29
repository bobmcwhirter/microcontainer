package org.jboss.test.bundle.support;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * 
 * A TestActivator.
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class TestActivator implements BundleActivator
{

   /**
    * Start Bundle
    * 
    * @param context BundleContext
    */
   public void start(BundleContext context) throws Exception
   {
      System.out.println("STARTING - " + context);
   }

   /**
    * Start Bundle
    * 
    * @param context BundleContext
    */
   public void stop(BundleContext context) throws Exception
   {
      System.out.println("STOPPING");
   }
}
