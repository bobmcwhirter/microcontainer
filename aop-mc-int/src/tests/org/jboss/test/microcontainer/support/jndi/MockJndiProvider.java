/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.microcontainer.support.jndi;

import java.io.IOException;
import java.rmi.MarshalledObject;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class MockJndiProvider implements Context
{
   private static ConcurrentHashMap<Object, Object> bindings = new ConcurrentHashMap<Object, Object>();
   private Hashtable<?, ?> env;

   MockJndiProvider(Hashtable<?, ?> env)
   {
      this.env = env;
   }
   public Object addToEnvironment(String propName, Object propVal) throws NamingException
   {
      // TODO Auto-generated method stub
      return null;
   }
   public void bind(Name name, Object obj) throws NamingException
   {
      String sname = name.toString();
      bind(sname, obj);
   }
   public void bind(String name, Object obj) throws NamingException
   {
      try
      {
         MarshalledObject mo = new MarshalledObject(obj);
         bindings.put(name, mo);
      }
      catch(IOException e)
      {
         NamingException ex = new NamingException("Failed to bind name: "+name);
         ex.setRootCause(e);
         throw ex;
      }
   }
   public void close() throws NamingException
   {
   }
   public Name composeName(Name name, Name prefix) throws NamingException
   {
      // TODO Auto-generated method stub
      return null;
   }
   public String composeName(String name, String prefix) throws NamingException
   {
      // TODO Auto-generated method stub
      return null;
   }
   public Context createSubcontext(Name name) throws NamingException
   {
      // TODO Auto-generated method stub
      return null;
   }
   public Context createSubcontext(String name) throws NamingException
   {
      // TODO Auto-generated method stub
      return null;
   }
   public void destroySubcontext(Name name) throws NamingException
   {
      // TODO Auto-generated method stub
      
   }
   public void destroySubcontext(String name) throws NamingException
   {
      // TODO Auto-generated method stub
      
   }
   public Hashtable<?, ?> getEnvironment() throws NamingException
   {
      return env;
   }
   public String getNameInNamespace() throws NamingException
   {
      // TODO Auto-generated method stub
      return null;
   }
   public NameParser getNameParser(Name name) throws NamingException
   {
      // TODO Auto-generated method stub
      return new MockNameParser();
   }
   public NameParser getNameParser(String name) throws NamingException
   {
      // TODO Auto-generated method stub
      return new MockNameParser();
   }
   public NamingEnumeration<NameClassPair> list(Name name) throws NamingException
   {
      // TODO Auto-generated method stub
      return null;
   }
   public NamingEnumeration<NameClassPair> list(String name) throws NamingException
   {
      // TODO Auto-generated method stub
      return null;
   }
   public NamingEnumeration<Binding> listBindings(Name name) throws NamingException
   {
      // TODO Auto-generated method stub
      return null;
   }
   public NamingEnumeration<Binding> listBindings(String name) throws NamingException
   {
      // TODO Auto-generated method stub
      return null;
   }
   public Object lookup(Name name) throws NamingException
   {
      String sname = name.toString();
      return lookup(sname);
   }
   public Object lookup(String name) throws NamingException
   {
      MarshalledObject mo = (MarshalledObject) bindings.get(name);
      Object value = null;
      if( mo != null )
      {
         try
         {
            value = mo.get();
         }
         catch(Exception e)
         {
            NamingException ex = new NamingException();
            ex.setRootCause(e);
            throw ex;
         }
      }
      return value;
   }
   public Object lookupLink(Name name) throws NamingException
   {
      // TODO Auto-generated method stub
      return null;
   }
   public Object lookupLink(String name) throws NamingException
   {
      // TODO Auto-generated method stub
      return null;
   }
   public void rebind(Name name, Object obj) throws NamingException
   {
      // TODO Auto-generated method stub
      
   }
   public void rebind(String name, Object obj) throws NamingException
   {
      bindings.put(name, obj);
   }
   public Object removeFromEnvironment(String propName) throws NamingException
   {
      // TODO Auto-generated method stub
      return null;
   }
   public void rename(Name oldName, Name newName) throws NamingException
   {
      // TODO Auto-generated method stub
      
   }
   public void rename(String oldName, String newName) throws NamingException
   {
      
   }
   public void unbind(Name name) throws NamingException
   {
      unbind(name.toString());
   }
   public void unbind(String name) throws NamingException
   {
      bindings.remove(name);
   }
   
}
