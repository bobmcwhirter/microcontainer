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
 * Basic bean metadata annotation handler.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BasicBeanMetaDataAnnotationAdapter extends AbstractMetaDataAnnotationAdapter
{
   public static BasicBeanMetaDataAnnotationAdapter INSTANCE = new BasicBeanMetaDataAnnotationAdapter();

   protected BasicBeanMetaDataAnnotationAdapter()
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
      addAnnotationPlugin(AliasMetaDataAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(DemandsAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(DependsAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(SupplysAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(new ClassFactoryAnnotationPlugin(adapters));
      addAnnotationPlugin(ExternalInstallAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(ExternalUninstallAnnotationPlugin.INSTANCE);
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
      addAnnotationPlugin(PropertyInstallCallbackAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(PropertyUninstallCallbackAnnotationPlugin.INSTANCE);
      // method
      addAnnotationPlugin(new FactoryMethodAnnotationPlugin(adapters));
      addAnnotationPlugin(new CreateLifecycleAnnotationPlugin(adapters));
      addAnnotationPlugin(new StartLifecycleAnnotationPlugin(adapters));
      addAnnotationPlugin(new StopLifecycleAnnotationPlugin(adapters));
      addAnnotationPlugin(new DestroyLifecycleAnnotationPlugin(adapters));
      addAnnotationPlugin(MethodInstallCallbackAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(MethodUninstallCallbackAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(new InstallMethodParameterAnnotationPlugin(adapters));
      addAnnotationPlugin(new UninstallMethodParameterAnnotationPlugin(adapters));
      // field
      addAnnotationPlugin(InjectFieldAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(ValueFactoryFieldAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(InstallFieldCallbackAnnotationPlugin.INSTANCE);
      addAnnotationPlugin(UninstallFieldCallbackAnnotationPlugin.INSTANCE);
   }
}