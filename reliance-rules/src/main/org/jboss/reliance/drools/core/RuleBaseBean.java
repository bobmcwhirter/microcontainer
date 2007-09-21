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
package org.jboss.reliance.drools.core;

import java.io.IOException;
import java.io.InputStream;

import org.drools.RuleBase;
import org.drools.RuleBaseConfiguration;
import org.drools.RuleBaseFactory;
import org.drools.StatefulSession;
import org.drools.StatelessSession;
import org.drools.rule.Package;
import org.jboss.beans.metadata.api.annotations.Constructor;
import org.jboss.beans.metadata.api.annotations.FromContext;
import org.jboss.beans.metadata.api.annotations.Inject;
import org.jboss.beans.metadata.api.annotations.Install;
import org.jboss.beans.metadata.api.annotations.Uninstall;
import org.jboss.logging.Logger;

/**
 * RuleBase bean delegate.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class RuleBaseBean implements RuleBase
{
   protected Logger log = Logger.getLogger(getClass());

   private String name;
   private RuleBaseConfiguration configuration;
   private org.drools.RuleBase ruleBase;

   @Constructor
   public RuleBaseBean(@Inject(fromContext = FromContext.NAME) String name)
   {
      this.name = name;
   }

   /**
    * Set the configuration.
    *
    * @param configuration the configuration
    */
   public void setConfiguration(RuleBaseConfiguration configuration)
   {
      this.configuration = configuration;
   }

   public void create() throws Exception
   {
      ruleBase = RuleBaseFactory.newRuleBase(configuration);
   }

   public void destroy()
   {
      ruleBase = null;
   }

   /**
    * Does name match wrapper's name.
    *
    * @param pw package wrapper
    * @return true if name matches
    */
   protected boolean matchPackage(PackageWrapper pw)
   {
      return pw.getName() == null || pw.getName().equals(name);
   }

   @Install(whenRequired = "Start", cardinality = "1..n")
   public void addPackage(PackageWrapper pckg) throws Exception
   {
      // no need to synch - drools already do that
      if (matchPackage(pckg))
         ruleBase.addPackage(pckg.getPackage());
   }

   @Uninstall(whenRequired = "Start")
   public void removePackage(PackageWrapper pckg)
   {
      if (matchPackage(pckg))
         ruleBase.removePackage(pckg.getPackage().getName());
   }

   // delegate

   public StatelessSession newStatelessSession()
   {
      return ruleBase.newStatelessSession();
   }

   public StatefulSession newStatefulSession()
   {
      return ruleBase.newStatefulSession();
   }

   public StatefulSession newStatefulSession(boolean keepReference)
   {
      return ruleBase.newStatefulSession(keepReference);
   }

   public StatefulSession newStatefulSession(InputStream inputStream) throws IOException, ClassNotFoundException
   {
      return ruleBase.newStatefulSession(inputStream);
   }

   public StatefulSession newStatefulSession(InputStream inputStream, boolean b) throws IOException, ClassNotFoundException
   {
      return ruleBase.newStatefulSession(inputStream);
   }

   public Package[] getPackages()
   {
      return ruleBase.getPackages();
   }

   public Package getPackage(String name)
   {
      return ruleBase.getPackage(name);
   }

   public void addPackage(Package pckg) throws Exception
   {
      ruleBase.addPackage(pckg);
   }

   public void removePackage(String name)
   {
      ruleBase.removePackage(name);
   }

   public void removeRule(String name, String ruleName)
   {
      ruleBase.removeRule(name, ruleName);
   }

   public void removeFunction(String packageName, String functionName)
   {
      ruleBase.removeFunction(packageName, functionName);
   }

   public void removeProcess(String id)
   {
      ruleBase.removeProcess(id);
   }

   public StatefulSession[] getStatefulSessions()
   {
      return ruleBase.getStatefulSessions();
   }
}
