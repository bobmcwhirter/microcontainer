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
package org.jboss.kernel.plugins.annotations;

import java.lang.annotation.Annotation;

/**
 * Basic bean annotation handler.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BasicBeanAnnotationAdapter extends AbstractBeanAnnotationAdapter
{
   public BasicBeanAnnotationAdapter()
   {
      // -- adapters
      @SuppressWarnings("unchecked")
      Annotation2ValueMetaDataAdapter<? extends Annotation>[] adapters = new Annotation2ValueMetaDataAdapter[]{
         InjectAnnotationPlugin.INSTANCE,
         StringValueAnnotationPlugin.INSTANCE,
         ValueFactoryAnnotationPlugin.INSTANCE,
         ThisValueAnnotationPlugin.INSTANCE,
         NullValueAnnotationPlugin.INSTANCE,
         JavaBeanValueAnnotationPlugin.INSTANCE,
         CollectionValueAnnotationPlugin.INSTANCE,
         ListValueAnnotationPlugin.INSTANCE,
         SetValueAnnotationPlugin.INSTANCE,
         ArrayValueAnnotationPlugin.INSTANCE,
         MapValueAnnotationPlugin.INSTANCE,
      };
      // -- plugins
      // class
      addAnnotationPlugin(new AliasesAnnotationPlugin());
      addAnnotationPlugin(new DemandsAnnotationPlugin());
      addAnnotationPlugin(new DependsAnnotationPlugin());
      addAnnotationPlugin(new SupplysAnnotationPlugin());
      addAnnotationPlugin(new ClassFactoryAnnotationPlugin(adapters));
      addAnnotationPlugin(new ExternalInstallAnnotationPlugin());
      addAnnotationPlugin(new ExternalUninstallAnnotationPlugin());
/*
      addAnnotationPlugin(new InjectConstructorValueAnnotationPlugin());
      addAnnotationPlugin(new StringValueConstructorValueAnnotationPlugin());
      addAnnotationPlugin(new ValueFactoryConstructorValueAnnotationPlugin());
      addAnnotationPlugin(new CollectionConstructorValueAnnotationPlugin());
      addAnnotationPlugin(new ListConstructorValueAnnotationPlugin());
      addAnnotationPlugin(new SetConstructorValueAnnotationPlugin());
      addAnnotationPlugin(new ArrayConstructorValueAnnotationPlugin());
      addAnnotationPlugin(new MapConstructorValueAnnotationPlugin());
*/
      // constructor
      addAnnotationPlugin(new ConstructorParameterAnnotationPlugin(adapters));
      // property
      addAnnotationPlugin(InjectAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(StringValueAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(ValueFactoryAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(ThisValueAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(NullValueAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(JavaBeanValueAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(CollectionValueAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(ListValueAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(SetValueAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(ArrayValueAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(MapValueAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(new PropertyInstallCallbackAnnotationPlugin());
      addAnnotationPlugin(new PropertyUninstallCallbackAnnotationPlugin());
      // method
      addAnnotationPlugin(new FactoryMethodAnnotationPlugin(adapters));
      addAnnotationPlugin(new CreateLifecycleAnnotationPlugin(adapters));
      addAnnotationPlugin(new StartLifecycleAnnotationPlugin(adapters));
      addAnnotationPlugin(new StopLifecycleAnnotationPlugin(adapters));
      addAnnotationPlugin(new DestroyLifecycleAnnotationPlugin(adapters));
      addAnnotationPlugin(new MethodInstallCallbackAnnotationPlugin());
      addAnnotationPlugin(new MethodUninstallCallbackAnnotationPlugin());
      addAnnotationPlugin(new InstallMethodParameterAnnotationPlugin(adapters));
      addAnnotationPlugin(new UninstallMethodParameterAnnotationPlugin(adapters));
      // field
      addAnnotationPlugin(new InjectFieldAnnotationPlugin());
      addAnnotationPlugin(new ValueFactoryFieldAnnotationPlugin());
      addAnnotationPlugin(new InstallFieldCallbackAnnotationPlugin());
      addAnnotationPlugin(new UninstallFieldCallbackAnnotationPlugin());
   }
}
