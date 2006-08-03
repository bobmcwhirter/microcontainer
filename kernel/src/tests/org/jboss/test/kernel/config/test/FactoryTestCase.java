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
package org.jboss.test.kernel.config.test;

import java.util.ArrayList;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.test.kernel.config.support.SimpleBean;
import org.jboss.test.kernel.config.support.SimpleBeanFactory;

/**
 * Factory Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class FactoryTestCase extends AbstractKernelConfigTest
{
   public static Test suite()
   {
      return suite(FactoryTestCase.class);
   }

   public FactoryTestCase(String name)
   {
      super(name);
   }

   public FactoryTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public void testSimpleInstantiateFromFactory() throws Throwable
   {
      SimpleBean bean = simpleInstantiateFromFactory();
      assertEquals("createSimpleBean()", SimpleBeanFactory.getMethodUsed());
      assertEquals("()", bean.getConstructorUsed());
   }

   protected SimpleBean simpleInstantiateFromFactory() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      SimpleBeanFactory factory = new SimpleBeanFactory();
      AbstractValueMetaData vmd = new AbstractValueMetaData(factory);
      cmd.setFactory(vmd);
      cmd.setFactoryMethod("createSimpleBean");

      return (SimpleBean) instantiate(configurator, bmd);
   }
   
   public void testSimpleInstantiateFromFactoryWithParameters() throws Throwable
   {
      SimpleBean bean = simpleInstantiateFromFactoryWithParameters();
      assertEquals("createSimpleBean(String)", SimpleBeanFactory.getMethodUsed());
      assertEquals("Factory Value", bean.getConstructorUsed());
   }
   
   protected SimpleBean simpleInstantiateFromFactoryWithParameters() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      SimpleBeanFactory factory = new SimpleBeanFactory();
      AbstractValueMetaData vmd = new AbstractValueMetaData(factory);
      cmd.setFactory(vmd);
      cmd.setFactoryMethod("createSimpleBean");
      ArrayList<ParameterMetaData> constructorParams = new ArrayList<ParameterMetaData>();
      AbstractParameterMetaData pmd = new AbstractParameterMetaData("Factory Value");
      constructorParams.add(pmd);
      cmd.setParameters(constructorParams);

      return (SimpleBean) instantiate(configurator, bmd);
   }

   public void testSimpleInstantiateFromFactoryWithIntegerParameter() throws Throwable
   {
      SimpleBean bean = simpleInstantiateFromFactoryWithIntegerParameter();
      assertEquals("createSimpleBean(integer)", SimpleBeanFactory.getMethodUsed());
      assertEquals("4", bean.getConstructorUsed());
   }

   protected SimpleBean simpleInstantiateFromFactoryWithIntegerParameter() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      SimpleBeanFactory factory = new SimpleBeanFactory();
      AbstractValueMetaData vmd = new AbstractValueMetaData(factory);
      cmd.setFactory(vmd);
      cmd.setFactoryMethod("createSimpleBean");
      ArrayList<ParameterMetaData> constructorParams = new ArrayList<ParameterMetaData>();
      AbstractParameterMetaData pmd = new AbstractParameterMetaData(new Integer(4));
      pmd.setType("java.lang.Integer");
      constructorParams.add(pmd);
      cmd.setParameters(constructorParams);

      return (SimpleBean) instantiate(configurator, bmd);
   }

   public void testStaticInstantiateFromFactory() throws Throwable
   {
      SimpleBean bean = staticInstantiateFromFactory();
      assertEquals("staticCreateSimpleBean()", SimpleBeanFactory.getMethodUsed());
      assertEquals("()", bean.getConstructorUsed());
   }

   protected SimpleBean staticInstantiateFromFactory() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      cmd.setFactoryClass(SimpleBeanFactory.class.getName());
      cmd.setFactoryMethod("staticCreateSimpleBean");

      return (SimpleBean) instantiate(configurator, bmd);
   }

   public void testStaticInstantiateFromFactoryWithParameters() throws Throwable
   {
      SimpleBean bean = staticInstantiateFromFactoryWithParameters();
      assertEquals("staticCreateSimpleBean(String)", SimpleBeanFactory.getMethodUsed());
      assertEquals("Static Factory Value", bean.getConstructorUsed());
   }

   protected SimpleBean staticInstantiateFromFactoryWithParameters() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      cmd.setFactoryClass(SimpleBeanFactory.class.getName());
      cmd.setFactoryMethod("staticCreateSimpleBean");
      ArrayList<ParameterMetaData> constructorParams = new ArrayList<ParameterMetaData>();
      AbstractParameterMetaData pmd = new AbstractParameterMetaData("Static Factory Value");
      constructorParams.add(pmd);
      cmd.setParameters(constructorParams);

      return (SimpleBean) instantiate(configurator, bmd);
   }

   public void testStaticInstantiateFromFactoryWithIntegerParameter() throws Throwable
   {
      SimpleBean bean = staticInstantiateFromFactoryWithIntegerParameter();
      assertEquals("staticCreateSimpleBean(integer)", SimpleBeanFactory.getMethodUsed());
      assertEquals("7", bean.getConstructorUsed());
   }

   protected SimpleBean staticInstantiateFromFactoryWithIntegerParameter() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      cmd.setFactoryClass(SimpleBeanFactory.class.getName());
      cmd.setFactoryMethod("staticCreateSimpleBean");
      ArrayList<ParameterMetaData> constructorParams = new ArrayList<ParameterMetaData>();
      AbstractParameterMetaData pmd = new AbstractParameterMetaData(new Integer(7));
      pmd.setType("java.lang.Integer");
      constructorParams.add(pmd);
      cmd.setParameters(constructorParams);

      return (SimpleBean) instantiate(configurator, bmd);
   }
}