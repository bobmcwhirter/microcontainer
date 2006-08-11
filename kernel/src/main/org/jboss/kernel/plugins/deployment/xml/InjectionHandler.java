/*
 * 
 */

package org.jboss.kernel.plugins.deployment.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import org.jboss.beans.metadata.injection.InjectionMode;
import org.jboss.beans.metadata.injection.InjectionType;
import org.jboss.beans.metadata.plugins.AbstractInjectionValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * DependencyHandler.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 43106 $
 */
public class InjectionHandler extends DefaultElementHandler
{
   /** The handler */
   public static final InjectionHandler HANDLER = new InjectionHandler();

   public Object startElement(Object parent, QName name, ElementBinding element)
   {
      return new AbstractInjectionValueMetaData();
   }

   public void setParent(Object parent, Object o, QName qName, ElementBinding element, ElementBinding parentElement)
   {
      AbstractPropertyMetaData x = (AbstractPropertyMetaData) parent;
      AbstractInjectionValueMetaData child = (AbstractInjectionValueMetaData) o;
      child.setPropertyMetaData(x);
   }

   public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
   {
      AbstractInjectionValueMetaData injection = (AbstractInjectionValueMetaData) o;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         if ("bean".equals(localName))
            injection.setValue(attrs.getValue(i));
         else if ("property".equals(localName))
            injection.setProperty(attrs.getValue(i));
         else if ("state".equals(localName))
            injection.setDependentState(new ControllerState(attrs.getValue(i)));
         else if ("injectionMode".equals(localName))
            injection.setInjectionMode(new InjectionMode(localName));
         else if ("injectionType".equals(localName))
            injection.setInjectionType(new InjectionType(localName));
      }
   }

}
