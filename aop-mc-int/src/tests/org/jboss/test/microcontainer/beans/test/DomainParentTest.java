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
package org.jboss.test.microcontainer.beans.test;

import java.util.List;
import java.util.Map;

import org.jboss.aop.AspectManager;
import org.jboss.aop.Domain;
import org.jboss.aop.introduction.AnnotationIntroduction;
import org.jboss.aop.metadata.ClassMetaDataLoader;
import org.jboss.aop.microcontainer.beans.AOPDomain;


/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public abstract class DomainParentTest extends DomainProxyTest
{
   public DomainParentTest(String name)
   {
      super(name);
   }

   public void testDomainWithParentInheritance()
   {
      Domain parentFirst = getDomainAndCheck("Level2ParentFirst", true, true, true);
      List<AnnotationIntroduction> overridesFirst = parentFirst.getAnnotationOverrides();
      assertEquals(2, overridesFirst.size());
      
      assertNotNull(parentFirst.getClassMetaDataLoaders());
      assertEquals(1, parentFirst.getClassMetaDataLoaders().size());

      Domain parentLast = getDomainAndCheck("Level2ParentLast", true, true, false);
      List<AnnotationIntroduction> overridesLast = parentLast.getAnnotationOverrides();
      assertEquals(2, overridesLast.size());

      assertNotNull(parentLast.getClassMetaDataLoaders());
      assertEquals(1, parentLast.getClassMetaDataLoaders().size());
      
      assertEquals(overridesFirst.get(0).getOriginalAnnotationExpr(), overridesLast.get(1).getOriginalAnnotationExpr());
      assertEquals(overridesFirst.get(1).getOriginalAnnotationExpr(), overridesLast.get(0).getOriginalAnnotationExpr());
   }
   
   public void testDomainWithNoInheritanceFromParent()
   {
      Domain level2 = getDomainAndCheck("Level2Nada", false, false, false);
      List<AnnotationIntroduction> overrides = level2.getAnnotationOverrides();
      assertEquals(0, overrides.size());

      Map<String, ClassMetaDataLoader> loaders = level2.getClassMetaDataLoaders();
      assertEquals(0, loaders.size());
}
   
   private Domain getDomainAndCheck(String name, boolean inheritBindings, boolean inheritDefinitions, boolean isParentFirst)
   {
      Domain level1 = (Domain)getDomain("Level1");
      checkLevel1Domain(null, level1);

      Domain level2 = (Domain)getDomain(name);
      checkDomain(level1, level2, name, inheritBindings, inheritDefinitions, isParentFirst);
      return level2;
   }
   
   private void checkLevel1Domain(AspectManager parent, Domain domain)
   {
      checkDomain(parent, domain, "Level1", true, true, true);
   }
   
   private void checkDomain(AspectManager parent, Domain domain, String name, boolean inheritBindings, boolean inheritDefinitions, boolean isParentFirst)
   {
      AOPDomain bean = (AOPDomain)getBean(name);
      assertSame(domain, bean.getDomain());

      assertEquals(inheritBindings, bean.isInheritBindings());
      assertEquals(inheritDefinitions, bean.isInheritDefinitions());
      assertEquals(isParentFirst, bean.isParentFirst());
      
      if (parent == null)
      {
         assertNull(bean.getParent());
      }
      else
      {
         assertSame(parent, bean.getParent().getDomain());
      }
   }
}
