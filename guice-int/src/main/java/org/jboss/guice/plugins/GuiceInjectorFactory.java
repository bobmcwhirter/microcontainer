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
package org.jboss.guice.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.SourceProviders;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * Guice Injector factory.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class GuiceInjectorFactory implements Injector
{
   static
   {
      SourceProviders.skip(GuiceInjectorFactory.class);
   }

   private Kernel kernel;
   private Injector injector;
   private Set<String> objects;

   private GuiceInjectorFactory(Kernel kernel, Injector injector, Set<String> objects)
   {
      this.kernel = kernel;
      this.injector = injector;
      this.objects = objects;
   }

   /**
    * Create injector.
    * Install all Guice objects.
    *
    * @param kernel the kernel
    * @param guiceObjects guice objects
    * @return injector instance
    */
   public static Injector createInjector(final Kernel kernel, final GuiceObject... guiceObjects)
   {
      if (kernel == null)
         throw new IllegalArgumentException("Null kernel.");

      final Set<String> objects = new HashSet<String>();
      Injector injector = Guice.createInjector(new AbstractModule()
      {
         protected void configure()
         {
            KernelController controller = kernel.getController();
            List<Throwable> errors = null;
            for(GuiceObject gObject : guiceObjects)
            {
               try
               {
                  String name = gObject.geName();
                  Object target = gObject.createObject(kernel, binder());
                  if (target == null)
                     throw new IllegalArgumentException("Null target.");

                  String beanClassName = target.getClass().getName();
                  AbstractBeanMetaData beanMetaData = new AbstractBeanMetaData(name, beanClassName);
                  KernelControllerContext context = controller.install(beanMetaData, target);
                  objects.add(name);
                  controller.change(context, ControllerState.INSTALLED);
               }
               catch (Throwable t)
               {
                  if (errors == null)
                     errors = new ArrayList<Throwable>();
                  errors.add(t);
               }
            }
            if (errors != null)
               throw new Error("Exception during Guice Objects installation: " + errors);
         }
      });
      return new GuiceInjectorFactory(kernel, injector, objects);
   }

   /**
    * Uninstall Guice objects.
    */
   public void destroy()
   {
      KernelController controller = kernel.getController();
      for(String name : objects)
      {
         if (controller.getInstalledContext(name) != null)
            controller.uninstall(name);
      }
      objects.clear();
      objects = null;
      kernel = null;
   }

   // injector delegate

   public void injectMembers(Object o)
   {
      injector.injectMembers(o);
   }

   public Map<Key<?>, Binding<?>> getBindings()
   {
      return injector.getBindings();
   }

   public <T> Binding<T> getBinding(Key<T> key)
   {
      return injector.getBinding(key);
   }

   public <T> List<Binding<T>> findBindingsByType(TypeLiteral<T> type)
   {
      return injector.findBindingsByType(type);
   }

   public <T> Provider<T> getProvider(Key<T> key)
   {
      return injector.getProvider(key);
   }

   public <T> Provider<T> getProvider(Class<T> type)
   {
      return injector.getProvider(type);
   }

   public <T> T getInstance(Key<T> key)
   {
      return injector.getInstance(key);
   }

   public <T> T getInstance(Class<T> type)
   {
      return injector.getInstance(type);
   }
}
