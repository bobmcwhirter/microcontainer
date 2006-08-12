/*
 * 
 */

package org.jboss.beans.metadata.plugins;

import java.util.Set;

import org.jboss.beans.metadata.injection.InjectionMode;
import org.jboss.beans.metadata.injection.InjectionType;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossStringBuilder;

/**
 * Injection value.
 *
 * @author <a href="ales.justin@gmail.com">Ales Justin</a>
 */
public class AbstractInjectionValueMetaData extends AbstractDependencyValueMetaData
{
   protected InjectionMode injectionMode = InjectionMode.BY_TYPE;

   protected InjectionType injectionType = InjectionType.STRICT;

   protected AbstractPropertyMetaData propertyMetaData;

   /**
    * Create a new injection value
    */
   public AbstractInjectionValueMetaData()
   {
   }

   /**
    * Create a new injection value
    *
    * @param value the value
    */
   public AbstractInjectionValueMetaData(Object value)
   {
      super(value);
   }

   /**
    * Create a new injection value
    *
    * @param value    the value
    * @param property the property
    */
   public AbstractInjectionValueMetaData(Object value, String property)
   {
      super(value, property);
   }

   public InjectionMode getInjectionMode()
   {
      return injectionMode;
   }

   public void setInjectionMode(InjectionMode injectionMode)
   {
      this.injectionMode = injectionMode;
   }

   public InjectionType getInjectionType()
   {
      return injectionType;
   }

   public void setInjectionType(InjectionType injectionType)
   {
      this.injectionType = injectionType;
   }

   public AbstractPropertyMetaData getPropertyMetaData()
   {
      return propertyMetaData;
   }

   public void setPropertyMetaData(AbstractPropertyMetaData propertyMetaData)
   {
      this.propertyMetaData = propertyMetaData;
   }

   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      if (value == null)
      {
         // what else to use here - if not info.getType?
         Set beans = controller.getInstantiatedBeans(info.getType());
         int numberOfMatchingBeans = beans.size();
         if (numberOfMatchingBeans > 1)
         {
            throw new Error("Should not be here, too many matching beans - dependency failed! " + this);
         }
         else if (numberOfMatchingBeans == 0)
         {
            if (InjectionType.STRICT.equals(injectionType))
            {
               throw new Error("Should not be here, no bean matches class type - dependency failed! " + this);
            }
            return null;
         }
         return beans.iterator().next();
      }
      return super.getValue(info, cl);
   }

   public void visit(MetaDataVisitor visitor)
   {
      // determine value
      if (getUnderlyingValue() == null)
      {
         // check for property
         if (property != null)
         {
            property = null;
            log.warn("Ignoring property - contextual injection: " + this);
         }
         
         if (InjectionMode.BY_NAME.equals(injectionMode))
         {
            setValue(propertyMetaData.getName());
         }
         else if (InjectionMode.BY_TYPE.equals(injectionMode))
         {
            // set controller
            KernelControllerContext controllerContext = visitor.getControllerContext();
            controller = (KernelController) controllerContext.getController();
            visitor.visit(this); // as in AbstractValueMetaData
            // skip AbstractDependencyVMD.visit() - no value defined
            return;
         }
         else
         {
            throw new IllegalArgumentException("Unknown injection mode=" + injectionMode);
         }
      }
      super.visit(visitor);
   }

   public void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      if (injectionMode != null)
         buffer.append(" injectionMode=").append(injectionMode);
      if (injectionType != null)
         buffer.append(" injectionType=").append(injectionType);
   }
}
