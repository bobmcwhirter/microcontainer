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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.aop.Advised;
import org.jboss.aop.Advisor;
import org.jboss.aop.proxy.container.AspectManaged;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.reflect.spi.MethodInfo;

import org.jboss.repository.spi.BasicMetaData;
import org.jboss.repository.spi.CommonNames;
import org.jboss.repository.spi.KernelRepository;
import org.jboss.repository.spi.Key;
import org.jboss.repository.spi.MetaData;
import org.jboss.repository.spi.MetaDataContext;
import org.jboss.util.JBossStringBuilder;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class AOPMetaDataContext implements MetaDataContext
{
   Map<String, String> scope;
   Object target;
   KernelRepository repository;
   private final static Class[] EMPTY_CLASS_ARRAY = new Class[0];

   public AOPMetaDataContext(KernelRepository repository, Object beanName)
   {
      this.repository = repository;
      scope = new HashMap<String, String>();
      
      //TODO: Determine level this data should be put at.
      scope.put(CommonNames.DOMAIN, "d");
      scope.put(CommonNames.CLUSTER, "c");
      scope.put(CommonNames.SERVER, "s");
      scope.put(CommonNames.APPLICATION, "a");
      scope.put(CommonNames.DEPLOYMENT, (String) beanName);
   }
   
   public Object getAnnotation(Class ann)
   {
      return getAnnotation(ann.getName());
   }

   public boolean hasAnnotation(String ann)
   {
      return getAnnotation(ann) != null;
   }

   private Object getAnnotation(String name)
   {
      Key key = new Key(name, scope);
      return repository.getMetaData(key);
   }

   public List getAnnotations()
   {
      ArrayList<Object> annotations = new ArrayList<Object>();
      Iterator keys = repository.getKeyNames();
      while (keys.hasNext())
      {
         Key key = (Key)keys.next();
         if (key.getName().length == 1)
         {
            Key realKey =  new Key(key.getName(), scope);
            Object annotation = repository.getMetaData(realKey);
            if (annotation != null)
            {
               annotations.add(annotation);
            }
         }
      }
      
      return annotations;
   }


   public Object getAnnotation(Method m, Class ann)
   {
      return getAnnotation(m, ann.getName());
   }

   public boolean hasAnnotation(Method m, String ann)
   {
      return getAnnotation(m, ann) != null;
   }

   private Object getAnnotation(Method m, String name)
   {
      Key key = createMethodKey(name, m.getName());
      return repository.getMetaData(key);
   }
   
   public List getAnnotationsForMethod(String methodName)
   {
      ArrayList<Object> annotations = new ArrayList<Object>();
      Iterator keys = repository.getKeyNames();
      while (keys.hasNext())
      {
         Key key = (Key)keys.next();
         String[] name = key.getName(); 
         if (name.length == 2)
         {
            if (name[1].equals(methodName))
            {
               Key realKey =  new Key(key.getName(), scope);
               Object annotation = repository.getMetaData(realKey);
               if (annotation != null)
               {
                  annotations.add(annotation);
               }
            }
         }
      }
      
      return annotations;
   }

   public List getAnnotationsForMethods(String[] methodNames)
   {
      ArrayList<Object> annotations = new ArrayList<Object>();
      Iterator keys = repository.getKeyNames();
      while (keys.hasNext())
      {
         Key key = (Key)keys.next();
         String[] name = key.getName(); 
         if (name.length == 2)
         {
            for (int i = 0 ; i < methodNames.length ; i++)
            {
               if (name[1].equals(methodNames[i]))
               {
                  Key realKey =  new Key(key.getName(), scope);
                  Object annotation = repository.getMetaData(realKey);
                  if (annotation != null)
                  {
                     annotations.add(annotation);
                  }
               }
            }
         }
      }
      
      return annotations;
   }
   
   public KernelRepository getRepository()
   {
      return repository;
   }

   public Map getScope()
   {
      return scope;
   }

   public void addAnnotations(Set annotations)
   {
      for (Iterator i = annotations.iterator(); i.hasNext();)
      {
         AnnotationMetaData annotation = (AnnotationMetaData) i.next();
         Key key = new Key(getName(annotation), scope);            
         
         final MetaData metadata = createMetaData(annotation);
         repository.addMetaData(key, metadata);
      }
   }
   
   public void addPropertyAnnotations(String propertyName, Set propertyInfos, Set annotations)
   {
      for (Iterator props = propertyInfos.iterator() ; props.hasNext() ; )
      {
         PropertyInfo info = (PropertyInfo)props.next();
         
         if (propertyName.equals(info.getName()))
         {
            MethodInfo getter = info.getGetter();
            MethodInfo setter = info.getSetter();
            
            for (Iterator anns = annotations.iterator() ; anns.hasNext() ; )
            {
               AnnotationMetaData annotation = (AnnotationMetaData)anns.next();
               
               if (getter != null || setter != null)
               {
                  MetaData data = createMetaData(annotation);
                  if (getter != null)
                  {
                     Key key = createMethodKey(getName(annotation), getter.getName());
                     repository.addMetaData(key, data);
                  }
                  if (setter != null)
                  {
                     Key key = createMethodKey(getName(annotation), setter.getName());
                     repository.addMetaData(key, data);
                  }
               }
            }
         }
      }
   }

   private Key createMethodKey(String annotationName, String methodName)
   {
      return new Key(new String[] {annotationName, methodName}, scope);
   }
   
   private MetaData createMetaData(AnnotationMetaData metadata)
   {
      return new BasicMetaData(0, metadata.getAnnotationInstance());
   }
   
   
   private String getName(Annotation annotation)
   {
      return annotation.annotationType().getName();
   }
   
   private String getName(MetaData metadata)
   {
      return getName((Annotation)metadata.getData());
   }
   
   private String getName(AnnotationMetaData annotation)
   {
      return getName(annotation.getAnnotationInstance());
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
}   
