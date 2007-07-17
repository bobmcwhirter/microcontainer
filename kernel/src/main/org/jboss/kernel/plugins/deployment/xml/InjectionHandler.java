/*
 * 
 */

package org.jboss.kernel.plugins.deployment.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import org.jboss.beans.metadata.plugins.AbstractInjectionValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.InjectionOption;
import org.jboss.beans.metadata.plugins.FromContext;
import org.jboss.beans.metadata.spi.AutowireType;
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
      AbstractInjectionValueMetaData vmd = new AbstractInjectionValueMetaData();
      if (parent instanceof AbstractPropertyMetaData)
      {
         AbstractPropertyMetaData x = (AbstractPropertyMetaData) parent;
         vmd.setPropertyMetaData(x);
      }
      return vmd;
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
         else if ("whenRequired".equals(localName))
            injection.setWhenRequiredState(new ControllerState(attrs.getValue(i)));
         else if ("type".equals(localName))
            injection.setInjectionType(AutowireType.getInstance(attrs.getValue(i)));
         else if ("option".equals(localName))
            injection.setInjectionOption(InjectionOption.getInstance(attrs.getValue(i)));
         else if ("fromContext".equals(localName))
            injection.setFromContext(FromContext.getInstance(attrs.getValue(i)));
      }
   }
}
