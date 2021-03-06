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
package org.jboss.test.microcontainer.test;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import javassist.CtClass;
import javassist.CtMethod;
import junit.framework.TestCase;

import org.jboss.aop.util.ClassInfoMethodHashing;
import org.jboss.aop.util.JavassistMethodHashing;
import org.jboss.aop.util.MethodHashing;
import org.jboss.aop.util.ReflectToJavassist;
import org.jboss.reflect.plugins.introspection.IntrospectionTypeInfoFactory;
import org.jboss.reflect.plugins.javassist.JavassistTypeInfoFactory;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.test.microcontainer.support.SubClass;

/**
 * Test to make sure that the different mechanisms of creating method hashes return the same hashes
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 61752 $
 */
public class MethodHashingTestCase  extends TestCase
{

   public MethodHashingTestCase(String arg0)
   {
      super(arg0);
   }


   @SuppressWarnings("unchecked")
   public void testDeclaredMethodHashing() throws Exception
   {
      Class<?> clazz = SubClass.class;
      CtClass ctclass = getCtClass(clazz);
      ClassInfo classInfoReflect = getIntrospectTypeInfo(clazz);
      ClassInfo classInfoJavassist = getJavassistTypeInfo(clazz);

      Map reflectInfoMethods = ClassInfoMethodHashing.getDeclaredMethodMap(classInfoReflect);
      Map javassistInfoMethods = ClassInfoMethodHashing.getDeclaredMethodMap(classInfoJavassist);
      Map javassistMethods = JavassistMethodHashing.getDeclaredMethodMap(ctclass);

      compareMaps(reflectInfoMethods, javassistInfoMethods, 1);
      compareMaps(reflectInfoMethods, javassistMethods, 1);

      for (Iterator it = reflectInfoMethods.keySet().iterator() ; it.hasNext() ; )
      {
         Long hash = (Long)it.next();
         CtMethod ctmethod = (CtMethod)javassistMethods.get(hash);
         MethodInfo methodInfo = (MethodInfo)reflectInfoMethods.get(hash);
         compareMethods(methodInfo, ctmethod);

         MethodInfo methodInfo2 = (MethodInfo)javassistInfoMethods.get(hash);
         compareMethods(methodInfo, methodInfo2);

         Method method = MethodHashing.findMethodByHash(clazz, hash.longValue());
         assertNotNull(method);
         compareMethods(methodInfo, method);

         Method methodB = org.jboss.util.MethodHashing.findMethodByHash(clazz, hash.longValue());
         assertNotNull(methodB);
         compareMethods(methodInfo, methodB);
      }
   }

   @SuppressWarnings("unchecked")
   public void testMethodHashing() throws Exception
   {
      Class clazz = SubClass.class;
      CtClass ctclass = getCtClass(clazz);
      ClassInfo classInfoReflect = getIntrospectTypeInfo(clazz);
      ClassInfo classInfoJavassist = getJavassistTypeInfo(clazz);

      Map reflectInfoMethods = ClassInfoMethodHashing.getMethodMap(classInfoReflect);
      Map javassistInfoMethods = ClassInfoMethodHashing.getMethodMap(classInfoJavassist);
      Map javassistMethods = JavassistMethodHashing.getMethodMap(ctclass);

      compareMaps(reflectInfoMethods, javassistInfoMethods, 2);
      compareMaps(reflectInfoMethods, javassistMethods, 2);

      for (Iterator it = reflectInfoMethods.keySet().iterator() ; it.hasNext() ; )
      {
         Long hash = (Long)it.next();
         CtMethod ctmethod = (CtMethod)javassistMethods.get(hash);
         MethodInfo methodInfo = (MethodInfo)reflectInfoMethods.get(hash);
         compareMethods(methodInfo, ctmethod);

         MethodInfo methodInfo2 = (MethodInfo)javassistInfoMethods.get(hash);
         compareMethods(methodInfo, methodInfo2);

         Method method = MethodHashing.findMethodByHash(clazz, hash.longValue());
         assertNotNull(method);
         compareMethods(methodInfo, method);

         Method methodB = org.jboss.util.MethodHashing.findMethodByHash(clazz, hash.longValue());
         assertNotNull(methodB);
         compareMethods(methodInfo, methodB);
      }
   }

   private CtClass getCtClass(Class<?> clazz) throws Exception
   {
      return ReflectToJavassist.classToJavassist(clazz);
   }

   private ClassInfo getIntrospectTypeInfo(Class<?> clazz)
   {
      IntrospectionTypeInfoFactory typeInfoFactory = new IntrospectionTypeInfoFactory();
      ClassInfo classInfo = (ClassInfo)typeInfoFactory.getTypeInfo(clazz);
      return classInfo;
   }

   private ClassInfo getJavassistTypeInfo(Class<?> clazz)
   {
      JavassistTypeInfoFactory typeInfoFactory = new JavassistTypeInfoFactory();
      ClassInfo classInfo = (ClassInfo)typeInfoFactory.getTypeInfo(clazz);
      return classInfo;
   }

   @SuppressWarnings("unchecked")
   private void compareMaps(Map mapA, Map mapB, int expecedSize)
   {
      assertEquals(expecedSize, mapA.size());
      assertEquals(expecedSize, mapB.size());

      for (Iterator it = mapA.keySet().iterator() ; it.hasNext() ; )
      {
         Long l = (Long)it.next();
         assertNotNull(mapB.get(l));
      }
   }

   private void compareMethods(MethodInfo methodInfo, CtMethod method) throws Exception
   {
      System.out.println("-----> method " + method);
      assertEquals(methodInfo.getName(), method.getName());
      assertEquals(methodInfo.getDeclaringClass().getName(), method.getDeclaringClass().getName());
      assertEquals(methodInfo.getReturnType().getName(), method.getReturnType().getName());
      assertEquals(methodInfo.getModifiers(), method.getModifiers());
      assertEquals(methodInfo.getParameterTypes().length, method.getParameterTypes().length);
   }

   private void compareMethods(MethodInfo methodInfo, MethodInfo method) throws Exception
   {
      System.out.println("-----> method " + method);
      assertEquals(methodInfo.getName(), method.getName());
      assertEquals(methodInfo.getDeclaringClass().getName(), method.getDeclaringClass().getName());
      assertEquals(methodInfo.getReturnType().getName(), method.getReturnType().getName());
      assertEquals(methodInfo.getModifiers(), method.getModifiers());
      assertEquals(methodInfo.getParameterTypes().length, method.getParameterTypes().length);
   }

   private void compareMethods(MethodInfo methodInfo, Method method) throws Exception
   {
      System.out.println("-----> method " + method);
      assertEquals(methodInfo.getName(), method.getName());
      assertEquals(methodInfo.getDeclaringClass().getName(), method.getDeclaringClass().getName());
      assertEquals(methodInfo.getReturnType().getName(), method.getReturnType().getName());
      assertEquals(methodInfo.getModifiers(), method.getModifiers());
      assertEquals(methodInfo.getParameterTypes().length, method.getParameterTypes().length);
   }
}
