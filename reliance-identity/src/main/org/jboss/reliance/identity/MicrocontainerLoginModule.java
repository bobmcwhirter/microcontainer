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

import java.security.acl.Group;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.jboss.logging.Logger;
import static org.jboss.reliance.identity.Identity.ROLES_GROUP;

/**
 * Performs authentication using a Microcontainer context.
 *
 * @author Shane Bryzak
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class MicrocontainerLoginModule implements LoginModule
{
   private static final Logger log = Logger.getLogger(MicrocontainerLoginModule.class);

   protected Set<String> roles = new HashSet<String>();

   protected Subject subject;
   protected Map<String, ?> options;
   protected CallbackHandler callbackHandler;

   protected String username;
   protected String password;

   public boolean abort() throws LoginException
   {
      return true;
   }

   public boolean commit() throws LoginException
   {
      subject.getPrincipals().add(new SimplePrincipal(username));

      Group roleGroup = null;
      for (Group g : subject.getPrincipals(Group.class))
      {
         if (ROLES_GROUP.equalsIgnoreCase(g.getName()))
         {
            roleGroup = g;
            break;
         }
      }

      if (roleGroup == null) roleGroup = new SimpleGroup(ROLES_GROUP);
      for (String role : roles)
      {
         roleGroup.addMember(new SimplePrincipal(role));
      }

      subject.getPrincipals().add(roleGroup);

      return true;
   }

   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options)
   {
      this.subject = subject;
      this.options = options;
      this.callbackHandler = callbackHandler;
   }

   public boolean login() throws LoginException
   {
      try
      {
         NameCallback cbName = new NameCallback("Enter username");
         PasswordCallback cbPassword = new PasswordCallback("Enter password", false);

         // Get the username and password from the callback handler
         callbackHandler.handle(new Callback[]{cbName, cbPassword});
         username = cbName.getName();
         password = new String(cbPassword.getPassword());
      }
      catch (Exception ex)
      {
         log.error("Error logging in", ex);
         throw new LoginException(ex.getMessage());
      }

      Authenticator authenticator = getAuthenticator();
      try
      {
         return authenticator.authenticate(username, password);
      }
      catch (Exception ex)
      {
         log.error("Error invoking login method", ex);
         throw new LoginException(ex.getMessage());
      }
   }

   public boolean logout() throws LoginException
   {
      return true;
   }

   /**
    * Get the authentificator.
    *
    * @return the authentificator
    */
   protected Authenticator getAuthenticator()
   {
      return DefaultAuthenticator.INSTANCE;
   }
}

