/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.aop.microcontainer.integration;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jboss.aop.Advised;
import org.jboss.aop.Advisor;
import org.jboss.aop.proxy.container.AspectManaged;
import org.jboss.aop.util.ClassInfoMethodHashing;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.kernel.spi.metadata.MutableMetaDataContext;
import org.jboss.metadata.plugins.loader.memory.MemoryMetaDataLoader;
import org.jboss.metadata.spi.repository.MetaDataRepository;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.retrieval.AnnotationItem;
import org.jboss.metadata.spi.retrieval.AnnotationsItem;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.spi.scope.CommonLevels;
import org.jboss.metadata.spi.scope.Scope;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.repository.spi.MetaDataContext;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class AOPMetaDataContext implements MutableMetaDataContext
{
   final static List<Annotation> EMPTY_ANNOTATIONS = new ArrayList<Annotation>();
   Scope scope;
   Object target;
   MutableMetaDataRepository repository;
   String beanName;
   ScopeKey instanceKey;
   
   public AOPMetaDataContext(MutableMetaDataRepository repository, String beanName)
   {
      this.repository = repository;
      this.beanName = beanName;
      //TODO: This needs linking up with the parent scopes somehow - where will that info come from?
      scope = new Scope(CommonLevels.INSTANCE, beanName);
      instanceKey = new ScopeKey(scope);
   }
   
   public <T extends Annotation> boolean hasAnnotation(Class<T> ann)
   {
      return getAnnotation(ann) != null;
   }

   public <T extends Annotation> Annotation getAnnotation(Class<T> ann)
   {
      return getAnnotation(instanceKey, ann);
   }

   public <T extends Annotation> boolean hasAnnotationForMethod(long methodHash, Class<T> ann)
   {
      return getAnnotationForMethod(methodHash, ann) != null;
   }

   public <T extends Annotation> Annotation getAnnotationForMethod(long methodHash, Class<T> ann)
   {
      ScopeKey joinpointKey = createHashedJoinpointKey(methodHash);
      return getAnnotation(joinpointKey, ann);
   }

   public List<Annotation> getAnnotations()
   {
      return getAnnotations(instanceKey);
   }
   
   public List<Annotation> getAnnotationsForMethod(long methodHash)
   {
      ScopeKey joinpointKey = createHashedJoinpointKey(methodHash);
      return getAnnotations(joinpointKey);
   }
   
   public List<Annotation> getAnnotationsForMethods(long[] methodHashes)
   {
      ArrayList<Annotation> annotations = new ArrayList<Annotation>();
      for (long hash : methodHashes)
      {
         ScopeKey joinpointKey = createHashedJoinpointKey(hash);
         List<Annotation> methodAnnotations = getAnnotations(joinpointKey);
         annotations.addAll(methodAnnotations);
      }
      return annotations;
   }
   
   public MetaDataRepository getRepository()
   {
      return repository;
   }

   /**
    * Add instance-level annotations
    * @param annotations a Set<AnnotationMetaData>
    */
   public void addAnnotations(Set<AnnotationMetaData> annotations)
   {
      if (annotations.size() == 0)
      {
         return;
      }

      MemoryMetaDataLoader retrieval = new MemoryMetaDataLoader(instanceKey);
      for (AnnotationMetaData annotationMetaData : annotations)
      {
         Annotation annotation = annotationMetaData.getAnnotationInstance();
         retrieval.addAnnotation(annotation);
      }
      repository.addMetaDataRetrieval(retrieval);
   }
   
   public void addPropertyAnnotations(String propertyName, Set<PropertyInfo> propertyInfos, Set<AnnotationMetaData> annotations)
   {
      for (PropertyInfo info : propertyInfos)
      {
         if (propertyName.equals(info.getName()))
         {
            MemoryMetaDataLoader getterRetrieval = createGetterMetaDataRetrieval(info);
            MemoryMetaDataLoader setterRetrieval = createSetterMetaDataRetrieval(info);
            
            if (getterRetrieval == null && setterRetrieval == null)
            {
               continue;
            }
            
            for (AnnotationMetaData annotation : annotations)
            {
               if (getterRetrieval != null)
               {
                  getterRetrieval.addAnnotation(annotation.getAnnotationInstance());
               }
               if (setterRetrieval != null)
               {
                  setterRetrieval.addAnnotation(annotation.getAnnotationInstance());
               }
            }
            
            if (getterRetrieval != null)
            {
               repository.addMetaDataRetrieval(getterRetrieval);
            }
            if (setterRetrieval != null)
            {
               repository.addMetaDataRetrieval(setterRetrieval);
            }
         }
      }
   }

   public void setTarget(Object tgt)
   {
      if (tgt == null)
      {
         return;
      }

      target = tgt;
      Advisor advisor = null;
      if (tgt instanceof AspectManaged)
      {
         advisor = (Advisor)((AspectManaged)tgt).getInstanceAdvisor();
      }
      else if (tgt instanceof Advised)
      {
         advisor = (Advisor)((Advised)tgt)._getInstanceAdvisor();
      }
      
      if (advisor != null)
      {
         MetaDataContext advCtx = advisor.getMetadataContext();
         if (advCtx != null && advCtx != this)
         {
            throw new RuntimeException("Different context being set in constructed advisor");
         }
         if (advCtx == null)
         {
            advisor.setMetadataContext(this);
         }
      }
   }
   
   private MemoryMetaDataLoader createGetterMetaDataRetrieval(PropertyInfo propertyInfo)
   {
      MethodInfo getter = propertyInfo.getGetter();
      return createMethodMetaDataRetrieval(getter);
   }
   
   private MemoryMetaDataLoader createSetterMetaDataRetrieval(PropertyInfo propertyInfo)
   {
      MethodInfo setter = propertyInfo.getSetter();
      return createMethodMetaDataRetrieval(setter);
   }
   
   private MemoryMetaDataLoader createMethodMetaDataRetrieval(MethodInfo accessor)
   {
      if (accessor == null)
      {
         return null;
      }
      long hash = ClassInfoMethodHashing.methodHash(accessor);
      ScopeKey joinpointKey = createHashedJoinpointKey(hash);
      MemoryMetaDataLoader retrieval = new MemoryMetaDataLoader(joinpointKey);
      return retrieval;
   }
   
   private ScopeKey createHashedJoinpointKey(long hash)
   {
      ScopeKey joinpointKey = new ScopeKey(instanceKey.getScopes());
      joinpointKey.addScope(CommonLevels.JOINPOINT, String.valueOf(hash));
      return joinpointKey;
   }

   private <T extends Annotation> Annotation getAnnotation(ScopeKey key, Class<T> ann)
   {
      MetaDataRetrieval retrieval = repository.getMetaDataRetrieval(key);
      
      if (retrieval != null)
      {
         AnnotationItem item = retrieval.retrieveAnnotation(ann);
         if (item != null)
         {
            return item.getAnnotation();
         }
      }      
      return null;
   }

   private List<Annotation> getAnnotations(ScopeKey key)
   {
      MetaDataRetrieval retrieval = repository.getMetaDataRetrieval(key);
      
      if (retrieval != null)
      {
         AnnotationsItem item = retrieval.retrieveAnnotations();
         if (item != null)
         {
            AnnotationItem[] items = item.getAnnotations();
            List<Annotation> annotations = new ArrayList<Annotation>();
            for (AnnotationItem aitem : items)
            {
               annotations.add(aitem.getAnnotation());
            }
            return annotations;    
         }
      }      
      return EMPTY_ANNOTATIONS;
   }
}   
