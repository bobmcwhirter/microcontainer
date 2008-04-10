package org.jboss.test.kernel.deployment.support.container.plugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.metadata.api.model.AutowireType;
import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.CallbackMetaData;
import org.jboss.beans.metadata.spi.ClassLoaderMetaData;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.beans.metadata.spi.DemandMetaData;
import org.jboss.beans.metadata.spi.DependencyMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.beans.metadata.spi.LifecycleMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.SupplyMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ErrorHandlingMode;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelControllerContextAware;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.test.kernel.deployment.support.container.spi.ComponentBeanMetaDataFactory;
import org.jboss.test.kernel.deployment.support.container.spi.ComponentFactory;
import org.jboss.test.kernel.deployment.support.container.spi.ComponentInstance;
import org.jboss.test.kernel.deployment.support.container.spi.ComponentNameBuilder;
import org.jboss.test.kernel.deployment.support.container.spi.ComponentVisitor;
import org.jboss.util.JBossStringBuilder;

public class GenericComponentFactory<T>
   implements ComponentFactory<T>, ComponentNameBuilder, KernelControllerContextAware
{
   private ComponentBeanMetaDataFactory componentsFactory;
   private AtomicLong compID = new AtomicLong(0);
   private KernelControllerContext factoryContext;
   private ComponentVisitor visitor;

   public GenericComponentFactory(ComponentBeanMetaDataFactory factory, ComponentVisitor visitor)
   {
      this.componentsFactory = factory;
      this.visitor = visitor;
   }

   public void setKernelControllerContext(KernelControllerContext context)
         throws Exception
   {
      factoryContext = context;
   }
   public void unsetKernelControllerContext(KernelControllerContext context)
         throws Exception
   {
      factoryContext = null;
   }

   public ComponentInstance<T> createComponents(String baseName)
      throws Throwable
   {
      ArrayList<String> compNames = new ArrayList<String>();
      long nextID = compID.incrementAndGet();
      KernelController controller = (KernelController) factoryContext.getController();
      List<BeanMetaData> compBeans = componentsFactory.getBeans(baseName, nextID, this, visitor);
      T t = null;
      for(BeanMetaData bmd : compBeans)
      {
         String beanName = buildName(baseName, bmd.getName(), nextID);
         BeanMetaDataName nbmd = new BeanMetaDataName(beanName, bmd);
         KernelControllerContext kcc = controller.install(nbmd);
         if(t == null)
            t = (T) kcc.getTarget();
         compNames.add(beanName);
      }
      GenericComponentInstance<T> instance = new GenericComponentInstance<T>(t, compNames, nextID);
      return instance;
   }

   public void destroyComponents(ComponentInstance<T> instance) throws Exception
   {
      KernelController controller = (KernelController) factoryContext.getController();
      List<String> compBeans = instance.getComponentNames();
      for(String beanName : compBeans)
      {
         controller.uninstall(beanName);
      }
   }

   /**
    * Parses a name for the #compID suffix
    */
   public long getComponentID(String name) throws NumberFormatException
   {
      int poundSign = name.indexOf('#');
      if(poundSign < 0)
         throw new NumberFormatException(name+" has no #compID suffix");
      long id = Long.parseLong(name.substring(poundSign+1));
      return id;
   }

   public String buildName(String baseName, String compName, long compID)
   {
      String beanName = baseName + "@" + compName + "#" + compID;
      return beanName;
   }

   public ComponentBeanMetaDataFactory getFactory()
   {
      return componentsFactory;
   }

   public void validate()
   {
      KernelController controller = (KernelController) factoryContext.getController();
      Set<ControllerContext> notInstalled = controller.getNotInstalled();
      if(notInstalled.size() != 0)
         throw new IllegalStateException(notInstalled.toString());
   }

   static class BeanMetaDataName implements BeanMetaData
   {
      BeanMetaData bmd;
      String name;
      BeanMetaDataName(String name, BeanMetaData bmd)
      {
         this.name = name;
         this.bmd = bmd;
      }
      public Object clone()
      {
         return bmd.clone();
      }
      public void describeVisit(MetaDataVisitor vistor)
      {
         bmd.describeVisit(vistor);
      }
      public BeanAccessMode getAccessMode()
      {
         return bmd.getAccessMode();
      }
      public Set<Object> getAliases()
      {
         return bmd.getAliases();
      }
      public Set<AnnotationMetaData> getAnnotations()
      {
         return bmd.getAnnotations();
      }
      public AutowireType getAutowireType()
      {
         return bmd.getAutowireType();
      }
      public String getBean()
      {
         return bmd.getBean();
      }
      public Iterator<? extends MetaDataVisitorNode> getChildren()
      {
         return bmd.getChildren();
      }
      public ClassLoaderMetaData getClassLoader()
      {
         return bmd.getClassLoader();
      }
      public ConstructorMetaData getConstructor()
      {
         return bmd.getConstructor();
      }
      public LifecycleMetaData getCreate()
      {
         return bmd.getCreate();
      }
      public Set<DemandMetaData> getDemands()
      {
         return bmd.getDemands();
      }
      public Set<DependencyMetaData> getDepends()
      {
         return bmd.getDepends();
      }
      public String getDescription()
      {
         return bmd.getDescription();
      }
      public LifecycleMetaData getDestroy()
      {
         return bmd.getDestroy();
      }
      public ErrorHandlingMode getErrorHandlingMode()
      {
         return bmd.getErrorHandlingMode();
      }
      public List<CallbackMetaData> getInstallCallbacks()
      {
         return bmd.getInstallCallbacks();
      }
      public List<InstallMetaData> getInstalls()
      {
         return bmd.getInstalls();
      }
      public ControllerMode getMode()
      {
         return bmd.getMode();
      }
      public String getName()
      {
         return name;
      }
      public String getParent()
      {
         return bmd.getParent();
      }
      public Set<PropertyMetaData> getProperties()
      {
         return bmd.getProperties();
      }
      public LifecycleMetaData getStart()
      {
         return bmd.getStart();
      }
      public LifecycleMetaData getStop()
      {
         return bmd.getStop();
      }
      public Set<SupplyMetaData> getSupplies()
      {
         return bmd.getSupplies();
      }
      public Object getUnderlyingValue()
      {
         return bmd.getUnderlyingValue();
      }
      public List<CallbackMetaData> getUninstallCallbacks()
      {
         return bmd.getUninstallCallbacks();
      }
      public List<InstallMetaData> getUninstalls()
      {
         return bmd.getUninstalls();
      }
      public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
      {
         return bmd.getValue(info, cl);
      }
      public void initialVisit(MetaDataVisitor vistor)
      {
         bmd.initialVisit(vistor);
      }
      public boolean isAbstract()
      {
         return bmd.isAbstract();
      }
      public boolean isAutowireCandidate()
      {
         return bmd.isAutowireCandidate();
      }
      public void setAnnotations(Set<AnnotationMetaData> annotations)
      {
         bmd.setAnnotations(annotations);
      }
      public void setClassLoader(ClassLoaderMetaData classLoader)
      {
         bmd.setClassLoader(classLoader);
      }
      public void setMode(ControllerMode mode)
      {
         bmd.setMode(mode);
      }
      public void setName(String name)
      {
         bmd.setName(name);
      }
      public String toShortString()
      {
         return bmd.toShortString();
      }
      public void toShortString(JBossStringBuilder arg0)
      {
         bmd.toShortString(arg0);
      }
      
   }
}
