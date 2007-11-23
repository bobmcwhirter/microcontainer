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
package org.jboss.aop.microcontainer.beans.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import org.jboss.aop.advice.AdviceType;
import org.jboss.aop.microcontainer.beans.AnnotationIntroduction;
import org.jboss.aop.microcontainer.beans.AnnotationOverride;
import org.jboss.aop.microcontainer.beans.CFlowStackEntry;
import org.jboss.aop.microcontainer.beans.DynamicCFlowDef;
import org.jboss.aop.microcontainer.beans.MixinEntry;
import org.jboss.aop.microcontainer.beans.NamedPointcut;
import org.jboss.aop.microcontainer.beans.Prepare;
import org.jboss.aop.microcontainer.beans.TypeDef;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.AdviceData;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.AspectBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.BeanMetaDataUtil;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.AspectManagerAwareBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.BaseInterceptorData;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.BindBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.CFlowStackBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.ConfigureLifecycleBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.CreateLifecycleBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.DescribeLifecycleBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.InstallLifecycleBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.InstantiateLifecycleBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.IntroductionBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.LifecycleBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.PreInstallLifecycleBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.PrecedenceBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.StackBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.StackRefData;
import org.jboss.aop.microcontainer.beans.beanmetadatafactory.StartLifecycleBeanMetaDataFactory;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.kernel.plugins.deployment.xml.BeanFactoryHandler;
import org.jboss.kernel.plugins.deployment.xml.BeanSchemaBinding20;
import org.jboss.kernel.plugins.deployment.xml.BeanSchemaBindingHelper;
import org.jboss.util.id.GUID;
import org.jboss.xb.binding.sunday.unmarshalling.CharactersHandler;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementInterceptor;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBinding;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBindingInitializer;
import org.jboss.xb.binding.sunday.unmarshalling.TypeBinding;
import org.xml.sax.Attributes;

/**
 * AOPBeansSchemaInitializer.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AOPBeansSchemaInitializer implements SchemaBindingInitializer
{
   private final static String MANAGER_BEAN_NAME = "manager-bean";
   private final static String MANAGER_PROPERTY_NAME = "manager-property";
   
   /** The namespace */
   private static final String AOP_BEANS_NS = "urn:jboss:aop-beans:1.0";

   /** The aspect or interceptor binding */
   private static final QName aspectOrInterceptorTypeQName = new QName(AOP_BEANS_NS, "aspectOrInterceptorType");

   /** The aspect or interceptor binding */
   private static final QName bindTypeQName = new QName(AOP_BEANS_NS, "bindType");
   
   private static final QName stackTypeQName = new QName(AOP_BEANS_NS, "stackType");

   private static final QName stackRefQName =  new QName(AOP_BEANS_NS, "stack-ref");
   
   private static final QName stackRefTypeQName = new QName(AOP_BEANS_NS, "stackRefType");
   
   private static final QName interceptorRefQName = new QName(AOP_BEANS_NS, "interceptor-ref");

   private static final QName interceptorRefTypeQName = new QName(AOP_BEANS_NS, "interceptorRefType");
   
   private static final QName adviceQName = new QName(AOP_BEANS_NS, "advice");

   private static final QName adviceTypeQName = new QName(AOP_BEANS_NS, "adviceType");
   
   private static final QName aroundQName = new QName(AOP_BEANS_NS, "around");

   private static final QName beforeQName = new QName(AOP_BEANS_NS, "before");

   private static final QName beforeTypeQName = new QName(AOP_BEANS_NS, "beforeType");

   private static final QName afterQName = new QName(AOP_BEANS_NS, "after");

   private static final QName afterTypeQName = new QName(AOP_BEANS_NS, "afterType");

   private static final QName throwingQName = new QName(AOP_BEANS_NS, "throwing");

   private static final QName throwingTypeQName = new QName(AOP_BEANS_NS, "throwingType");

   private static final QName finallyQName = new QName(AOP_BEANS_NS, "finally");

   private static final QName finallyTypeQName = new QName(AOP_BEANS_NS, "finallyType");

   private static final QName typedefTypeQName = new QName(AOP_BEANS_NS, "typedefType");
   
   private static final QName cflowStackTypeQName = new QName(AOP_BEANS_NS, "cflowStackType");

   private static final QName cflowStackEntryTypeQName = new QName(AOP_BEANS_NS, "cflowStackEntryType");
   
   private static final QName calledQName = new QName(AOP_BEANS_NS, "called");
   
   private static final QName notCalledQName = new QName(AOP_BEANS_NS, "not-called");
   
   private static final QName dynamicCflowStackTypeQName = new QName(AOP_BEANS_NS, "dynamicCflowStackType");
   
   private static final QName pointcutTypeQName = new QName(AOP_BEANS_NS, "pointcutType"); 
   
   private static final QName prepareTypeQName = new QName(AOP_BEANS_NS, "prepareType"); 
   
   private static final QName annotationTypeQName = new QName(AOP_BEANS_NS, "annotationType"); 
   
   private static final QName annotationIntroductionTypeQName = new QName(AOP_BEANS_NS, "annotationIntroductionType");
   
   private static final QName precedenceTypeQName = new QName(AOP_BEANS_NS, "precedenceType");
   
   private static final QName introductionTypeQName = new QName(AOP_BEANS_NS, "introductionType");

   private static final QName interfacesQName = new QName(AOP_BEANS_NS, "interfaces");

   private static final QName interfacesTypeQName = new QName(AOP_BEANS_NS, "interfacesType");
   
   private static final QName mixinQName = new QName(AOP_BEANS_NS, "mixin");

   private static final QName mixinTypeQName = new QName(AOP_BEANS_NS, "mixinType");
   
   private static final QName classQName = new QName(AOP_BEANS_NS, "class");

   private static final QName classTypeQName = new QName(AOP_BEANS_NS, "classType");
   
   private static final QName transientQName = new QName(AOP_BEANS_NS, "transient");

   private static final QName transientTypeQName = new QName(AOP_BEANS_NS, "transientType");
   
   private static final QName constructionQName = new QName(AOP_BEANS_NS, "construction");

   private static final QName constructionTypeQName = new QName(AOP_BEANS_NS, "constructionType");
   
//   private static final QName propertyQName = new QName(AOP_BEANS_NS, "property");
//
//   private static final QName attributeQName = new QName(AOP_BEANS_NS, "attribute");
   
   /** The lifecycle configure aspect binding */
   private static final QName lifecycleTypeQName = new QName(AOP_BEANS_NS, "lifecycleType");
   
   
   public SchemaBinding init(SchemaBinding schema)
   {
      // ignore XB property replacement
      schema.setReplacePropertyRefs(false);

      initTopLevelBindings(schema);
      initChildBindings(schema);
      
      

      // TODO FIXME???
      BeanSchemaBinding20.initArtifacts(schema);
      
      return schema;
   }
   
   private void initTopLevelBindings(SchemaBinding schema)
   {
      initAspectOrInterceptorType(schema);   // aspect and interceptor binding
      initBindType(schema); // bind binding
      initStackType(schema);
      initTypedefType(schema);
      initCFlowStackType(schema);
      initDynamicCFlowStackType(schema);
      initPrepareType(schema);
      initPointcutType(schema);
      initAnnotationIntroductionType(schema);
      initAnnotationType(schema);
      initPrecedenceType(schema);
      initIntroductionType(schema);
      initLifecycleType(schema);
   }

   private void initChildBindings(SchemaBinding schema)
   {
      //Children of bind
      initInterceptorRefType(schema); // interceptor-ref binding
      initStackRefType(schema); //stack-ref binding
      initAdviceType(schema); //advice binding
      initBeforeType(schema);
      initAfterType(schema);
      initThrowingType(schema);
      initFinallyType(schema);
      initCFlowStackEntryType(schema); //called/not-called within a cflow-stack
      initInterfacesType(schema);
      initMixinType(schema);
      initClassType(schema);
      initTransientType(schema);
      initConstructionType(schema);
   }
   
   private void initAspectOrInterceptorType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(aspectOrInterceptorTypeQName);
      BeanSchemaBindingHelper.initBeanFactoryHandlers(type);
      type.setHandler(new AspectBeanFactoryHandler());

//      type.pushInterceptor(propertyQName, BeanFactoryPropertyInterceptor.INTERCEPTOR);
//      type.pushInterceptor(attributeQName, BeanFactoryPropertyInterceptor.INTERCEPTOR);
   }
   
   private void initBindType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(bindTypeQName);
      type.pushInterceptor(stackRefQName, BindContentInterceptor.INTERCEPTOR);
      type.pushInterceptor(interceptorRefQName, BindContentInterceptor.INTERCEPTOR);
      type.pushInterceptor(adviceQName, BindContentInterceptor.INTERCEPTOR);
      type.pushInterceptor(aroundQName, BindContentInterceptor.INTERCEPTOR);
      type.pushInterceptor(beforeQName, BindContentInterceptor.INTERCEPTOR);
      type.pushInterceptor(afterQName, BindContentInterceptor.INTERCEPTOR);
      type.pushInterceptor(throwingQName, BindContentInterceptor.INTERCEPTOR);
      type.pushInterceptor(finallyQName, BindContentInterceptor.INTERCEPTOR);
      type.setHandler(new BindBeanFactoryHandler());
   }
   
   private void initStackType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(stackTypeQName);
      type.pushInterceptor(stackRefQName, StackContentInterceptor.INTERCEPTOR);
      type.pushInterceptor(interceptorRefQName, StackContentInterceptor.INTERCEPTOR);
      type.pushInterceptor(adviceQName, StackContentInterceptor.INTERCEPTOR);
      type.pushInterceptor(aroundQName, StackContentInterceptor.INTERCEPTOR);
      type.pushInterceptor(beforeQName, StackContentInterceptor.INTERCEPTOR);
      type.pushInterceptor(afterQName, StackContentInterceptor.INTERCEPTOR);
      type.pushInterceptor(throwingQName, StackContentInterceptor.INTERCEPTOR);
      type.pushInterceptor(finallyQName, StackContentInterceptor.INTERCEPTOR);
      type.setHandler(new StackBeanFactoryHandler());
   }
   
   private void initTypedefType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(typedefTypeQName);
      type.setHandler(new TypeDefHandler());
   }
   
   private void initCFlowStackType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(cflowStackTypeQName);
      type.pushInterceptor(calledQName, CFlowStackCalledInterceptor.INTERCEPTOR);
      type.pushInterceptor(notCalledQName, CFlowStackNotCalledInterceptor.INTERCEPTOR);
      type.setHandler(CFlowStackHandler.HANDLER);
   }

   private void initDynamicCFlowStackType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(dynamicCflowStackTypeQName);
      type.setHandler(DynamicCFlowHandler.HANDLER);
   }
   
   private void initPointcutType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(pointcutTypeQName);
      type.setHandler(PointcutHandler.HANDLER);
   }

   private void initPrepareType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(prepareTypeQName);
      type.setHandler(PrepareHandler.HANDLER);
   }

   private void initAnnotationIntroductionType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(annotationIntroductionTypeQName);
      type.setHandler(AnnotationIntroductionHandler.HANDLER);
      type.setSimpleType(AnnotationCharactersHandler.HANDLER);
   }

   private void initAnnotationType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(annotationTypeQName);
      type.setHandler(AnnotationOverrideHandler.HANDLER);
      type.setSimpleType(AnnotationCharactersHandler.HANDLER);
   }
   
   private void initPrecedenceType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(precedenceTypeQName);
      type.setHandler(PrecedenceHandler.HANDLER);
      type.pushInterceptor(adviceQName, PrecedenceInterceptor.INTERCEPTOR);
      type.pushInterceptor(interceptorRefQName, PrecedenceInterceptor.INTERCEPTOR);
   }

   private void initIntroductionType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(introductionTypeQName);
      type.pushInterceptor(interfacesQName, IntroductionInterfacesInterceptor.INTERCEPTOR);
      type.pushInterceptor(mixinQName, IntroductionMixinInterceptor.INTERCEPTOR);
      type.setHandler(IntroductionHandler.HANDLER);
   }
   
   private void initLifecycleType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(lifecycleTypeQName);
      BeanSchemaBindingHelper.initBeanFactoryHandlers(type);
      type.setHandler(new LifecycleBeanFactoryHandler());
   }
   
   private void initInterceptorRefType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(interceptorRefTypeQName);
      type.setHandler(InterceptorRefHandler.HANDLER);
   }
   
   private void initStackRefType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(stackRefTypeQName);
      type.setHandler(StackRefHandler.HANDLER);
   }
   
   private void initAdviceType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(adviceTypeQName);
      type.setHandler(AdviceHandler.HANDLER);
   }
   
   private void initBeforeType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(beforeTypeQName);
      type.setHandler(BeforeHandler.HANDLER);
   }

   private void initAfterType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(afterTypeQName);
      type.setHandler(AfterHandler.HANDLER);
   }

   private void initThrowingType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(throwingTypeQName);
      type.setHandler(ThrowingHandler.HANDLER);
   }

   private void initFinallyType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(finallyTypeQName);
      type.setHandler(FinallyHandler.HANDLER);
   }

   private void initCFlowStackEntryType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(cflowStackEntryTypeQName);
      type.setHandler(CFlowStackEntryHandler.HANDLER);
   }

   private void initInterfacesType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(interfacesTypeQName);
      type.setSimpleType(StringBufferCharacterHandler.HANDLER);
      type.setHandler(InterfacesHandler.HANDLER);
   }
   
   private void initMixinType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(mixinTypeQName);
      type.pushInterceptor(classQName, MixinClassInterceptor.INTERCEPTOR);
      type.pushInterceptor(transientQName, MixinTransientInterceptor.INTERCEPTOR);
      type.pushInterceptor(interfacesQName, MixinInterfacesInterceptor.INTERCEPTOR);
      type.pushInterceptor(constructionQName, MixinConstructionInterceptor.INTERCEPTOR);
      type.setHandler(MixinHandler.HANDLER);
   }
   
   private void initClassType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(classTypeQName);
      type.setHandler(StringBufferHandler.HANDLER);
      type.setSimpleType(StringBufferCharacterHandler.HANDLER);
   }
   
   private void initTransientType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(transientTypeQName);
      type.setHandler(StringBufferHandler.HANDLER);
      type.setSimpleType(StringBufferCharacterHandler.HANDLER);
   }
   
   private void initConstructionType(SchemaBinding schema)
   {
      TypeBinding type = schema.getType(constructionTypeQName);
      type.setHandler(StringBufferHandler.HANDLER);
      type.setSimpleType(StringBufferCharacterHandler.HANDLER);
   }
   
   
   ///////////////////////////////////////////////////////////////////////////////////////
   //BeanFactoryHandlers
   private static class AspectManagerAwareBeanFactoryHandler extends BeanFactoryHandler
   {
      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         super.attributes(o, elementName, element, attrs, nsCtx);

         AspectManagerAwareBeanMetaDataFactory factory = (AspectManagerAwareBeanMetaDataFactory) o;
         for (int i = 0; i < attrs.getLength(); ++i)
         {
            String localName = attrs.getLocalName(i);
            if (MANAGER_BEAN_NAME.equals(localName))
            {
               factory.setManagerBean(attrs.getValue(i));
            }
            else if (MANAGER_PROPERTY_NAME.equals(localName))
            {
               factory.setManagerProperty(attrs.getValue(i));
            }
         }
      }
   }
   
   private static class AspectBeanFactoryHandler extends AspectManagerAwareBeanFactoryHandler
   {
      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new AspectBeanMetaDataFactory();
      }
      
      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         super.attributes(o, elementName, element, attrs, nsCtx);

         AspectBeanMetaDataFactory factory = (AspectBeanMetaDataFactory) o;
         for (int i = 0; i < attrs.getLength(); ++i)
         {
            String localName = attrs.getLocalName(i);
            if ("scope".equals(localName))
            {
               factory.setScope(attrs.getValue(i));
            }
            else if ("factory".equals(localName))
            {
               factory.setFactory(attrs.getValue(i));
            }
         }
      }
   }

   private static class LifecycleBeanFactoryHandler extends AspectManagerAwareBeanFactoryHandler
   {
      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         String localname = name.getLocalPart();
         if (localname.equals("lifecycle-configure"))
         {
            return new ConfigureLifecycleBeanMetaDataFactory();
         }
         if (localname.equals("lifecycle-create"))
         {      
            return new CreateLifecycleBeanMetaDataFactory();
         }
         if (localname.equals("lifecycle-describe"))
         {      
            return new DescribeLifecycleBeanMetaDataFactory();
         }
         if (localname.equals("lifecycle-install"))
         {      
            return new InstallLifecycleBeanMetaDataFactory();
         }
         if (localname.equals("lifecycle-instantiate"))
         {      
            return new InstantiateLifecycleBeanMetaDataFactory();
         }
         if (localname.equals("lifecycle-preinstall"))
         {      
            return new PreInstallLifecycleBeanMetaDataFactory();
         }
         if (localname.equals("lifecycle-start"))
         {
            return new StartLifecycleBeanMetaDataFactory();
         }

         throw new IllegalStateException(name + " is not a recognized element");
      }

      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         super.attributes(o, elementName, element, attrs, nsCtx);

         LifecycleBeanMetaDataFactory factory = (LifecycleBeanMetaDataFactory) o;
         for (int i = 0; i < attrs.getLength(); ++i)
         {
            String localName = attrs.getLocalName(i);
            if ("classes".equals(localName))
            {
               factory.setClasses(attrs.getValue(i));
            }
            else if ("expr".equals(localName))
            {
               factory.setExpr(attrs.getValue(i));
            }         
            else if ("install".equals(localName))
            {
               factory.setInstallMethod(attrs.getValue(i));
            }
            else if ("uninstall".equals(localName))
            {
               factory.setUninstallMethod(attrs.getValue(i));
            }
         }
      }
   }

   private static class BindBeanFactoryHandler extends AspectManagerAwareBeanFactoryHandler
   {
      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new BindBeanMetaDataFactory();
      }
      
      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         super.attributes(o, elementName, element, attrs, nsCtx);

         BindBeanMetaDataFactory factory = (BindBeanMetaDataFactory) o;
         for (int i = 0; i < attrs.getLength(); ++i)
         {
            String localName = attrs.getLocalName(i);
            if ("pointcut".equals(localName))
            {
               factory.setPointcut(attrs.getValue(i));
            }
            else if ("cflow".equals(localName))
            {
               factory.setCflow(attrs.getValue(i));
            }
         }
      }

      @Override
      public Object endElement(Object o, QName name, ElementBinding element)
      {
         return super.endElement(o, name, element);
      }
   }
   
   private static class StackBeanFactoryHandler extends AspectManagerAwareBeanFactoryHandler
   {
      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new StackBeanMetaDataFactory();
      }
   }
   
   private static class CFlowStackHandler extends AspectManagerAwareBeanFactoryHandler
   {
      public static final CFlowStackHandler HANDLER = new CFlowStackHandler();

      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new CFlowStackBeanMetaDataFactory();
      }
   }

   private static class PrecedenceHandler extends AspectManagerAwareBeanFactoryHandler
   {
      public static final PrecedenceHandler HANDLER = new PrecedenceHandler();
      
      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new PrecedenceBeanMetaDataFactory();
      }
   }
   
   private static class IntroductionHandler extends AspectManagerAwareBeanFactoryHandler
   {
      public static final IntroductionHandler HANDLER = new IntroductionHandler();
      
      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new IntroductionBeanMetaDataFactory();
      }
      
      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         super.attributes(o, elementName, element, attrs, nsCtx);

         IntroductionBeanMetaDataFactory factory = (IntroductionBeanMetaDataFactory) o;
         for (int i = 0; i < attrs.getLength(); ++i)
         {
            String localName = attrs.getLocalName(i);
            if ("expr".equals(localName))
            {
               factory.setExpr(attrs.getValue(i));
            }
            else if ("class".equals(localName))
            {
               factory.setClazz(attrs.getValue(i));
            }
         }
      }
   }
   
   
   ///////////////////////////////////////////////////////////////////////////////////////
   //DefaultElementHandlers

   private static class InterceptorRefHandler  extends DefaultElementHandler
   {
      public static final InterceptorRefHandler HANDLER = new InterceptorRefHandler();

      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new AdviceData();
      }

      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         BaseInterceptorData interceptorRef = (BaseInterceptorData)o;
         for (int i = 0; i < attrs.getLength(); ++i)
         {
            String localName = attrs.getLocalName(i);
            if ("name".equals(localName))
            {
               interceptorRef.setRefName(attrs.getValue(i));
            }
         }
      }
   }

   private static class StackRefHandler  extends DefaultElementHandler
   {
      public static final StackRefHandler HANDLER = new StackRefHandler();

      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new StackRefData();
      }

      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         BaseInterceptorData interceptorRef = (BaseInterceptorData)o;
         for (int i = 0; i < attrs.getLength(); ++i)
         {
            String localName = attrs.getLocalName(i);
            if ("name".equals(localName))
            {
               interceptorRef.setRefName(attrs.getValue(i));
            }
         }
      }
   }
      
   private static class AdviceHandler  extends DefaultElementHandler
   {
      public static final AdviceHandler HANDLER = new AdviceHandler();

      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new AdviceData();
      }

      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         super.attributes(o, elementName, element, attrs, nsCtx);
         AdviceData advice = (AdviceData)o;
         for (int i = 0; i < attrs.getLength(); ++i)
         {
            String localName = attrs.getLocalName(i);
            if ("aspect".equals(localName))
            {
               advice.setRefName(attrs.getValue(i));
            }
            else if ("name".equals(localName))
            {
               advice.setAdviceMethod(attrs.getValue(i));
            }
         }
      }
   }
      
   private static class BeforeHandler extends AdviceHandler
   {
      public static final BeforeHandler HANDLER = new BeforeHandler();

      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         super.attributes(o, elementName, element, attrs, nsCtx);
         AdviceData advice = (AdviceData)o;
         advice.setType(AdviceType.BEFORE);
      }
   }
      
   private static class AfterHandler extends AdviceHandler
   {
      public static final AfterHandler HANDLER = new AfterHandler();

      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         super.attributes(o, elementName, element, attrs, nsCtx);
         AdviceData advice = (AdviceData)o;
         advice.setType(AdviceType.AFTER);
      }
   }

   private static class ThrowingHandler extends AdviceHandler
   {
      public static final ThrowingHandler HANDLER = new ThrowingHandler();

      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         super.attributes(o, elementName, element, attrs, nsCtx);
         AdviceData advice = (AdviceData)o;
         advice.setType(AdviceType.THROWING);
      }
   }
      
   private static class FinallyHandler extends AdviceHandler
   {
      public static final FinallyHandler HANDLER = new FinallyHandler();

      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         super.attributes(o, elementName, element, attrs, nsCtx);
         AdviceData advice = (AdviceData)o;
         advice.setType(AdviceType.FINALLY);
      }
   }
      
   private static class TypeDefHandler  extends DefaultElementHandler
   {
      public static final TypeDefHandler HANDLER = new TypeDefHandler();

      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new AbstractBeanMetaData(TypeDef.class.getName());
      }

      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         BeanMetaDataUtil util = new BeanMetaDataUtil();
         AbstractBeanMetaData typedef = (AbstractBeanMetaData)o;

         for (int i = 0; i < attrs.getLength(); ++i)
         {
            String localName = attrs.getLocalName(i);
            if ("name".equals(localName))
            {
               typedef.setName(attrs.getValue(i));
               BeanMetaDataUtil.setSimpleProperty(typedef, "name", attrs.getValue(i));
            }
            else if ("expr".equals(localName))
            {
               BeanMetaDataUtil.setSimpleProperty(typedef, "expr", attrs.getValue(i));
            }
            else if (MANAGER_BEAN_NAME.equals(localName))
            {
               util.setManagerBean(attrs.getValue(i));
            }
            else if (MANAGER_PROPERTY_NAME.equals(localName))
            {
               util.setManagerProperty(attrs.getValue(i));
            }
         }
         util.setAspectManagerProperty(typedef, "manager");
      }
   }
      
   private static class CFlowStackEntryHandler extends DefaultElementHandler
   {
      public static final CFlowStackEntryHandler HANDLER = new CFlowStackEntryHandler();

      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new AbstractBeanMetaData(CFlowStackEntry.class.getName());
      }

      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         AbstractBeanMetaData entry = (AbstractBeanMetaData)o;
         for (int i = 0; i < attrs.getLength(); ++i)
         {
            String localName = attrs.getLocalName(i);
            if ("expr".equals(localName))
            {
               BeanMetaDataUtil.setSimpleProperty(entry, "expr", attrs.getValue(i));
            }
         }
      }
   }
   
   private static class DynamicCFlowHandler extends DefaultElementHandler
   {
      public static final DynamicCFlowHandler HANDLER = new DynamicCFlowHandler();

      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new AbstractBeanMetaData(DynamicCFlowDef.class.getName());
      }

      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         BeanMetaDataUtil util = new BeanMetaDataUtil();
         AbstractBeanMetaData dynamicCFlow = (AbstractBeanMetaData)o;

         for (int i = 0; i < attrs.getLength(); ++i)
         {
            String localName = attrs.getLocalName(i);
            if ("name".equals(localName))
            {
               dynamicCFlow.setName(attrs.getValue(i));
               BeanMetaDataUtil.setSimpleProperty(dynamicCFlow, "name", attrs.getValue(i));
            }
            else if ("class".equals(localName))
            {
               BeanMetaDataUtil.setSimpleProperty(dynamicCFlow, "className", attrs.getValue(i));
            }
            else if (MANAGER_BEAN_NAME.equals(localName))
            {
               util.setManagerBean(attrs.getValue(i));
            }
            else if (MANAGER_PROPERTY_NAME.equals(localName))
            {
               util.setManagerProperty(attrs.getValue(i));
            }
         }
         util.setAspectManagerProperty(dynamicCFlow, "manager");
      }
   }
   
   private static class PrepareHandler extends DefaultElementHandler
   {
      public static final PrepareHandler HANDLER = new PrepareHandler();

      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new AbstractBeanMetaData(Prepare.class.getName());
      }

      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         BeanMetaDataUtil util = new BeanMetaDataUtil();
         AbstractBeanMetaData pointcut = (AbstractBeanMetaData)o;

         for (int i = 0; i < attrs.getLength(); ++i)
         {
            String localName = attrs.getLocalName(i);
            if ("name".equals(localName))
            {
               pointcut.setName(attrs.getValue(i));
               BeanMetaDataUtil.setSimpleProperty(pointcut, "name", attrs.getValue(i));
            }
            else if ("expr".equals(localName))
            {
               BeanMetaDataUtil.setSimpleProperty(pointcut, "expr", attrs.getValue(i));
            }
            else if (MANAGER_BEAN_NAME.equals(localName))
            {
               util.setManagerBean(attrs.getValue(i));
            }
            else if (MANAGER_PROPERTY_NAME.equals(localName))
            {
               util.setManagerProperty(attrs.getValue(i));
            }
         }
         util.setAspectManagerProperty(pointcut, "manager");
      }
   }
   
   private static class PointcutHandler extends PrepareHandler
   {
      public static final PointcutHandler HANDLER = new PointcutHandler();

      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new AbstractBeanMetaData(NamedPointcut.class.getName());
      }
   }

   private static class AnnotationIntroductionHandler extends DefaultElementHandler
   {
      public static final AnnotationIntroductionHandler HANDLER = new AnnotationIntroductionHandler(); 

      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new AbstractBeanMetaData(AnnotationIntroduction.class.getName());
      }
      
      @Override
      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         BeanMetaDataUtil util = new BeanMetaDataUtil();
         AbstractBeanMetaData annotation = (AbstractBeanMetaData)o;

         for (int i = 0; i < attrs.getLength(); ++i)
         {
            String localName = attrs.getLocalName(i);
            if ("invisible".equals(localName))
            {
               BeanMetaDataUtil.setSimpleProperty(annotation, "invisible", attrs.getValue(i));
            }
            else if ("expr".equals(localName))
            {
               BeanMetaDataUtil.setSimpleProperty(annotation, "expr", attrs.getValue(i));
            }
            else if (MANAGER_BEAN_NAME.equals(localName))
            {
               util.setManagerBean(attrs.getValue(i));
            }
            else if (MANAGER_PROPERTY_NAME.equals(localName))
            {
               util.setManagerProperty(attrs.getValue(i));
            }
         }
         util.setAspectManagerProperty(annotation, "manager");
         annotation.setName(GUID.asString());
      }

      @Override
      public Object endElement(Object o, QName name, ElementBinding element)
      {
         return super.endElement(o, name, element);
      }
      
      
   }
   
   private static class AnnotationOverrideHandler extends AnnotationIntroductionHandler
   {
      public static final AnnotationOverrideHandler HANDLER = new AnnotationOverrideHandler(); 

      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new AbstractBeanMetaData(AnnotationOverride.class.getName());
      }
      
   }
   
   private static class InterfacesHandler extends DefaultElementHandler
   {
      public static final InterfacesHandler HANDLER = new InterfacesHandler();

      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new StringBuffer();
      }
   }
      
   private static class MixinHandler extends DefaultElementHandler
   {
      public static final MixinHandler HANDLER = new MixinHandler();

      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new AbstractBeanMetaData(MixinEntry.class.getName());
      }
   }
      
   private static class StringBufferHandler extends DefaultElementHandler
   {
      public static final StringBufferHandler HANDLER = new StringBufferHandler();

      @Override
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new StringBuffer();
      }
   }
      
   ///////////////////////////////////////////////////////////////////////////////////////
   //Interceptors
   
   private static class BindContentInterceptor extends DefaultElementInterceptor
   {
      public static final BindContentInterceptor INTERCEPTOR = new BindContentInterceptor();
      
      @Override
      public void add (Object parent, Object child, QName name)
      {
         BindBeanMetaDataFactory factory = (BindBeanMetaDataFactory) parent;
         BaseInterceptorData interceptorData = (BaseInterceptorData)child;
         factory.addInterceptor(interceptorData);
      }
   }
   
   private static class StackContentInterceptor extends DefaultElementInterceptor
   {
      public static final StackContentInterceptor INTERCEPTOR = new StackContentInterceptor();
      
      @Override
      public void add (Object parent, Object child, QName name)
      {
         StackBeanMetaDataFactory factory = (StackBeanMetaDataFactory) parent;
         BaseInterceptorData interceptorData = (BaseInterceptorData)child;
         factory.addInterceptor(interceptorData);
      }
   }
   
   private static class CFlowStackInterceptor extends DefaultElementInterceptor
   {
      public void addEntry(Object parent, Object child, QName name, boolean called)
      {
         CFlowStackBeanMetaDataFactory cflowStack = (CFlowStackBeanMetaDataFactory)parent;
         AbstractBeanMetaData entry = (AbstractBeanMetaData)child;
         BeanMetaDataUtil.setSimpleProperty(entry, "called", called);
         cflowStack.addEntry(entry);
      }
   }
   
   private static class CFlowStackNotCalledInterceptor extends CFlowStackInterceptor
   {
      public static final CFlowStackNotCalledInterceptor INTERCEPTOR = new CFlowStackNotCalledInterceptor();

      @Override
      public void add (Object parent, Object child, QName name)
      {
         super.addEntry(parent, child, name, false);
      }
   }
   
   private static class CFlowStackCalledInterceptor extends CFlowStackInterceptor
   {
      public static final CFlowStackCalledInterceptor INTERCEPTOR = new CFlowStackCalledInterceptor();

      @Override
      public void add (Object parent, Object child, QName name)
      {
         super.addEntry(parent, child, name, true);
      }
   }

   private static class PrecedenceInterceptor extends DefaultElementInterceptor
   {
      public static final PrecedenceInterceptor INTERCEPTOR = new PrecedenceInterceptor();

      @Override
      public void add(Object parent, Object child, QName name)
      {
         PrecedenceBeanMetaDataFactory precedence = (PrecedenceBeanMetaDataFactory)parent;
         BaseInterceptorData interceptorData = (BaseInterceptorData)child;
         precedence.addEntry(interceptorData);
      }      
   }
   
   private static class IntroductionInterfacesInterceptor extends DefaultElementInterceptor
   {
      public static final IntroductionInterfacesInterceptor INTERCEPTOR = new IntroductionInterfacesInterceptor();

      @Override
      public void add(Object parent, Object child, QName name)
      {
         IntroductionBeanMetaDataFactory intro = (IntroductionBeanMetaDataFactory)parent;
         intro.setInterfaces(((StringBuffer)child).toString());
      }      
   }
   
   private static class IntroductionMixinInterceptor extends DefaultElementInterceptor
   {
      public static final IntroductionMixinInterceptor INTERCEPTOR = new IntroductionMixinInterceptor();

      @Override
      public void add(Object parent, Object child, QName name)
      {
         IntroductionBeanMetaDataFactory intro = (IntroductionBeanMetaDataFactory)parent;
         intro.addMixinEntry((AbstractBeanMetaData)child);
      }      
   }
   
   private static class MixinClassInterceptor extends DefaultElementInterceptor
   {
      public static final MixinClassInterceptor INTERCEPTOR = new MixinClassInterceptor();

      @Override
      public void add(Object parent, Object child, QName name)
      {
         AbstractBeanMetaData mixin = (AbstractBeanMetaData)parent;
         BeanMetaDataUtil.setSimpleProperty(mixin, "mixin", ((StringBuffer)child).toString());
      }      
   }
   
   private static class MixinTransientInterceptor extends DefaultElementInterceptor
   {
      public static final MixinTransientInterceptor INTERCEPTOR = new MixinTransientInterceptor();

      @Override
      public void add(Object parent, Object child, QName name)
      {
         AbstractBeanMetaData mixin = (AbstractBeanMetaData)parent;
         BeanMetaDataUtil.setSimpleProperty(mixin, "transient", ((StringBuffer)child).toString());
      }      
   }
   
   private static class MixinConstructionInterceptor extends DefaultElementInterceptor
   {
      public static final MixinConstructionInterceptor INTERCEPTOR = new MixinConstructionInterceptor();

      @Override
      public void add(Object parent, Object child, QName name)
      {
         AbstractBeanMetaData mixin = (AbstractBeanMetaData)parent;
         BeanMetaDataUtil.setSimpleProperty(mixin, "construction", ((StringBuffer)child).toString());
      }      
   }
   
   private static class MixinInterfacesInterceptor extends DefaultElementInterceptor
   {
      public static final MixinInterfacesInterceptor INTERCEPTOR = new MixinInterfacesInterceptor();

      @Override
      public void add(Object parent, Object child, QName name)
      {
         AbstractBeanMetaData mixin = (AbstractBeanMetaData)parent;
         IntroductionBeanMetaDataFactory.addInterfaces(mixin, "interfaces", ((StringBuffer)child).toString());
      }      
   }
   
   ///////////////////////////////////////////////////////////////////////////////////////
   //Characters Handlers
   private static class AnnotationCharactersHandler extends CharactersHandler
   {
      public static final AnnotationCharactersHandler HANDLER = new AnnotationCharactersHandler(); 
      
      @Override
      public Object unmarshal(QName qName, TypeBinding typeBinding, NamespaceContext nsCtx, org.jboss.xb.binding.metadata.ValueMetaData valueMetaData, String value)
      {
         return value;
      }

      @Override
      public void setValue(QName qname, ElementBinding element, Object owner, Object value)
      {
         BeanMetaDataUtil.setSimpleProperty((AbstractBeanMetaData)owner, "annotation", ((String)value).trim());
      }
   }
   
   private static class StringBufferCharacterHandler extends CharactersHandler
   {
      public static final StringBufferCharacterHandler HANDLER = new StringBufferCharacterHandler(); 
      
      @Override
      public Object unmarshal(QName qName, TypeBinding typeBinding, NamespaceContext nsCtx, org.jboss.xb.binding.metadata.ValueMetaData valueMetaData, String value)
      {
         return value;
      }

      @Override
      public void setValue(QName qname, ElementBinding element, Object owner, Object value)
      {
         ((StringBuffer)owner).append(((String)value).trim());
      }
   }
      
}
