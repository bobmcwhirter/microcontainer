/*
 * 
 */

package org.jboss.test.kernel.inject.support;

import java.util.Collection;
import java.util.Map;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class PropertyInjectTestObject
{

   private TesterInterface testerInterface;
   private DuplicateTester duplicateTester;
   private Collection collection;
   private Map map;

   public TesterInterface getTesterInterface()
   {
      return testerInterface;
   }

   public void setTesterInterface(TesterInterface testerInterface)
   {
      this.testerInterface = testerInterface;
   }

   public DuplicateTester getDuplicateTester()
   {
      return duplicateTester;
   }

   public void setDuplicateTester(DuplicateTester duplicateTester)
   {
      this.duplicateTester = duplicateTester;
   }

   public Collection getCollection()
   {
      return collection;
   }

   public void setCollection(Collection collection)
   {
      this.collection = collection;
   }

   public Map getMap()
   {
      return map;
   }

   public void setMap(Map map)
   {
      this.map = map;
   }

}
