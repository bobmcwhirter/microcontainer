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
package org.jboss.reliance.drools.core.security;

import java.security.Principal;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.login.LoginException;
import javax.security.auth.login.LoginContext;

import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.StatefulSession;
import org.drools.base.ClassObjectFilter;
import org.jboss.reliance.drools.core.RuleDependencyCheck;
import org.jboss.reliance.drools.core.aspects.DisableFireAllRules;
import org.jboss.reliance.drools.core.aspects.FireAllRulesAfter;
import org.jboss.reliance.identity.Identity;

/**
 * Drools based identity.
 *
 * @author Shane Bryzak
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class RuleBasedIdentity extends Identity implements RuleDependencyCheck
{
   private StatefulSession securityContext;
   private RuleBase securityRules;

   @Override
   public void create()
   {
      super.create();

      if (securityContext == null && securityRules != null)
      {
         securityContext = securityRules.newStatefulSession(false);
      }

      if (securityContext == null)
      {
         log.warn("No security rule base available - please install a RuleBase if permission checks are required.");
      }
   }

   @Override
   protected void postAuthenticate()
   {
      super.postAuthenticate();

      StatefulSession securityContext = getSecurityContext();

      if (securityContext != null)
      {
         // Populate the working memory with the user's principals
         for (Principal p : getSubject().getPrincipals())
         {
            if ((p instanceof Group) && ROLES_GROUP.equals(p.getName()))
            {
               Enumeration<? extends Principal> e = ((Group)p).members();
               while (e.hasMoreElements())
               {
                  Principal role = e.nextElement();
                  securityContext.insert(new Role(role.getName()));
               }
            }
         }

         securityContext.insert(getPrincipal());
      }
   }

   @DisableFireAllRules
   public boolean canResolve(Object name, Object action, Object... arg)
   {
      StatefulSession securityContext = getSecurityContext();
      if (securityContext == null)
         return false;

      List<FactHandle> handles = new ArrayList<FactHandle>();
      PermissionCheck check = new PermissionCheck(name, action);
      synchronized (securityContext)
      {
         try
         {
            handles.add(securityContext.insert(check));

            for (int i = 0; i < arg.length; i++)
            {
               if (i == 0 && arg[0] instanceof Collection)
               {
                  for (Object value : (Collection<?>)arg[i])
                  {
                     if (securityContext.getFactHandle(value) == null)
                     {
                        handles.add(securityContext.insert(value));
                     }
                  }
               }
               else
               {
                  handles.add(securityContext.insert(arg[i]));
               }
            }

            securityContext.fireAllRules();
         }
         finally
         {
            for (FactHandle handle : handles)
               securityContext.retract(handle);
         }
      }
      return check.isGranted();
   }

   @SuppressWarnings("unchecked")
   @Override
   protected void unAuthenticate()
   {
      StatefulSession securityContext = getSecurityContext();

      if (securityContext != null)
      {
         Iterator<Role> iter = securityContext.iterateObjects(new ClassObjectFilter(Role.class));
         while (iter.hasNext())
         {
            securityContext.retract(securityContext.getFactHandle(iter.next()));
         }
      }

      super.unAuthenticate();
   }

   @Override
   public boolean addRole(String role)
   {
      if (super.addRole(role))
      {
         StatefulSession securityContext = getSecurityContext();

         if (securityContext != null)
         {
            securityContext.insert(new Role(role));
            return true;
         }
      }

      return false;
   }

   @SuppressWarnings("unchecked")
   @Override
   public void removeRole(String role)
   {
      StatefulSession securityContext = getSecurityContext();

      if (securityContext != null)
      {
         Iterator<Role> iter = securityContext.iterateObjects(new ClassObjectFilter(Role.class));
         while (iter.hasNext())
         {
            Role r = iter.next();
            if (r.getName().equals(role))
            {
               FactHandle fh = securityContext.getFactHandle(r);
               securityContext.retract(fh);
               break;
            }
         }
      }

      super.removeRole(role);
   }

   /**
    * Get the statefull session.
    *
    * @return statefull session
    */
   public StatefulSession getSecurityContext()
   {
      return securityContext;
   }

   /**
    * Set statefull session.
    *
    * @param securityContext the session
    */
   public void setSecurityContext(StatefulSession securityContext)
   {
      this.securityContext = securityContext;
   }

   /**
    * Set the rule base.
    *
    * @param securityRules the rule base instance
    */
   public void setSecurityRules(RuleBase securityRules)
   {
      this.securityRules = securityRules;
   }

   //--- fire all rules after

   @FireAllRulesAfter
   public boolean login()
   {
      return super.login();
   }

   @FireAllRulesAfter
   public void quietLogin()
   {
      super.quietLogin();
   }

   @FireAllRulesAfter
   public void authenticate() throws LoginException
   {
      super.authenticate();
   }

   @FireAllRulesAfter
   public void authenticate(LoginContext loginContext) throws LoginException
   {
      super.authenticate(loginContext);
   }

   @FireAllRulesAfter
   public boolean isLoggedIn()
   {
      return super.isLoggedIn();
   }

   @FireAllRulesAfter
   public boolean isLoggedIn(boolean attemptLogin)
   {
      return super.isLoggedIn(attemptLogin);
   }
}
