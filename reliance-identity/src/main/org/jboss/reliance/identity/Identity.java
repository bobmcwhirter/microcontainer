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
package org.jboss.reliance.identity;

import java.io.IOException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.jboss.logging.Logger;
import org.jboss.beans.metadata.api.annotations.Inject;

/**
 * Simple identity.
 *
 * @author Shane Bryzak
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class Identity
{
   protected static Logger log = Logger.getLogger(Identity.class);
   public static final String ROLES_GROUP = "Roles";

   private String username;
   private String password;
   private Principal principal;
   private Subject subject;
   private String jaasConfigName = null;
   private Configuration configuration;
   /**
    * Flag that indicates we are in the process of authenticating
    */
   private boolean authenticating = false;
   private List<String> preAuthenticationRoles = new ArrayList<String>();

   public void create()
   {
      subject = new Subject();
   }

   public boolean login()
   {
      try
      {
         authenticate();
         if (log.isTraceEnabled())
         {
            log.trace("Login successful for: " + getUsername());
         }
         return true;
      }
      catch (LoginException ex)
      {
         if (log.isTraceEnabled())
         {
            log.trace("Login failed for: " + getUsername(), ex);
         }
         return false;
      }
   }

   /**
    * Logout.
    */
   public void logout()
   {
      principal = null;
   }

   /**
    * Are credential set.
    *
    * @return true if credentials set
    */
   public boolean isCredentialsSet()
   {
      return username != null && password != null;
   }

   /**
    * Attempts a quiet login, suppressing any login exceptions and not creating
    * any faces messages. This method is intended to be used primarily as an
    * internal API call, however has been made public for convenience.
    */
   public void quietLogin()
   {
      try
      {
         if (isCredentialsSet()) authenticate();
      }
      catch (LoginException ignored)
      {
      }
   }

   /**
    * Authenticate.
    *
    * @throws LoginException for any login exception
    */
   public void authenticate()
         throws LoginException
   {
      // If we're already authenticated, then don't authenticate again
      if (!isLoggedIn())
      {
         authenticate(getLoginContext());
      }
   }

   /**
    * Authenticate.
    *
    * @param loginContext the login exception
    * @throws LoginException for any login exception
    */
   public void authenticate(LoginContext loginContext)
         throws LoginException
   {
      try
      {
         authenticating = true;
         preAuthenticate();
         loginContext.login();
         postAuthenticate();
      }
      finally
      {
         authenticating = false;
      }
   }

   /**
    * PreAuthentificate.
    */
   protected void preAuthenticate()
   {
      unAuthenticate();
      preAuthenticationRoles.clear();
   }

   /**
    * Post authentificate.
    */
   protected void postAuthenticate()
   {
      // Populate the working memory with the user's principals
      for (Principal p : getSubject().getPrincipals())
      {
         if (!(p instanceof Group))
         {
            if (principal == null)
            {
               principal = p;
               break;
            }
         }
      }

      if (!preAuthenticationRoles.isEmpty() && isLoggedIn())
      {
         for (String role : preAuthenticationRoles)
         {
            addRole(role);
         }
         preAuthenticationRoles.clear();
      }
      password = null;
   }

   /**
    * Removes all Role objects from the security context, removes the "Roles"
    * group from the user's subject.
    */
   protected void unAuthenticate()
   {
      principal = null;

      for (Group sg : getSubject().getPrincipals(Group.class))
      {
         if (ROLES_GROUP.equals(sg.getName()))
         {
            getSubject().getPrincipals().remove(sg);
            break;
         }
      }
   }

   /**
    * Is logged in.
    * @return true if logged in
    */
   public boolean isLoggedIn()
   {
      return isLoggedIn(false);
   }

   /**
    * Is logged in.
    *
    * @param attemptLogin try to attempt login
    * @return true if logged in
    */
   public boolean isLoggedIn(boolean attemptLogin)
   {
      if (!authenticating && attemptLogin && getPrincipal() == null && isCredentialsSet())
      {
         quietLogin();
      }
      // If there is a principal set, then the user is logged in.
      return getPrincipal() != null;
   }

   /**
    * Checks if the authenticated Identity is a member of the specified role.
    *
    * @param role String The name of the role to check
    * @return boolean True if the user is a member of the specified role
    */
   public boolean hasRole(String role)
   {
      isLoggedIn(true);

      for (Group sg : getSubject().getPrincipals(Group.class))
      {
         if (ROLES_GROUP.equals(sg.getName()))
         {
            return sg.isMember(new SimplePrincipal(role));
         }
      }
      return false;
   }

   /**
    * Adds a role to the user's subject, and their security context
    *
    * @param role The name of the role to add
    * @return true if logged in
    */
   public boolean addRole(String role)
   {
      if (!isLoggedIn())
      {
         preAuthenticationRoles.add(role);
         return false;
      }
      else
      {
         for (Group sg : getSubject().getPrincipals(Group.class))
         {
            if (ROLES_GROUP.equals(sg.getName()))
            {
               return sg.addMember(new SimplePrincipal(role));
            }
         }

         SimpleGroup roleGroup = new SimpleGroup(ROLES_GROUP);
         roleGroup.addMember(new SimplePrincipal(role));
         getSubject().getPrincipals().add(roleGroup);
         return true;
      }
   }

   /**
    * Removes a role from the user's subject and their security context
    *
    * @param role The name of the role to remove
    */
   public void removeRole(String role)
   {
      for (Group sg : getSubject().getPrincipals(Group.class))
      {
         if (ROLES_GROUP.equals(sg.getName()))
         {
            Enumeration<? extends Principal> e = sg.members();
            while (e.hasMoreElements())
            {
               Principal member = e.nextElement();
               if (member.getName().equals(role))
               {
                  sg.removeMember(member);
                  break;
               }
            }

         }
      }
   }

   /**
    * Get LoginContext.
    *
    * @return the login context
    * @throws LoginException for any login exception
    */
   protected LoginContext getLoginContext() throws LoginException
   {
      if (getJaasConfigName() != null)
         return new LoginContext(getJaasConfigName(), getSubject(), getDefaultCallbackHandler());

      return new LoginContext(
            Configuration.DEFAULT_JAAS_CONFIG_NAME,
            getSubject(),
            getDefaultCallbackHandler(),
            configuration.getConfiguration()
      );
   }

   /**
    * Creates a callback handler that can handle a standard username/password
    * callback, using the username and password properties.
    *
    * @return CallbackHandler instance
    */
   protected CallbackHandler getDefaultCallbackHandler()
   {
      return new CallbackHandler()
      {
         public void handle(Callback[] callbacks)
               throws IOException, UnsupportedCallbackException
         {
            for (Callback callback : callbacks)
            {
               if (callback instanceof NameCallback)
               {
                  ((NameCallback)callback).setName(getUsername());
               }
               else if (callback instanceof PasswordCallback)
               {
                  ((PasswordCallback)callback).setPassword(getPassword() != null ? getPassword().toCharArray() : null);
               }
               else
               {
                  throw new UnsupportedCallbackException(callback, "Unsupported callback");
               }
            }

         }
      };
   }

   /**
    * Get the username.
    *
    * @return username
    */
   public String getUsername()
   {
      return username;
   }

   /**
    * Set the username.
    *
    * @param username the username
    */
   public void setUsername(String username)
   {
      this.username = username;
   }

   /**
    * Get the password.
    *
    * @return the password
    */
   public String getPassword()
   {
      return password;
   }

   /**
    * Set the password.
    *
    * @param password the password
    */
   public void setPassword(String password)
   {
      this.password = password;
   }

   /**
    * Get the principal.
    *
    * @return the principal
    */
   public Principal getPrincipal()
   {
      return principal;
   }

   /**
    * Get the subject.
    *
    * @return the subject
    */
   public Subject getSubject()
   {
      return subject;
   }

   /**
    * Get the jaas config name.
    *
    * @return the jaas config name
    */
   public String getJaasConfigName()
   {
      return jaasConfigName;
   }

   /**
    * Set the jaas config name.
    *
    * @param jaasConfigName the jass config name
    */
   public void setJaasConfigName(String jaasConfigName)
   {
      this.jaasConfigName = jaasConfigName;
   }

   /**
    * Set the configuration.
    *
    * @param configuration the configuration
    */
   @Inject
   public void setConfiguration(Configuration configuration)
   {
      this.configuration = configuration;
   }

}
