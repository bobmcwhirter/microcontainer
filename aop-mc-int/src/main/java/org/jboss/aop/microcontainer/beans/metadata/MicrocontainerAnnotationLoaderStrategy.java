/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.aop.microcontainer.beans.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.aop.AspectAnnotationLoader;
import org.jboss.aop.AspectAnnotationLoaderStrategy;
import org.jboss.aop.AspectAnnotationLoaderStrategySupport;
import org.jboss.aop.advice.AdviceType;
import org.jboss.aop.advice.PrecedenceDefEntry;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.pointcut.ast.ASTCFlowExpression;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class MicrocontainerAnnotationLoaderStrategy extends AspectAnnotationLoaderStrategySupport
{
   List<AspectManagerAwareBeanMetaDataFactory> factories = new ArrayList<AspectManagerAwareBeanMetaDataFactory>();
   
   public List<AspectManagerAwareBeanMetaDataFactory> getFactories()
   {
      return factories;
   }
   
   @Override
   public void deployAspect(AspectAnnotationLoader loader, boolean isFactory, String name, Scope scope)
   {
      AspectBeanMetaDataFactory aspect = new AspectBeanMetaDataFactory();
      deployAspectOrInterceptor(aspect, isFactory, name, scope);
   }
   
   @Override
   public void undeployAspect(AspectAnnotationLoader loader, String name)
   {
      //Noop
   }

   @Override
   public void deployInterceptor(AspectAnnotationLoader loader, boolean isFactory, String name, Scope scope)
   {
      InterceptorBeanMetaDataFactory aspect = new InterceptorBeanMetaDataFactory();
      deployAspectOrInterceptor(aspect, isFactory, name, scope);
   }

   @Override
   public void undeployInterceptor(AspectAnnotationLoader loader, String name)
   {
      //Noop
   }

   @Override
   public void deployAspectMethodBinding(AspectAnnotationLoader loader, AdviceType internalAdviceType,
         String aspectDefName, String methodName, String bindingName, String pointcutString, String cflow,
         ASTCFlowExpression cflowExpression) throws Exception
   {
      AdviceData entry = null;
      if (internalAdviceType == AdviceType.AROUND)
      {
         entry = new AdviceData();
      }
      else if (internalAdviceType == AdviceType.BEFORE)
      {
         entry = new BeforeAdviceData();
      }  
      else if (internalAdviceType == AdviceType.AFTER)
      {
         entry = new AfterAdviceData();
      }
      else if (internalAdviceType == AdviceType.THROWING)
      {
         entry = new ThrowingAdviceData();
      }
      else if (internalAdviceType == AdviceType.FINALLY)
      {
         entry = new FinallyAdviceData();
      }
      entry.setAdviceMethod(methodName);
      entry.setRefName(aspectDefName);

      deployBinding(bindingName, pointcutString, cflow, entry);
   }

   @Override
   public void undeployAspectMethodBinding(AspectAnnotationLoader loader, String bindingName, String className,
         String methodName)
   {
      //Noop
   }

   @Override
   public void deployInterceptorBinding(AspectAnnotationLoader loader, String name, String pointcutString,
         String cflow, ASTCFlowExpression cflowExpression) throws Exception
   {
      InterceptorRefData entry = new InterceptorRefData();
      entry.setRefName(name);
   
      deployBinding(name, pointcutString, cflow, entry);
   }

   @Override
   public void undeployInterceptorBinding(AspectAnnotationLoader loader, String name)
   {
      //Noop
   }

   private void deployAspectOrInterceptor(AspectBeanMetaDataFactory aspect, boolean isFactory, String name, Scope scope)
   {
      aspect.setScope(scope.name());
      aspect.setName(name);
      if (isFactory)
      {
         aspect.setFactory(name);
      }
      else
      {
         aspect.setBean(name);
      }
      
      factories.add(aspect);
   }
   
   private void deployBinding(String name, String pointcutString, String cflow, BaseInterceptorData entry)
   {
      BindBeanMetaDataFactory bind = new BindBeanMetaDataFactory();
      bind.setName("Binding$" + name);
      bind.setPointcut(pointcutString);
      bind.setCflow(cflow);
      
      bind.setInterceptors(Collections.singletonList(entry));
   
      factories.add(bind);
   }

   @Override
   public void deployCFlow(AspectAnnotationLoader loader, CFlowStackInfo stack)
   {
      CFlowStackBeanMetaDataFactory cflow = new CFlowStackBeanMetaDataFactory();
      cflow.setName(stack.getName());

      CFlowInfo[] cflows = stack.getCFlows();
      if (cflows != null)
      {
         List<CFlowEntry> entries = new ArrayList<CFlowEntry>();
         for (CFlowInfo current : cflows)
         {
            CFlowEntry entry = new CFlowEntry();
            entry.setExpr(current.getExpr());
            entry.setCalled(!current.isNot());
            entries.add(entry);
         }
         cflow.setCalledEntries(entries);
      }
      factories.add(cflow);
   }

   @Override
   public void undeployCFlow(AspectAnnotationLoader loader, String name)
   {
      //Noop
   }

   @Override
   public void deployAnnotationIntroduction(AspectAnnotationLoader loader, String expr, String annotation,
         boolean invisible)
   {
      AnnotationIntroductionBeanMetaDataFactory intro = new AnnotationIntroductionBeanMetaDataFactory();
      intro.setExpr(expr);
      intro.setAnnotation(annotation);
      intro.setInvisible(invisible);
      factories.add(intro);
   }

   @Override
   public void undeployAnnotationIntroduction(AspectAnnotationLoader loader, String expr, String annotation,
         boolean invisible)
   {
      //Noop
   }

   @Override
   public void deployTypedef(AspectAnnotationLoader loader, String name, String expr) throws Exception
   {
      TypeDefBeanMetaDataFactory typedef = new TypeDefBeanMetaDataFactory();
      typedef.setName(name);
      typedef.setExpr(expr);
      factories.add(typedef);
   }

   @Override
   public void undeployTypedef(AspectAnnotationLoader loader, String name)
   {
      //Noop
   }

   @Override
   public void deployDynamicCFlow(AspectAnnotationLoader loader, String name, String clazz)
   {
      DynamicCflowBeanMetaDataFactory dcflow = new DynamicCflowBeanMetaDataFactory();
      dcflow.setName(name);
      dcflow.setClazz(clazz);
      factories.add(dcflow);
   }

   @Override
   public void undeployDynamicCFlow(AspectAnnotationLoader loader, String name)
   {
      //Noop
   }

   @Override
   public void deployInterfaceIntroduction(AspectAnnotationLoader loader, AspectAnnotationLoaderStrategy.InterfaceIntroductionInfo introduction)
   {
      IntroductionBeanMetaDataFactory intro = new IntroductionBeanMetaDataFactory();
      intro.setName(introduction.getName());
      intro.setExpr(introduction.getExpr());
      intro.setClazz(introduction.getTarget());
      if (introduction.getInterfaces() != null)
      {
         intro.setInterfaces(createCommaSeparatedInterfaceString(introduction.getInterfaces()));
      }
      
      if (introduction.getMixins() != null)
      {
         List<MixinData> mixinDatas = new ArrayList<MixinData>();
         for (InterfaceIntroductionMixinInfo mixin : introduction.getMixins())
         {
            MixinData mixinData = new MixinData();
            mixinData.setMixin(mixin.getClassname());
            mixinData.setInterfaces(createCommaSeparatedInterfaceString(mixin.getInterfaces()));
            mixinData.setConstruction(mixin.getConstruction());
            
            mixinDatas.add(mixinData);
         }
         intro.setMixins(mixinDatas);
      }
      
      factories.add(intro);
   }

   @Override
   public void undeployInterfaceIntroduction(AspectAnnotationLoader loader, String name)
   {
      //Noop
   }

   @Override
   public void deployPointcut(AspectAnnotationLoader loader, String name, String expr) throws Exception
   {
      NamedPointcutBeanMetaDataFactory pointcut = new NamedPointcutBeanMetaDataFactory();
      pointcut.setName(name);
      pointcut.setExpr(expr);
      factories.add(pointcut);
   }

   @Override
   public void undeployPointcut(AspectAnnotationLoader loader, String name)
   {
      //Noop
   }

   @Override
   public void deployPrecedence(AspectAnnotationLoader loader, String name, PrecedenceDefEntry[] pentries)
   {
      PrecedenceBeanMetaDataFactory precedence = new PrecedenceBeanMetaDataFactory();
      
      List<BaseInterceptorData> entries = new ArrayList<BaseInterceptorData>();
      for (PrecedenceDefEntry pentry : pentries)
      {
         String clazz = pentry.getInterceptorClass();
         String advice = pentry.getAdviceMethod();
         
         BaseInterceptorData entry = null;
         if (advice == null)
         {
            entry = new InterceptorRefData();
            entry.setRefName(clazz);
         }
         else
         {
            entry = new AdviceData();
            entry.setRefName(clazz);
            ((AdviceData)entry).setAdviceMethod(advice);
         }
         entries.add(entry);
         precedence.setEntries(entries);
      }
      
      factories.add(precedence);
   }

   @Override
   public void undeployPrecedence(AspectAnnotationLoader loader, String name)
   {
      super.undeployPrecedence(loader, name);
   }
   
   @Override
   public void deployDeclare(AspectAnnotationLoader loader, String name, String expr, boolean warning, String msg)
         throws Exception
   {
      AbstractDeclareBeanMetaDataFactory declare = 
         warning ? new DeclareWarningBeanMetaDataFactory() : new DeclareErrorBeanMetaDataFactory();
         
      declare.setName(name);
      declare.setExpr(expr);
      declare.setMessage(msg);
      factories.add(declare);
   }

   private String createCommaSeparatedInterfaceString(String[] interfaces)
   {
      StringBuilder sb = new StringBuilder();
      for (int i = 0 ; i < interfaces.length ; i++)
      {
         if (i > 0)
         {
            sb.append(", ");
         }
         sb.append(interfaces[i]);
      }
      return sb.toString();
   }
}
