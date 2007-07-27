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
package org.jboss.test.kernel.annotations.support;

import java.util.Set;
import java.util.HashSet;

import org.jboss.beans.metadata.plugins.annotations.Aliases;
import org.jboss.beans.metadata.plugins.annotations.Demands;
import org.jboss.beans.metadata.plugins.annotations.Depends;
import org.jboss.beans.metadata.plugins.annotations.FactoryMethod;
import org.jboss.beans.metadata.plugins.annotations.Inject;
import org.jboss.beans.metadata.plugins.annotations.StringValue;
import org.jboss.beans.metadata.plugins.annotations.Supplys;
import org.jboss.beans.metadata.plugins.annotations.ValueFactory;
import org.jboss.beans.metadata.plugins.annotations.Install;
import org.jboss.beans.metadata.plugins.annotations.Uninstall;
import org.jboss.beans.metadata.plugins.annotations.Start;
import org.jboss.beans.metadata.plugins.annotations.InstallMethod;
import org.jboss.beans.metadata.plugins.annotations.ThisValue;
import org.jboss.beans.metadata.plugins.annotations.NullValue;
import org.jboss.beans.metadata.plugins.annotations.UninstallMethod;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Aliases({"al", "test"})
@Demands({"otherBean"})
@Depends({"serviceBean"})
@Supplys({"txBean"})
public class SimpleInject
{
   private int intVF;
   private TestBean testBean;
   private Set<MyDeployer> deployers;

   public int getVf()
   {
      return intVF;
   }

   @FactoryMethod
   public static SimpleInject getInstance(@NullValue Object someNull)
   {
      return new SimpleInject();
   }

   @Start
   public void startMeUp(@Inject(bean = "lifecycleBean") TestBean bean, @ValueFactory(bean="valueBean", method = "getValue", parameter = "123") int value)
   {
      intVF =- value;      
   }

   @StringValue("123")
   public void setVf(int vf)
   {
      this.intVF = vf;
   }

   @Install
   public void addDeployer(MyDeployer deployer)
   {
      if (deployers == null)
         deployers = new HashSet<MyDeployer>();
      deployers.add(deployer);
   }

   @Uninstall
   public void removeDeployer(MyDeployer deployer)
   {
      deployers.remove(deployer);
   }

   @InstallMethod
   public void whenInstalled(@ThisValue SimpleInject me, @NullValue Object plainNull)
   {
      System.out.println(me == this);
      System.out.println("plainNull = " + plainNull);
   }

   @UninstallMethod
   public void withUninstall(@ThisValue SimpleInject me, @NullValue Object plainNull)
   {
      System.out.println(me == this);
      System.out.println("plainNull = " + plainNull);
   }

   public TestBean getTestBean()
   {
      return testBean;
   }

   @Inject(bean = "testBean")
   public void setTestBean(TestBean bean)
   {
      this.testBean = bean; 
   }
}
