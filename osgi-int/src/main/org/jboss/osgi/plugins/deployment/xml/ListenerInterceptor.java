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
package org.jboss.osgi.plugins.deployment.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;

import org.jboss.osgi.plugins.metadata.AbstractListenerMetaData;
import org.jboss.osgi.plugins.metadata.AbstractReferenceMetaData;
import org.jboss.osgi.spi.metadata.ListenerMetaData;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementInterceptor;

/**
 * Listener interceptor.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ListenerInterceptor extends DefaultElementInterceptor
{
   public static final ListenerInterceptor INTERCEPTOR = new ListenerInterceptor();

   public void add(Object parent, Object child, QName qName)
   {
      AbstractReferenceMetaData rmd = (AbstractReferenceMetaData) parent;
      AbstractListenerMetaData lmd = (AbstractListenerMetaData) child;
      List<ListenerMetaData> listeners = rmd.getListeners();
      if (listeners == null) {
         listeners = new ArrayList<ListenerMetaData>();
         rmd.setListeners(listeners);
      }
      listeners.add(lmd);
   }

}
